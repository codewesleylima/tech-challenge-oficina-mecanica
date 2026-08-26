locals {
  app_log_group = "/aws/containerinsights/${var.clusterName}/application"

  api_filter = "filter kubernetes.container_name = \"api-container\" and log not like /otel.javaagent/ and log not like /^\\s+at /"
}


resource "aws_cloudwatch_query_definition" "api_logs" {
  name            = "${var.projectName}/API - logs da aplicacao"
  log_group_names = [local.app_log_group]

  query_string = <<-QUERY
    fields @timestamp, kubernetes.pod_name, log
    | ${local.api_filter}
    | sort @timestamp desc
    | limit 200
  QUERY
}

resource "aws_cloudwatch_query_definition" "api_logins" {
  name            = "${var.projectName}/API - tentativas de login"
  log_group_names = [local.app_log_group]

  query_string = <<-QUERY
    fields @timestamp, kubernetes.pod_name
    | filter log like /User login/
    | parse log "User login: *" as email
    | sort @timestamp desc
    | limit 100
  QUERY
}

resource "aws_cloudwatch_query_definition" "api_errors" {
  name            = "${var.projectName}/API - erros e excecoes"
  log_group_names = [local.app_log_group]

  query_string = <<-QUERY
    fields @timestamp, kubernetes.pod_name, log
    | filter kubernetes.container_name = "api-container"
    | filter log like /ERROR|Exception|WARN/
    | sort @timestamp desc
    | limit 100
  QUERY
}

resource "aws_cloudwatch_dashboard" "api" {
  dashboard_name = "${var.projectName}-api"

  dashboard_body = jsonencode({
    widgets = [
      {
        type   = "log"
        x      = 0
        y      = 0
        width  = 24
        height = 9
        properties = {
          title  = "API - logs da aplicacao (todas as replicas)"
          region = var.region
          view   = "table"
          query  = "SOURCE '${local.app_log_group}' | fields @timestamp, kubernetes.pod_name as pod, log | ${local.api_filter} | sort @timestamp desc | limit 200"
        }
      },
      {
        type   = "log"
        x      = 0
        y      = 9
        width  = 12
        height = 6
        properties = {
          title  = "Tentativas de login"
          region = var.region
          view   = "table"
          query  = "SOURCE '${local.app_log_group}' | fields @timestamp, kubernetes.pod_name as pod | filter log like /User login/ | parse log \"User login: *\" as email | sort @timestamp desc | limit 50"
        }
      },
      {
        type   = "log"
        x      = 12
        y      = 9
        width  = 12
        height = 6
        properties = {
          title  = "Erros e excecoes"
          region = var.region
          view   = "table"
          query  = "SOURCE '${local.app_log_group}' | fields @timestamp, kubernetes.pod_name as pod, log | filter kubernetes.container_name = \"api-container\" | filter log like /ERROR|Exception/ | sort @timestamp desc | limit 50"
        }
      },
      {
        type   = "metric"
        x      = 0
        y      = 15
        width  = 12
        height = 6
        properties = {
          title   = "CPU / memoria dos pods da API"
          region  = var.region
          view    = "timeSeries"
          stat    = "Average"
          period  = 60
          stacked = false
          metrics = [
            ["ContainerInsights", "pod_cpu_utilization", "Service", "api-service", "ClusterName", var.clusterName],
            [".", "pod_memory_utilization", ".", ".", ".", "."]
          ]
          yAxis = { left = { min = 0, max = 100, label = "%" } }
        }
      },
      {
        type   = "metric"
        x      = 12
        y      = 15
        width  = 12
        height = 6
        properties = {
          title  = "Replicas da API em execucao"
          region = var.region
          view   = "timeSeries"
          stat   = "Average"
          period = 60
          metrics = [
            ["ContainerInsights", "pod_container_status_running", "Service", "api-service", "ClusterName", var.clusterName]
          ]
        }
      }
    ]
  })
}
