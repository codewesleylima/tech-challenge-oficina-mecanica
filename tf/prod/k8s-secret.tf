resource "kubectl_manifest" "postgres-secret" {
  depends_on = [kubectl_manifest.namespace]
  yaml_body  = <<YAML
apiVersion: v1
kind: Secret
metadata:
  name: postgres-secret
  namespace: prod
type: Opaque
stringData:
  POSTGRES_DB: "${var.postgresDb}"
  POSTGRES_USER: "${var.postgresUser}"
  POSTGRES_PASSWORD: "${var.postgresPassword}"

YAML
}
resource "kubectl_manifest" "api-secret" {
  depends_on = [kubectl_manifest.namespace]
  yaml_body  = <<YAML
apiVersion: v1
kind: Secret
metadata:
  name: api-secret
  namespace: prod
type: Opaque
stringData:
  SPRING_DATASOURCE_URL: "jdbc:postgresql://postgres-service:5432/${var.postgresDb}"
  SPRING_DATASOURCE_USERNAME: "${var.postgresUser}"
  SPRING_DATASOURCE_PASSWORD: "${var.postgresPassword}"
  JWT_SECRET: "${var.jwtSecret}"

YAML
}