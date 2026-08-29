resource "minikube_cluster" "dev" {
  cluster_name = var.cluster_name
  driver       = var.driver
  nodes        = var.nodes
  cpus         = var.cpus
  memory       = var.memory
  addons       = var.addons
}

resource "kubernetes_namespace" "oficina" {
  depends_on = [minikube_cluster.dev]

  metadata {
    name = var.namespace

    labels = {
      "app.kubernetes.io/part-of" = "oficina-mecanica"
      environment                 = "dev"
    }
  }
}
