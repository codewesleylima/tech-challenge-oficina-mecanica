# Oficina Mecânica — Collection

## Como importar no Insomnia

1. Abra o Insomnia
2. `Application` → `Import` → `From File`
3. Selecione `CURL/oficina-mecanica.json`
4. Selecione o environment **local** e preencha `token`, `customerId`, `vehicleId`, `serviceOrderId` conforme for testando

## Fluxo de uso

1. **Auth** → Register → Login (copie o token para o environment)
2. **Customers** → Register Customer (copie o `customerId`)
3. **Vehicles** → Register Vehicle (copie o `vehicleId`)
4. **Service Orders** → 01 a 14 em ordem (copie o `serviceOrderId` após o 01)
5. **Budget** → 01 a 05 entre os steps 08 e 09 da OS

## Observações

- O step 09 (Finalize Diagnosis) exige que o orçamento esteja finalizado com ao menos 1 item antes de ser chamado
- Caso o cliente rejeite o orçamento (step 11), o fluxo retorna para IN_DIAGNOSIS e o ciclo do Budget deve ser refeito
- As variáveis de environment são atualizadas manualmente após cada resposta
