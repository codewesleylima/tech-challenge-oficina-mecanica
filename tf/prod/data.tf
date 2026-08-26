data "aws_iam_role" "lab_role" {
  name = "LabRole"
}
data "aws_caller_identity" "current" {}

data "aws_eks_cluster" "cluster" {
  name = aws_eks_cluster.main.name
}

# Usa var.clusterName em vez de aws_eks_cluster.main.name de proposito: este data
# source apenas gera um token STS presigned localmente (nao chama a API do EKS), e
# referenciar o recurso faria o Terraform adiar a leitura para o apply sempre que o
# cluster tivesse mudanca pendente, deixando o token vazio durante o plan.
data "aws_eks_cluster_auth" "auth" {
  name = var.clusterName
}