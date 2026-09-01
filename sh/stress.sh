#!/bin/bash

# Uso: ./stress.sh [--login] [url] [concorrencia]
# Sem url, descobre sozinho o Load Balancer do cluster no contexto atual do kubectl.
# --login estressa o POST /auth/login, o endpoint mais pesado (BCrypt);
# credenciais via --user / --pass, ou LOGIN_USER / LOGIN_PASS.
NAMESPACE=${NAMESPACE:-prod}
SERVICE=${SERVICE:-api-service}
PORT=${PORT:-8080}
ENDPOINT=${ENDPOINT:-/v3/api-docs}
METHOD=${METHOD:-GET}
BODY=${BODY:-}           # quando preenchido, vai como JSON no corpo

LOGIN_USER=${LOGIN_USER:-user@user.com}
LOGIN_PASS=${LOGIN_PASS:-teste123}
IS_LOGIN=0

while [[ "${1:-}" == --* ]]; do
  case "$1" in
    --login) IS_LOGIN=1 ;;
    --user)  shift; LOGIN_USER=$1 ;;
    --pass)  shift; LOGIN_PASS=$1 ;;
    *) echo "Opção desconhecida: $1"; exit 1 ;;
  esac
  shift
done

if [ "$IS_LOGIN" = "1" ]; then
  ENDPOINT=/auth/login
  METHOD=POST
  BODY="{\"email\":\"${LOGIN_USER}\",\"password\":\"${LOGIN_PASS}\"}"
fi

# A url é opcional: sem ela, o endereço vem do cluster.
URL=${API_URL:-}
if [[ "${1:-}" == http://* || "${1:-}" == https://* ]]; then
  URL=$1
  shift
fi

CONCORRENCIA=${1:-3}     # Concorrência padrão: 5 requisições paralelas

if [ -z "$URL" ]; then
  echo "URL não informada — buscando o Load Balancer de ${SERVICE} no namespace ${NAMESPACE}..."

  HOST=$(kubectl get svc "$SERVICE" -n "$NAMESPACE" \
    -o jsonpath='{.status.loadBalancer.ingress[0].hostname}' 2>/dev/null)

  # O ELB da AWS publica hostname; outros provedores publicam ip
  if [ -z "$HOST" ]; then
    HOST=$(kubectl get svc "$SERVICE" -n "$NAMESPACE" \
      -o jsonpath='{.status.loadBalancer.ingress[0].ip}' 2>/dev/null)
  fi

  if [ -z "$HOST" ]; then
    echo "Não foi possível descobrir a URL pelo cluster."
    echo "Confira o contexto do kubectl (kubectl config current-context) ou informe a URL:"
    echo "  $0 <url> [delay] [concorrencia]"
    exit 1
  fi

  URL="http://${HOST}:${PORT}${ENDPOINT}"
fi

CURL_ARGS=(-s -o /dev/null -w "%{http_code} " -X "$METHOD")
if [ -n "$BODY" ]; then
  CURL_ARGS+=(-H "Content-Type: application/json" -d "$BODY")
fi

echo "Iniciando teste de carga em: $METHOD $URL"
echo "Concorrência por ciclo: ${CONCORRENCIA} requisições"
echo "--------------------------------------------------"

while true; do
  for i in $(seq 1 $CONCORRENCIA); do
    curl "${CURL_ARGS[@]}" "$URL" &
  done
  
  wait
  echo ""


done
