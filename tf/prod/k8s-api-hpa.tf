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
    name: api                   # nome do Deployment em k8s-api-deployment.tf
  minReplicas: 1                
  maxReplicas: 5                
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70 
  behavior:
    scaleUp:
      stabilizationWindowSeconds: 30 
      policies:
      - type: Percent
        value: 100
        periodSeconds: 15
YAML
}