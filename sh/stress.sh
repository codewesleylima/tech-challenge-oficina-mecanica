#!/bin/bash

# Parâmetros com valores padrão caso não sejam passados no terminal
DELAY=${1:-0.5}          # Delay padrão: 0.5 segundos (se não informado)
CONCORRENCIA=${2:-5}     # Concorrência padrão: 5 requisições paralelas
URL="http://aa1dac020e4954819a7124f58f333b7b-1617088812.us-east-1.elb.amazonaws.com:8080/swagger-ui/index.html"

echo "🚀 Iniciando teste de carga em: $URL"
echo "⏱️  Delay entre ciclos: ${DELAY}s"
echo "⚡ Concorrência por ciclo: ${CONCORRENCIA} requisições"
echo "Pressione [CTRL+C] para parar."
echo "--------------------------------------------------"

while true; do
  # Dispara as requisições em segundo plano (paralelo)
  for i in $(seq 1 $CONCORRENCIA); do
    curl -s -o /dev/null -w "%{http_code} " "$URL" &
  done
  
  # Aguarda as requisições do ciclo terminarem
  wait
  echo ""

  # Aplica o delay entre os ciclos
  #sleep $DELAY
done