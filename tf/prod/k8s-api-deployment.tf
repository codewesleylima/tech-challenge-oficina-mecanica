resource "kubectl_manifest" "api-deployment" {
  depends_on = [aws_eks_cluster.main, aws_eks_addon.metrics_server, kubectl_manifest.api-secret]
  yaml_body  = <<YAML
apiVersion: apps/v1
kind: Deployment
metadata:
  name: api
  namespace: prod
spec:
  replicas: 1
  selector:
    matchLabels:
      app: api       
  template:
    metadata:
      labels:
        app: api
    spec:
      containers:
      - name: api-container
        image: timbeck97/tc-fiap:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_OUTPUT_ANSI_ENABLED
          value: "never"
        envFrom:
        - secretRef:
            name: api-secret
        resources:
          requests:
            cpu: "250m"      # 100 millicores (0.1 CPU)
            memory: "348Mi"  # 256 Megabytes
          limits:
              cpu: "500m"      # Máximo de 0.5 vCPU
              memory: "512Mi"  # Máximo de 512 MB
YAML
}