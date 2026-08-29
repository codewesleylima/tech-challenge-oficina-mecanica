variable "cluster_name" {
  type        = string
  default     = "oficina-dev"
  description = "Nome do profile/cluster do minikube (também é o nome do context no kubeconfig)"
}

variable "driver" {
  type        = string
  default     = "docker"
  description = "Driver do minikube (docker, podman, virtualbox, kvm2...)"
}

variable "kubernetes_version" {
  type        = string
  default     = "v1.31.0"
  description = "Versão do Kubernetes usada pelo cluster local"
}

variable "nodes" {
  type        = number
  default     = 1
  description = "Quantidade de nós do cluster"
}

variable "cpus" {
  type        = string
  default     = "2"
  description = "CPUs alocadas para o nó do minikube (use \"max\" para todas)"
}

variable "memory" {
  type        = string
  default     = "4g"
  description = "Memória alocada para o nó do minikube"
}

variable "addons" {
  type        = list(string)
  default     = ["default-storageclass", "storage-provisioner", "metrics-server"]
  description = "Addons do minikube. O metrics-server é obrigatório para o HPA funcionar"
}

variable "namespace" {
  type        = string
  default     = "oficina"
  description = "Namespace onde a API e o banco são criados"
}

# ---------------------------------------------------------------------------
# API
# ---------------------------------------------------------------------------

variable "api_image" {
  type        = string
  default     = "timbeck97/tc-fiap:latest"
  description = "Imagem da API (mesma usada em k8s/api/api-deployment.yml)"
}

variable "api_replicas" {
  type        = number
  default     = 1
  description = "Réplicas iniciais da API (o HPA assume o controle depois)"
}

variable "api_node_port" {
  type        = number
  default     = 30080
  description = "NodePort exposta para a API"
}

variable "api_hpa_min_replicas" {
  type        = number
  default     = 1
  description = "Mínimo de réplicas do HPA da API"
}

variable "api_hpa_max_replicas" {
  type        = number
  default     = 5
  description = "Máximo de réplicas do HPA da API"
}

variable "api_hpa_cpu_target" {
  type        = number
  default     = 70
  description = "Utilização média de CPU (%) que dispara o scale do HPA"
}

variable "jwt_secret" {
  type        = string
  default     = "chave-dev-minimo-256-bits-dev-dev-dev-dev-dev-dev-dev"
  description = "Segredo de assinatura do JWT (valor de desenvolvimento; sobrescreva em terraform.tfvars)"
  sensitive   = true
}

variable "jwt_expiration_seconds" {
  type        = number
  default     = 3600
  description = "Expiração do token JWT em segundos"
}

# ---------------------------------------------------------------------------
# Banco de dados
# ---------------------------------------------------------------------------

variable "db_image" {
  type        = string
  default     = "postgres:15-alpine"
  description = "Imagem do PostgreSQL"
}

variable "db_name" {
  type        = string
  default     = "oficina_db"
  description = "Nome do banco criado na inicialização do container"
}

variable "db_user" {
  type        = string
  default     = "oficina"
  description = "Usuário do banco"
}

variable "db_password" {
  type        = string
  default     = "fiapdesafiotc"
  description = "Senha do banco (valor de desenvolvimento; sobrescreva em terraform.tfvars)"
  sensitive   = true
}

variable "db_node_port" {
  type        = number
  default     = 30432
  description = "NodePort exposta para o PostgreSQL (acesso via DBeaver/psql a partir do host)"
}

variable "db_storage_size" {
  type        = string
  default     = "2Gi"
  description = "Tamanho do volume persistente do PostgreSQL"
}
