resource "kubectl_manifest" "api-hpa" {
  depends_on = [aws_eks_cluster.main, kubectl_manifest.api-deployment]
  yaml_body  = <<YAML
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: api-hpa
  namespace: prod
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: api                  
  minReplicas: 2                
  maxReplicas: 5                
  # CPU e memoria dependem do metrics-server (aws_eks_addon.metrics_server, em cluster.tf);
  # sem ele o HPA fica em <unknown>/70% e nao escala.
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70 
  - type: Resource
    resource:
      name: memory
      target:
        type: Utilization
        averageUtilization: 80
  behavior:
    scaleUp:
      stabilizationWindowSeconds: 30 
      policies:
      - type: Percent
        value: 100
        periodSeconds: 15
    scaleDown:
      stabilizationWindowSeconds: 60
      policies:
      - type: Percent
        value: 100
        periodSeconds: 15
YAML
}