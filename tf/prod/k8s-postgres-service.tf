resource "kubectl_manifest" "postgres-service" {
  depends_on = [kubectl_manifest.postgres-deployment, kubectl_manifest.api-secret]
  yaml_body  = <<YAML
apiVersion: v1
kind: Service
metadata:
  name: postgres-service
  namespace: prod
  annotations:
    # Torna o Load Balancer acessível pela internet
    service.beta.kubernetes.io/aws-load-balancer-scheme: "internet-facing"
    # Anexa o seu Security Group customizado ao Load Balancer
    service.beta.kubernetes.io/aws-load-balancer-extra-security-groups: "${aws_security_group.sg.id}"
spec:
  selector:
    app: postgres
  ports:
    - protocol: TCP
      port: 5432
      targetPort: 5432
  type: LoadBalancer

YAML
}