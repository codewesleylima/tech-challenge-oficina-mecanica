resource "kubectl_manifest" "namespace" {
  # Sem isso o namespace é aplicado em paralelo à access entry/policy da
  # voclabs e o apply falha com 403 (role autenticada mas sem RBAC no cluster).
  depends_on = [aws_eks_access_policy_association.eks_policy_console]

  yaml_body = <<YAML
apiVersion: v1
kind: Namespace
metadata:
  name: prod
  YAML

}