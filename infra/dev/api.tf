resource "kubernetes_secret" "api" {
  metadata {
    name      = "api-secret"
    namespace = kubernetes_namespace.oficina.metadata[0].name
  }

  type = "Opaque"

  data = {
    SPRING_DATASOURCE_URL      = "jdbc:postgresql://${kubernetes_service.postgres.metadata[0].name}:5432/${var.db_name}"
    SPRING_DATASOURCE_USERNAME = var.db_user
    SPRING_DATASOURCE_PASSWORD = var.db_password
    JWT_SECRET                 = var.jwt_secret
    JWT_EXPIRATION             = tostring(var.jwt_expiration_seconds)
  }
}

resource "kubernetes_deployment" "api" {
  metadata {
    name      = "api-deployment"
    namespace = kubernetes_namespace.oficina.metadata[0].name

    labels = {
      app = "api-pod"
    }
  }

  spec {
    replicas = var.api_replicas

    selector {
      match_labels = {
        app = "api-pod"
      }
    }

    template {
      metadata {
        labels = {
          app = "api-pod"
        }

        # Força novo rollout quando os valores do secret mudam
        annotations = {
          "oficina/secret-hash" = sha256(jsonencode(kubernetes_secret.api.data))
        }
      }

      spec {
        container {
          name  = "api-container"
          image = var.api_image

          port {
            container_port = 8080
          }

          env_from {
            secret_ref {
              name = kubernetes_secret.api.metadata[0].name
            }
          }

          # Dá tempo para o boot do Spring e para as migrations do Flyway
          startup_probe {
            http_get {
              path = "/actuator/health"
              port = 8080
            }

            initial_delay_seconds = 20
            period_seconds        = 5
            timeout_seconds       = 3
            failure_threshold     = 30
          }

          readiness_probe {
            http_get {
              path = "/actuator/health"
              port = 8080
            }

            period_seconds    = 10
            timeout_seconds   = 3
            failure_threshold = 3
          }

          liveness_probe {
            http_get {
              path = "/actuator/health"
              port = 8080
            }

            period_seconds    = 20
            timeout_seconds   = 3
            failure_threshold = 3
          }

          resources {
            requests = {
              cpu    = "250m"
              memory = "348Mi"
            }

            limits = {
              cpu    = "1000m"
              memory = "768Mi"
            }
          }
        }
      }
    }
  }

  # O HPA passa a controlar o número de réplicas depois do primeiro apply
  lifecycle {
    ignore_changes = [spec[0].replicas]
  }

  wait_for_rollout = true

  depends_on = [kubernetes_deployment.postgres]

  timeouts {
    create = "10m"
    update = "10m"
  }
}

resource "kubernetes_service" "api" {
  metadata {
    name      = "api-service"
    namespace = kubernetes_namespace.oficina.metadata[0].name
  }

  spec {
    type = "NodePort"

    selector = {
      app = "api-pod"
    }

    port {
      port        = 8080
      target_port = 8080
      node_port   = var.api_node_port
    }
  }
}

resource "kubernetes_horizontal_pod_autoscaler_v2" "api" {
  metadata {
    name      = "api-hpa"
    namespace = kubernetes_namespace.oficina.metadata[0].name
  }

  spec {
    scale_target_ref {
      api_version = "apps/v1"
      kind        = "Deployment"
      name        = kubernetes_deployment.api.metadata[0].name
    }

    min_replicas = var.api_hpa_min_replicas
    max_replicas = var.api_hpa_max_replicas

    metric {
      type = "Resource"

      resource {
        name = "cpu"

        target {
          type                = "Utilization"
          average_utilization = var.api_hpa_cpu_target
        }
      }
    }

    behavior {
      scale_up {
        stabilization_window_seconds = 30

        # Obrigatório quando o bloco behavior é declarado explicitamente
        select_policy = "Max"

        policy {
          type           = "Percent"
          value          = 100
          period_seconds = 15
        }
      }
    }
  }
}
