resource "kubectl_manifest" "api-deployment" {
  depends_on = [aws_eks_cluster.main, aws_eks_addon.metrics_server, kubectl_manifest.api-secret]
  yaml_body  = <<YAML
apiVersion: apps/v1
kind: Deployment
metadata:
  name: api
  namespace: prod
spec:
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 0   # nenhum pod antigo sai antes de um novo estar Ready
  selector:
    matchLabels:
      app: api       
  template:
    metadata:
      labels:
        app: api
      annotations:
        appPublicUrlHash: "${sha1(var.appPublicUrl)}"
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
        startupProbe:
          httpGet:
            path: /actuator/health/readiness
            port: 8080
          periodSeconds: 5
          failureThreshold: 30   # tolera até 150s de boot
        readinessProbe:
          httpGet:
            path: /actuator/health/readiness
            port: 8080
          periodSeconds: 5
          timeoutSeconds: 3
          failureThreshold: 3
        livenessProbe:
          httpGet:
            path: /actuator/health/liveness
            port: 8080
          periodSeconds: 10
          timeoutSeconds: 3
          failureThreshold: 3
        resources:
          requests:
            cpu: "250m"      # 100 millicores (0.1 CPU)
            memory: "348Mi"  # 256 Megabytes
          limits:
              cpu: "500m"      # Máximo de 0.5 vCPU
              memory: "512Mi"  # Máximo de 512 MB
YAML
}