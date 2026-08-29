output "cluster_name" {
  value       = minikube_cluster.dev.cluster_name
  description = "Nome do profile do minikube criado"
}

output "cluster_host" {
  value       = minikube_cluster.dev.host
  description = "Endereço do API server do cluster"
}

output "namespace" {
  value       = kubernetes_namespace.oficina.metadata[0].name
  description = "Namespace onde a API e o banco foram criados"
}

output "api_url_command" {
  value       = "minikube -p ${var.cluster_name} service api-service -n ${var.namespace} --url"
  description = "Comando que imprime a URL acessível da API a partir do host"
}

output "api_node_port" {
  value       = var.api_node_port
  description = "NodePort da API (use com o IP retornado por 'minikube ip')"
}

output "swagger_path" {
  value       = "/swagger-ui.html"
  description = "Caminho do Swagger UI, relativo à URL da API"
}

output "db_connection_command" {
  value       = "psql -h $(minikube -p ${var.cluster_name} ip) -p ${var.db_node_port} -U ${var.db_user} -d ${var.db_name}"
  description = "Comando para conectar no Postgres a partir do host"
}

output "kubectl_context" {
  value       = var.cluster_name
  description = "Context do kubeconfig a ser usado com 'kubectl config use-context'"
}
