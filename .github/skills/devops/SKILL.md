---
name: devops
description: Operates the project's Gradle, Docker, GitHub Actions, Kubernetes, AWS EKS, Terraform, and release workflows safely and reproducibly.
version: 1.0.0
---

# DevOps Skill

## Preconditions

- Verify the current branch and working tree.
- Never store AWS, Docker Hub, or cluster credentials in the repository.
- Confirm cluster, namespace, deployment, image, and service names before operating.

## Execution

1. Validate locally with the smallest existing Gradle/Docker/Kubernetes command.
2. Keep secrets in environment variables, GitHub Secrets, Kubernetes Secrets, or AWS services.
3. Verify image repository and architecture compatibility.
4. Apply infrastructure before workloads and wait for readiness.
5. For CI/CD, verify tests, image publication, AWS authentication, kubeconfig, and rollout.
6. Capture evidence from GitHub Actions and Kubernetes without exposing secret values.
