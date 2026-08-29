# Oficina Mecânica — Collection

## Como importar no Insomnia

1. Abra o Insomnia
2. `Application` → `Import` → `From File`
3. Selecione `CURL/oficina-mecanica.json`
4. Selecione o environment **local** e preencha as variáveis conforme for testando

---

## Fluxo de uso — endpoint por endpoint

### 1. Auth → Register
Cria o usuário no sistema. Necessário antes de qualquer outra chamada.

### 2. Auth → Login
Autentica o usuário. Copie o `token` retornado e cole na variável `token` do environment.

### 3. Customers → Register Customer
Cadastra um cliente. Copie o `id` retornado e cole na variável `customerId` do environment.

### 4. Vehicles → Register Vehicle
Cadastra um veículo vinculado ao cliente criado no passo anterior. Copie o `id` retornado e cole na variável `vehicleId` do environment.

### 5. Service Orders → 01 - Open Service Order
Abre uma nova ordem de serviço. Copie o `id` retornado e cole na variável `serviceOrderId` do environment.
Status inicial: **RECEIVED**

### 6. Service Orders → 02 - Get Service Order
Consulta os dados e o status atual da OS.

### 7. Service Orders → 03 - List By Customer
Lista todas as ordens de serviço do cliente.

### 8. Service Orders → 04 - List By Status
Lista as ordens de serviço filtrando por status. Altere o query param `status` conforme necessário.

### 9. Service Orders → 05 - Pull Next (Queue)
Retorna a próxima OS da fila de acordo com a prioridade.

### 10. Service Orders → 06 - Increase Priority
Aumenta a prioridade da OS na fila.

### 11. Service Orders → 07 - Decrease Priority
Diminui a prioridade da OS na fila.

### 12. Service Orders → 08 - Start Diagnosis
Inicia o diagnóstico da OS.
Status: **RECEIVED → IN_DIAGNOSIS**

### 13. Budget → 01 - Add Items (Batch)
Adiciona peças e serviços ao orçamento em **uma única chamada**. Não existe um endpoint para "abrir"
o orçamento: ele é criado sob demanda no primeiro item adicionado. Requer a OS em **IN_DIAGNOSIS**.
Os preços vêm do catálogo — o corpo envia apenas `type`, `itemId`, `description` e `quantity`.

> Para adicionar itens avulsos, use `03 - Add Part` e `04 - Add Service`.

### 14. Budget → 02 - Get Budget
Consulta o orçamento com os itens adicionados e o total calculado.

### 15. Service Orders → 09 - Finalize Diagnosis
Finaliza o diagnóstico e envia para aprovação do cliente. **Finaliza o orçamento automaticamente** e
aceita os itens no corpo (campo `items`, opcional) — o que permite montar e fechar o orçamento na
mesma chamada. Se o orçamento já foi montado nos passos anteriores, envie apenas `diagnosis`.
Exige ao menos um item no orçamento.
Status: **IN_DIAGNOSIS → AWAITING_APPROVAL**

> `05 - Finalize Budget` continua disponível para quem quiser fechar o orçamento antes, mas é opcional.

### 16. Service Orders → 10 - Execute Order _(cliente aprova)_
Aprova o orçamento e inicia a execução (baixa o estoque das peças).
Status: **AWAITING_APPROVAL → IN_EXECUTION**

> **Caminho alternativo — cliente rejeita:**
> Chame `11 - Reject Budget` no lugar do passo 16.
> Status: **AWAITING_APPROVAL → IN_DIAGNOSIS**
> Retorne ao passo 13 para revisar o orçamento.

### 17. Service Orders → 12 - Complete Service Item
Conclui a execução de um item de serviço (grava a data de fim). Requer status **IN_EXECUTION**.
Copie o `budgetItemId` do item do tipo `SERVICE` retornado em **Budget → Get Budget** para a variável `serviceBudgetItemId` do environment.

### 18. Service Orders → 13 - Finalize Order
Finaliza a execução da OS. **Só é permitido quando todos os itens de serviço estão concluídos.**
Status: **IN_EXECUTION → FINALIZED**

### 19. Service Orders → 14 - Deliver
Registra a entrega do veículo ao cliente.
Status: **FINALIZED → DELIVERED**

### 20. Service Orders → 15 - Average Execution Time (Metrics)
Retorna o **tempo médio de execução por tipo de serviço na OS** (em minutos), com a contagem de execuções concluídas consideradas.
