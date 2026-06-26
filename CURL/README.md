# Oficina Mecânica — Guia de Testes

Importe o arquivo `oficina-mecanica.json` no Insomnia e selecione o environment **local**.
Preencha as variáveis de environment conforme indicado em cada etapa.

---

## 1. Autenticação

### Registrar usuário
```
POST http://localhost:8080/auth/register
Content-Type: application/json

{
  "email": "admin@oficina.com",
  "password": "senha123"
}
```

### Login
```
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "email": "admin@oficina.com",
  "password": "senha123"
}
```
> Copie o `token` retornado e cole na variável `token` do environment.

---

## 2. Cadastro de Cliente

```
POST http://localhost:8080/customers/register
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "João Silva",
  "email": "joao@email.com",
  "phone": "11999998888",
  "cpf": "123.456.789-00"
}
```
> Copie o `id` retornado e cole na variável `customerId` do environment.

---

## 3. Cadastro de Veículo

```
POST http://localhost:8080/vehicles/register
Authorization: Bearer {token}
Content-Type: application/json

{
  "customerId": "{customerId}",
  "carLicensePlate": "ABC-1234",
  "model": "Civic",
  "manufacturer": "Honda",
  "kilometers": 45000,
  "year": 2020
}
```
> Copie o `id` retornado e cole na variável `vehicleId` do environment.

---

## 4. Ordem de Serviço

### 4.1 Abrir OS
```
POST http://localhost:8080/service-orders
Authorization: Bearer {token}
Content-Type: application/json

{
  "customerId": "{customerId}",
  "vehicleId": "{vehicleId}",
  "problemDescription": "Carro fazendo barulho ao frear"
}
```
> Copie o `id` retornado e cole na variável `serviceOrderId` do environment.
> Status inicial: **RECEIVED**

### 4.2 Consultar OS
```
GET http://localhost:8080/service-orders/{serviceOrderId}
Authorization: Bearer {token}
```

### 4.3 Listar por cliente
```
GET http://localhost:8080/service-orders/customer/{customerId}
Authorization: Bearer {token}
```

### 4.4 Listar por status
```
GET http://localhost:8080/service-orders?status=RECEIVED
Authorization: Bearer {token}
```
> Valores possíveis: `RECEIVED`, `IN_DIAGNOSIS`, `AWAITING_APPROVAL`, `IN_EXECUTION`, `FINALIZED`, `DELIVERED`

### 4.5 Buscar próxima da fila
```
GET http://localhost:8080/service-orders/pullNext
Authorization: Bearer {token}
```

### 4.6 Aumentar prioridade na fila
```
PATCH http://localhost:8080/service-orders/{serviceOrderId}/priority/increase
Authorization: Bearer {token}
```

### 4.7 Diminuir prioridade na fila
```
PATCH http://localhost:8080/service-orders/{serviceOrderId}/priority/decrease
Authorization: Bearer {token}
```

---

## 5. Diagnóstico

### 5.1 Iniciar diagnóstico
```
PATCH http://localhost:8080/service-orders/{serviceOrderId}/start-diagnosis
Authorization: Bearer {token}
```
> Status: **RECEIVED → IN_DIAGNOSIS**

---

## 6. Orçamento (executar enquanto status = IN_DIAGNOSIS)

### 6.1 Abrir orçamento
```
POST http://localhost:8080/service-orders/{serviceOrderId}/budget
Authorization: Bearer {token}
```

### 6.2 Adicionar peça
```
POST http://localhost:8080/service-orders/{serviceOrderId}/budget/parts
Authorization: Bearer {token}
Content-Type: application/json

{
  "itemId": "00000000-0000-0000-0000-000000000001",
  "description": "Pastilha de freio dianteira",
  "quantity": 1,
  "unitPrice": 89.90
}
```

### 6.3 Adicionar serviço
```
POST http://localhost:8080/service-orders/{serviceOrderId}/budget/services
Authorization: Bearer {token}
Content-Type: application/json

{
  "itemId": "00000000-0000-0000-0000-000000000002",
  "description": "Mão de obra - troca de freios",
  "quantity": 1,
  "unitPrice": 150.00
}
```

### 6.4 Consultar orçamento
```
GET http://localhost:8080/service-orders/{serviceOrderId}/budget
Authorization: Bearer {token}
```

### 6.5 Finalizar orçamento
```
PATCH http://localhost:8080/service-orders/{serviceOrderId}/budget/finalize
Authorization: Bearer {token}
```
> Requer ao menos 1 item no orçamento.

---

## 7. Finalizar Diagnóstico

```
PATCH http://localhost:8080/service-orders/{serviceOrderId}/finalize-diagnosis
Authorization: Bearer {token}
```
> Requer orçamento com status FINALIZED e ao menos 1 item.
> Status: **IN_DIAGNOSIS → AWAITING_APPROVAL**

---

## 8. Aprovação do Cliente

### Caminho A — Cliente aprova

```
PATCH http://localhost:8080/service-orders/{serviceOrderId}/execute
Authorization: Bearer {token}
```
> Status: **AWAITING_APPROVAL → IN_EXECUTION**

### Caminho B — Cliente rejeita

```
PATCH http://localhost:8080/service-orders/{serviceOrderId}/reject-budget
Authorization: Bearer {token}
```
> Status: **AWAITING_APPROVAL → IN_DIAGNOSIS**
> Retorna ao passo 6 para revisar o orçamento.

---

## 9. Execução

### 9.1 Registrar tempo de serviço
```
POST http://localhost:8080/service-orders/{serviceOrderId}/time-records
Authorization: Bearer {token}
Content-Type: application/json

{
  "startTime": "2026-06-26T08:00:00",
  "endTime": "2026-06-26T10:30:00",
  "notes": "Troca de pastilhas de freio concluída"
}
```
> Requer status IN_EXECUTION. `endTime` deve ser posterior a `startTime`.

### 9.2 Finalizar OS
```
PATCH http://localhost:8080/service-orders/{serviceOrderId}/finalize
Authorization: Bearer {token}
```
> Status: **IN_EXECUTION → FINALIZED**

---

## 10. Entrega

```
PATCH http://localhost:8080/service-orders/{serviceOrderId}/deliver
Authorization: Bearer {token}
```
> Status: **FINALIZED → DELIVERED**

---

## Fluxo resumido

```
RECEIVED → (start-diagnosis) → IN_DIAGNOSIS → (finalize-diagnosis) → AWAITING_APPROVAL
                                    ↑                                         |
                               (reject-budget) ←──────────────────────────────
                                                                              |
                                                                         (execute)
                                                                              ↓
                                                                        IN_EXECUTION
                                                                              ↓
                                                                         (finalize)
                                                                              ↓
                                                                          FINALIZED
                                                                              ↓
                                                                          (deliver)
                                                                              ↓
                                                                          DELIVERED
```
