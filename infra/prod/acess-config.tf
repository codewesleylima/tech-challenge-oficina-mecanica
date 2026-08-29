locals {
  # Aponta exatamente para a Role de federacao/console do AWS Academy
  voclabs_role_arn = "arn:aws:iam::${data.aws_caller_identity.current.account_id}:role/voclabs"
}

# -------------------------------------------------------------
# 1. LABROLE (role dos nos do cluster - node_role_arn em node.tf)
# -------------------------------------------------------------
# Precisa ser EC2_LINUX: e o tipo que mapeia a role para o usuario
# system:node:{{EC2PrivateDNSName}} no grupo system:nodes. Com STANDARD o
# kubelet autentica como assumed-role/LabRole/<instance-id> sem system:nodes,
# e o EKS nunca assina as CSR kubelet-serving -> o kubelet fica sem certificado
# na porta 10250 -> o metrics-server nunca fica Ready e o addon trava em CREATING.
#
# So existe UMA access entry por principal ARN, entao declarar STANDARD aqui
# impedia o EKS de criar a entrada EC2_LINUX automatica do node group.
# Entradas EC2_LINUX nao aceitam access policies (o acesso admin vem de voclabs).
resource "aws_eks_access_entry" "access_entry_lab" {
  cluster_name  = aws_eks_cluster.main.name
  principal_arn = data.aws_iam_role.lab_role.arn
  type          = "EC2_LINUX"
}

# -------------------------------------------------------------
# 2. ROLE DO CONSOLE / VOCAREUM (Libera acesso no navegador)
# -------------------------------------------------------------
resource "aws_eks_access_entry" "access_entry_console" {
  cluster_name  = aws_eks_cluster.main.name
  principal_arn = local.voclabs_role_arn
  type          = "STANDARD"
}

resource "aws_eks_access_policy_association" "eks_policy_console" {
  cluster_name  = aws_eks_cluster.main.name
  policy_arn    = "arn:aws:eks::aws:cluster-access-policy/AmazonEKSClusterAdminPolicy"
  principal_arn = local.voclabs_role_arn

  access_scope {
    type = "cluster"
  }

  depends_on = [aws_eks_access_entry.access_entry_console]
}