provider "minikube" {
  kubernetes_version = var.kubernetes_version
}

# As credenciais vêm direto do cluster criado no mesmo apply, e não do
# kubeconfig, para que um único "terraform apply" crie cluster e workloads.
provider "kubernetes" {
  host                   = minikube_cluster.dev.host
  client_certificate     = minikube_cluster.dev.client_certificate
  client_key             = minikube_cluster.dev.client_key
  cluster_ca_certificate = minikube_cluster.dev.cluster_ca_certificate
}
