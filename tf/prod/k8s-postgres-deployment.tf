resource "kubectl_manifest" "postgres-deployment" {
  depends_on = [aws_eks_cluster.main, aws_eks_addon.metrics_server, kubectl_manifest.postgres-secret]
  yaml_body  = <<YAML
apiVersion: apps/v1
kind: Deployment
metadata:
  name: postgres-deployment
  namespace: prod
spec:
  replicas: 1
  selector:
    matchLabels:
      app: postgres       
  template:
    metadata:
      labels:
        app: postgres
    spec:
      containers:
      - name: postgres
        image: postgres:15-alpine
        ports:
        - containerPort: 5432
        envFrom:
        - secretRef:
            name: postgres-secret
YAML
}