# Oficina Mecânica - Gestão de Serviços

Este projeto tem como objetivo principal fornecer um sistema de gestão de serviços automotivos que permite
ao cliente acompanhar em tempo real o progresso da manutenção do seu veículo, autorizar reparos adicionais
e receber notificações sobre o status do atendimento. Além disso, oferece aos mecânicos uma interface para
atualizar o status dos serviços, registrar reparos adicionais e se comunicar com os clientes de forma eficiente.

Este projeto nasceu de uma necessidade real de uma oficina mecânica que trabalhava de forma desorganizada, operando com anotações manuais,
gerando erros de priorização, perda de histórico e ineficiência no fluxo de orçamentos.
Nossa solução visa digitalizar todo esse processo.

## Funcionalidades Principais
- Identificação do cliente por CPF/CNPJ
- Cadastro de veículo (placa, marca, modelo, ano)
- Inclusão de serviços solicitados (exemplo: troca de óleo, alinhamento)
- Possibilidade de incluir peças e insumos necessários
- Orçamento gerado automaticamente com base nos serviços e peças
- Envio do orçamento ao cliente para aprovação
- Atualizações em tempo real sobre o andamento do serviço
- Canal de comunicação entre mecânicos e clientes
- Notificações para os clientes sobre o status do atendimento

## Tecnologias Utilizadas
- **Java 21** + **Spring Boot 3.2** (API REST, porta `8080`)
- **PostgreSQL 15** (banco de dados relacional, porta `5432`)
- **Gradle** (build via wrapper `./gradlew`)
- **Docker** + **Docker Compose** (orquestração local)

## Pré-requisitos
- [Docker](https://docs.docker.com/get-docker/) e Docker Compose v2
- (Opcional, apenas para executar sem containers) JDK 21

## Configuração
A aplicação lê as credenciais do banco de dados e demais configurações a partir de variáveis de ambiente.
Um template está disponível em `.env.example` — **nunca commite o seu `.env` real** (ele já está no `.gitignore`).

```bash
cp .env.example .env      # Windows PowerShell: copy .env.example .env
```

Em seguida, edite o `.env` e defina um valor real para `DB_PASSWORD` (e `JWT_SECRET` para
ambientes fora do desenvolvimento).

| Variável | Padrão | Descrição |
|---|---|---|
| `DB_NAME` | `oficina_db` | Nome do banco de dados PostgreSQL |
| `DB_USER` | `oficina` | Usuário do PostgreSQL |
| `DB_PASSWORD` | — | Senha do PostgreSQL (defina a sua) |
| `DB_PORT` | `5432` | Porta do host mapeada para o PostgreSQL |
| `SPRING_PROFILE` | `dev` | Perfil ativo do Spring |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `none` | Estratégia DDL do Hibernate |
| `JWT_SECRET` | padrão dev | Segredo de assinatura JWT (substitua fora do dev) |
| `JWT_EXPIRATION` | `3600` | Expiração do JWT em segundos |

## Executando com Docker Compose
O Docker Compose sobe dois serviços: `oficina-db` (PostgreSQL) e
`oficina-backend` (a API). O backend só inicia após o PostgreSQL reportar
saúde, e se conecta a ele pela rede interna `oficina-network`.

```bash
# Constrói as imagens e sobe tudo em segundo plano
docker compose up --build -d

# Verifica o status dos containers
docker compose ps
```

Quando saudável, a API estará disponível em `http://localhost:8080`.

### Conexão com o banco de dados
Dentro da rede do Compose, o backend acessa o banco pelo nome do serviço:

```
jdbc:postgresql://postgres:5432/oficina_db
```

Da sua máquina local (ex: um cliente SQL ou `psql`) use `localhost`:

```
host=localhost  port=5432  db=oficina_db  user=oficina
```

Esses valores vêm das variáveis `SPRING_DATASOURCE_*` injetadas no
container do backend e podem ser sobrescritos via `.env`.

### Verificando a conexão
```bash
# Verificar se o banco está aceitando conexões
docker compose exec postgres pg_isready -U oficina -d oficina_db

# Saúde do backend (inclui verificação do banco)
curl -fsS http://localhost:8080/actuator/health     # -> {"status":"UP"}

# Abrir uma sessão psql dentro do container do banco
docker compose exec postgres psql -U oficina -d oficina_db
```

### Encerrando
```bash
docker compose down          # para e remove os containers (mantém o volume de dados)
docker compose down -v       # também remove o volume de dados do PostgreSQL (apaga os dados)
```

## Executando a API sem containers (opcional)
Com uma instância do PostgreSQL acessível, aponte o datasource via variáveis de ambiente
e inicie a aplicação com o Gradle wrapper:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/oficina_db
export SPRING_DATASOURCE_USERNAME=oficina
export SPRING_DATASOURCE_PASSWORD=sua_senha
./gradlew bootRun
```

A documentação da API (Swagger UI) estará disponível em
`http://localhost:8080/swagger-ui.html` após a aplicação iniciar.

## Membros da Equipe

Agradecemos às seguintes pessoas que contribuíram para este projeto:

<table>
  <tr>
    <td align="center">
      <a href="https://www.linkedin.com/in/wesslima/" title="Wesley Lima">
        <img src="https://media.licdn.com/dms/image/v2/D4D03AQGxzuIy-ANfNA/profile-displayphoto-crop_800_800/B4DZ7HT8V.GkAI-/0/1781460357892?e=1783555200&v=beta&t=iV0RLtZj1z9zgOntL3X6Y0CzY05dJeIL8VivX5fr3RA" width="100px;" alt="Foto do Wesley Lima"/><br>
        <sub>
          <b>Wesley Lima</b>
        </sub>
      </a>
      <br>
      <sub>Backend Engineer</sub>
    </td>
    <td align="center">
      <a href="https://www.linkedin.com/in/tim-morgenstern-4581911b1/" title="Tim Morgenstern">
        <img src="https://media.licdn.com/dms/image/v2/C4E03AQG57Du9tsCS5A/profile-displayphoto-shrink_800_800/profile-displayphoto-shrink_800_800/0/1643980434981?e=1782345600&v=beta&t=r2XlUXgFNRi3C0LKczF1AGjaMhCUQSYQcLx11Ilq_Yk" width="100px;" alt="Foto do Tim Morgenstern"/><br>
        <sub>
          <b>Tim Morgenstern</b>
        </sub>
      </a>
      <br>
      <sub>Backend Engineer</sub>
    </td>
    <td align="center">
      <a href="https://www.linkedin.com/in/matheus-pitas-baptista/" title="Matheus Pitas Baptista">
        <img src="https://media.licdn.com/dms/image/v2/D4D03AQHFS4VJk5WteA/profile-displayphoto-crop_800_800/B4DZv_LwbmHYAI-/0/1769512832027?e=1782345600&v=beta&t=yZNCtRAXZNCIw2ecvKaIVvtxjQy4dkoilGYparn1br0" width="100px;" alt="Foto do Matheus Pitas Baptista"/><br>
        <sub>
          <b>Matheus Pitas Baptista</b>
        </sub>
      </a>
      <br>
      <sub>Backend Engineer</sub>
    </td>
  </tr>
</table>

## Licença
Este projeto está licenciado sob a Licença MIT — consulte o arquivo [LICENSE](LICENSE) para mais detalhes.
