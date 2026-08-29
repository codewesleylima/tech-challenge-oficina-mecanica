terraform {
  required_version = ">= 1.5"

  required_providers {
    # Provider que sobe/destrói o cluster local do minikube
    minikube = {
      source  = "scott-the-programmer/minikube"
      version = "~> 0.6"
    }

    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = "~> 2.35"
    }
  }
}
