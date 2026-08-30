# Deploy AWS EKS - Tech Challenge Oficina Mecânica

## 📋 Pré-requisitos

- AWS Account com permissões para criar EKS
- AWS CLI v2 configurado: `aws configure`
- kubectl: `choco install kubernetes-cli`
- eksctl: `choco install eksctl`
- Docker Hub account (para push de imagens)

---

## 🚀 Passo 1: Criar Cluster EKS

```bash
eksctl create cluster \
  --name tech-challenge-prod \
  --region us-east-1 \
  --nodegroup-name workers \
  --node-type t3.medium \
  --nodes 2 \
  --nodes-min 1 \
  --nodes-max 3 \
  --managed
```

⏱️ Leva ~15-20 minutos.

Verificar cluster:
```bash
eksctl get cluster --name tech-challenge-prod
aws eks update-kubeconfig --name tech-challenge-prod --region us-east-1
kubectl cluster-info
```

---

## 🔐 Passo 2: Configurar GitHub Secrets

Vá em: **Settings > Secrets and variables > Actions**

Crie os seguintes secrets:

| Secret | Valor | Exemplo |
|--------|-------|---------|
| `AWS_ACCESS_KEY_ID` | Sua Access Key | `AKIA...` |
| `AWS_SECRET_ACCESS_KEY` | Sua Secret Key | `...` |
| `AWS_REGION` | Região AWS | `us-east-1` |
| `AWS_SESSION_TOKEN` | (opcional) | - |
| `EKS_CLUSTER_NAME` | Nome do cluster | `tech-challenge-prod` |
| `DOCKER_LOGIN` | User Docker Hub | `seu_usuario` |
| `DOCKER_PASSWORD` | Password Docker Hub | `...` |

**Gerar AWS Access Key:**
1. AWS Console > IAM > Users > Seu usuário
2. Security credentials > Create access key
3. Copie Access Key ID e Secret Access Key

---

## 🏗️ Passo 3: Preparar Cluster

### 3.1 Criar Namespace
```bash
kubectl apply -f k8s/namespace.yaml
```

### 3.2 Configurar Secrets de Produção
```bash
# IMPORTANTE: Altere as senhas antes de aplicar!
kubectl apply -f k8s/postgres-secret.yaml
kubectl apply -f k8s/jwt-secret.yaml
```

Ou crie manualmente:
```bash
# JWT Secret
kubectl create secret generic jwt-secret \
  --from-literal=JWT_SECRET='seu-jwt-secret-minimo-256-bits' \
  -n prod

# PostgreSQL
kubectl create secret generic postgres-secret \
  --from-literal=POSTGRES_DB=oficina_db \
  --from-literal=POSTGRES_USER=oficina \
  --from-literal=POSTGRES_PASSWORD='sua-senha-forte' \
  -n prod
```

### 3.3 Deploy PostgreSQL
```bash
kubectl apply -f k8s/postgres-deployment.yaml
```

Verificar:
```bash
kubectl get pods -n prod
kubectl get pvc -n prod
```

Aguarde PostgreSQL estar `Running` antes de prosseguir (~1-2 min).

### 3.4 Deploy Backend
```bash
kubectl apply -f k8s/backend-deployment.yaml
```

Verificar:
```bash
kubectl get deployment -n prod
kubectl get svc -n prod
```

---

## ✅ Validações

### Verificar Pods
```bash
kubectl get pods -n prod -w
```

Aguarde até todos estarem `Running` e `READY 1/1`.

### Verificar Logs
```bash
# Backend
kubectl logs -f deployment/api -n prod

# PostgreSQL
kubectl logs -f deployment/postgres -n prod
```

### Acessar API
```bash
# Obter IP externo do LoadBalancer
kubectl get svc api -n prod

# Exemplo de resposta:
# NAME   TYPE           CLUSTER-IP      EXTERNAL-IP      PORT(S)
# api    LoadBalancer   10.100.200.50   203.0.113.45     80:31234/TCP

# Acessar:
curl http://203.0.113.45/actuator/health
```

---

## 🔄 Pipeline CI/CD Automático

Quando você faz push para `main`:

1. ✅ Tests passam
2. 🐳 Docker image é buildada e enviada para Docker Hub
3. 🔑 AWS credentials são configuradas
4. ☸️ Kubeconfig do EKS é atualizado
5. 🚀 Pods do deployment `api` são reiniciados (rollout restart)
6. 📊 Nova versão é ativada com zero-downtime

**CI/CD está no arquivo:** `.github/workflows/ci.yml`

---

## 📊 Monitoramento

### Ver todos os recursos
```bash
kubectl get all -n prod
```

### Descrever Deployment
```bash
kubectl describe deployment api -n prod
```

### Port-forward local (opcional)
```bash
kubectl port-forward svc/api 8080:80 -n prod
# Depois acesse: http://localhost:8080
```

### Ver eventos
```bash
kubectl get events -n prod --sort-by='.lastTimestamp'
```

---

## 🧹 Cleanup

### Deletar tudo
```bash
kubectl delete namespace prod
```

### Deletar cluster EKS
```bash
eksctl delete cluster --name tech-challenge-prod
```

⚠️ Isso levará ~15 minutos e vai deletar todo o cluster AWS.

---

## 🔧 Troubleshooting

### Pods não iniciam
```bash
kubectl describe pod <pod-name> -n prod
kubectl logs <pod-name> -n prod
```

### PostgreSQL não conecta
```bash
# Testar conexão
kubectl run -it --rm debug --image=postgres:15-alpine --restart=Never -n prod -- psql -h postgres -U oficina -d oficina_db
```

### LoadBalancer sem EXTERNAL-IP
```bash
# Aguarde alguns minutos ou verifique IAM/security groups
kubectl describe svc api -n prod
```

---

## 📚 Recursos Adicionais

- [AWS EKS Documentation](https://docs.aws.amazon.com/eks/)
- [kubectl Cheat Sheet](https://kubernetes.io/docs/reference/kubectl/cheatsheet/)
- [Kubernetes Best Practices](https://kubernetes.io/docs/concepts/configuration/overview/)
