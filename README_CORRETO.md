# 🐳 Docker para Desenvolvimento Local

## Filosofia de Design

Este `docker-compose.yml` foi **propositalmente simplificado** para:

✅ **Localhost apenas** - Não há configuração para produção  
✅ **Leveza** - Apenas 3 serviços essenciais  
✅ **Desenvolvimento** - Otimizado para 2 pessoas localmente  
✅ **Simplicidade** - Fácil de entender e modificar

---

## 🎯 Serviços Inclusos (3 apenas)

### 1. **PostgreSQL 15 Alpine** (~170MB)
```
Imagem: postgres:15-alpine
Porta: 5432
Volume: postgres_data (local)
```
**Por quê Alpine?** Imagem mínima, rápida de baixar, perfeita para dev local.

### 2. **Spring Boot Backend** (seu código)
```
Linguagem: Java 17
Framework: Spring Boot 3.x
Porta: 8080
```
**Simplificado**: Sem health checks complexos, sem restart policies.

### 3. **Docusaurus 3.10.1** (~350MB Node)
```
Imagem: Node 20 Alpine
Porta: 3000
Volume: hot-reload automático
```
**Muito leve!** Node Alpine é a imagem oficial recomendada. Suporta hot-reload para desenvolvimento.

---

## 📊 Comparação: Antes vs Depois

### ❌ ANTES (Complexo demais para DEV local)
```
7 Serviços:
├── PostgreSQL
├── Spring Boot
├── Docusaurus
├── RabbitMQ          ← Não precisa (fase testes)
├── SonarQube         ← Roda local, não container
├── Redis             ← Não precisa agora
└── Mailhog           ← Não precisa (fase testes)

~2GB de imagens
Tempo startup: ~30s
```

### ✅ DEPOIS (Otimizado para DEV local)
```
3 Serviços essenciais:
├── PostgreSQL
├── Spring Boot
└── Docusaurus

~650MB de imagens
Tempo startup: ~8-10s
```

---

## 🚀 Como Usar (1 Comando)

```bash
# 1. Prepare
cp .env.example .env

# 2. Execute
docker-compose up

# 3. Acesse
# Docs:    http://localhost:3000
# API:     http://localhost:8080
# DB:      localhost:5432
```

**Tempo total**: ~10 segundos até estar pronto.

---

## 📋 .env Simplificado

```bash
# Apenas o essencial para DEV local

DB_NAME=oficina_db
DB_USER=postgres
DB_PASSWORD=postgres_dev

SPRING_PROFILE=dev

JWT_SECRET=dev-chave-temporaria-development-only
```

**Sem produção, sem secrets complexos, sem profiles opcionais.**

---

## 🐳 Tamanho das Imagens

| Imagem | Tamanho | Observação |
|--------|---------|-----------|
| postgres:15-alpine | ~170 MB | Oficial, recomendada |
| node:20-alpine | ~170 MB | Oficial, mínima |
| seu backend | variável | Spring Boot 3.x típico: ~500MB |
| **TOTAL** | **~850 MB** | Muito leve para 3 serviços |

---

## ⚡ Performance em Localhost

**Startup**:
- PostgreSQL: ~2s
- Backend: ~5s (Spring Boot)
- Docusaurus: ~3s (Node)
- **Total**: ~10s

**Memória**:
- Idle: ~800MB RAM
- Com uso: ~1.2GB RAM (confortável)

**Para 2 pessoas**: Mais que suficiente.

---

## 📦 Docusaurus: Por que é leve

### Dockerfile
```dockerfile
FROM node:20-alpine          # 170MB - mínimo oficial
COPY package*.json ./
RUN npm ci                   # Apenas dependências necessárias
COPY . .
EXPOSE 3000
CMD ["npm", "run", "start"]  # Hot-reload automático
```

**Total da imagem Docusaurus**: ~350-400 MB  
**Tempo build**: ~30s (primeira vez)  
**Tempo startup**: ~3s (subsequentes)

---

## 🔄 Hot-Reload (Desenvolvimento)

O Docusaurus está configurado para hot-reload:

```bash
# Edite um arquivo
echo "# Nova página" > docs/docs/teste.md

# Salve

# Recarregue http://localhost:3000 no browser
# Sua página nova já está lá!
```

**Sem rebuildar container.**

---

## 🛑 Como Parar

```bash
# Parar (mantém volumes)
docker-compose stop

# Remover containers (mantém dados)
docker-compose down

# Remover tudo (limpar BD também)
docker-compose down -v
```

---

## 🔍 Monitorar

```bash
# Ver status dos 3 containers
docker-compose ps

# Ver logs de um serviço
docker-compose logs docs
docker-compose logs backend
docker-compose logs postgres

# Tempo real
docker-compose logs -f docs
```

---

## ✅ Checklist: Tudo Funciona?

```bash
# Terminal 1
docker-compose up

# Terminal 2 - Aguarde ~10s depois execute:

# 1. Docusaurus
curl http://localhost:3000
# Esperado: HTML (status 200)

# 2. Backend
curl http://localhost:8080/actuator/health
# Esperado: {"status":"UP"}

# 3. Banco
docker exec oficina-db psql -U postgres -d oficina_db -c "SELECT 1;"
# Esperado: (1 row)
```

---

## 🎯 Filosofia de Escolhas

### Por que **apenas 3 serviços**?

1. **RabbitMQ** - Você está em fase de **testes do MVP**
2. **SonarQube** - Pode rodar **local na máquina**, não precisa container
3. **Redis** - Ainda não precisa **cache**, banco relacional é suficiente
4. **Mailhog** - Não há sistema de **notificação por email** no escopo MVP

### Por que **versões específicas leves**?

- `postgres:15-alpine` - Sempre a versão LTS leve recomendada
- `node:20-alpine` - Node 20 é LTS, Alpine é mínimo
- `openjdk:17-slim` (backend) - Java 17 é LTS, slim é enxuto

### Por que **sem health checks complexos**?

Em localhost com 2 pessoas, não precisa de:
- `start_period` complexo
- `restart: unless-stopped`
- Múltiplas retries

---

## 📈 Se Precisar Escalar Depois

Se no futuro você quiser adicionar:

```bash
# RabbitMQ - descomente serviço
# Redis - descomente serviço
# SonarQube - descomente serviço
# Mailhog - descomente serviço
```

Tudo está comentado no `docker-compose.yml` original. **Fácil de ativar.**

---

## 🤔 FAQ

**P: Por que não usar Kubernetes?**  
R: Para 2 pessoas em localhost, Kubernetes é overkill. Docker Compose é perfeito.

**P: Por que não usar Docker Swarm?**  
R: Você não vai fazer orchestration em localhost. Um único `docker-compose up` é o bastante.

**P: Por que não usar imagens produção?**  
R: Seu compose está focado em **dev local**. Produção é outro problema.

**P: E se eu quiser rodar tudo sem Docker?**  
R: Veja o `GUIA_IMPLEMENTACAO.md` - tem instrução de setup local puro.

---

## 🚀 Próximo Passo

```bash
cp .env.example .env
docker-compose up
```

**Pronto!** Seu projeto local está rodando em 10 segundos. 🎉

---

*Simplicidade, acertividade, leveza - para dev local apenas.*