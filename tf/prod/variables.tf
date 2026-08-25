variable "projectName" {
  type        = string
  default     = "TC-FIAP"
  description = "Nome do projeto"
}
variable "region" {
  type        = string
  default     = "us-east-1"
  description = "Região onde os recursos serão criados"
}
variable "accessConfig" {
  type        = string
  default     = "API"
  description = "Access config"
}
variable "postgresDb" {
  type        = string
  default     = "oficina_db"
  description = "Nome do banco de dados"
}
variable "postgresUser" {
  type        = string
  default     = "oficina"
  description = "Usuário do banco de dados"
}
variable "postgresPassword" {
  type        = string
  sensitive   = true
  description = "Senha do banco de dados"
}
variable "jwtSecret" {
  type        = string
  sensitive   = true
  description = "Chave de assinatura do JWT (mínimo 256 bits)"
}