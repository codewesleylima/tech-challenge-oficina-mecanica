# Oficina Mecânica - Gestão de Serviços

MVP do back-end de um Sistema Integrado de Atendimento e Execução de Serviços para uma oficina
mecânica de médio porte. O sistema digitaliza o processo de atendimento, diagnóstico, orçamento,
execução e entrega de veículos, substituindo anotações manuais e planilhas — eliminando erros de
priorização, falhas no controle de peças e perda de histórico de clientes e veículos.

O projeto é um **monolito** desenvolvido com **Domain-Driven Design (DDD)**, expondo **APIs REST**
documentadas via **Swagger** e protegidas por **autenticação JWT**.

## Funcionalidades implementadas (MVP)

**Cadastros e catálogo**
- Cadastro de cliente com identificação por CPF/CNPJ (validação de dados sensíveis com dígitos verificadores)
- Cadastro de veículo (placa no padrão Mercosul, marca, modelo, ano, km)
- CRUD completo de **serviços** (catálogo)
- CRUD completo de **peças/insumos** com **controle de estoque** (entrada/saída)

**Ordem de Serviço (OS)**
- Abertura da OS vinculando cliente e veículo
- Orçamento da OS com peças e serviços e **cálculo automático do total**
- Aprovação/rejeição do orçamento pelo cliente
- Descrição do diagnóstico registrada ao encerrar o diagnóstico
- Registro de tempo de execução do serviço
- Fila de atendimento por prioridade (aumentar/diminuir prioridade)
- Listagem e detalhamento de OS (por id, por cliente e por status)

**Acompanhamento (fluxo de status)**

A OS percorre os estados abaixo, com transição automática conforme as ações no sistema:

```
RECEBIDA ──► EM_DIAGNÓSTICO ──► AGUARDANDO_APROVAÇÃO ──► EM_EXECUÇÃO ──► FINALIZADA ──► ENTREGUE
(RECEIVED)   (IN_DIAGNOSIS)     (AWAITING_APPROVAL)       (IN_EXECUTION)   (FINALIZED)    (DELIVERED)
                   ▲                     │
                   └──── rejeitar ◄──────┘
```

**Segurança e qualidade**
- Autenticação JWT (HS256) nas APIs administrativas
- Validação de dados sensíveis (CPF/CNPJ, placa de veículo)
- Tratamento global de exceções com respostas de erro padronizadas
- Testes unitários (domínio e casos de uso) e de integração (fluxo completo da OS)

> **Fora do escopo desta versão:** notificações/comunicação em tempo real com o cliente,
> relatório de tempo médio de execução e CRUD completo (update/delete)
> de clientes e veículos. Itens previstos para evoluções futuras.

## Arquitetura

Monolito em camadas seguindo DDD, organizado por **módulos** (bounded contexts) em
`src/main/java/.../modules`:

| Módulo | Responsabilidade |
|---|---|
| `auth` | Registro/login de usuários e emissão/validação de JWT |
| `register` | Cadastro de clientes e veículos |
| `inventory` | Catálogo de peças e serviços + controle de estoque |
| `serviceorder` | Ordem de serviço, orçamento, diagnóstico e ciclo de vida |
| `shared` | Exceções, handler global de erros e blocos de domínio comuns |

Cada módulo é dividido em `domain` (entidades, value objects, repositórios),
`application` (casos de uso, comandos, respostas), `infrastructure` (persistência JPA, mappers,
segurança) e `presentation` (controllers e DTOs).

## Tecnologias Utilizadas
- **Java 21** + **Spring Boot 3.2** (API REST, porta `8080`)
- **Spring Security** + **OAuth2 Resource Server** (JWT HS256)
- **Spring Data JPA** / **Hibernate 6**
- **PostgreSQL 15** (banco de dados relacional, porta `5432`)
- **H2** (em memória, apenas para os testes de integração)
- **Gradle** (build via wrapper `./gradlew`)
- **Docker** + **Docker Compose** (orquestração local)
- **springdoc-openapi** (Swagger UI)

### Por que PostgreSQL?
O domínio é fortemente relacional — cliente → veículo → ordem de serviço → orçamento → itens —
e o ciclo de vida da OS exige **consistência transacional (ACID)** nas mudanças de status e na
montagem/finalização do orçamento. O PostgreSQL atende a esses requisitos com maturidade, é
open-source, tem suporte nativo a `UUID` (usado como identificador das entidades) e excelente
integração com Spring Data JPA, sendo a escolha natural para garantir integridade do histórico
de clientes, veículos e atendimentos.

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
Com uma instância do PostgreSQL acessível (ex.: `docker compose up postgres -d`), aponte o
datasource via variáveis de ambiente e inicie a aplicação com o Gradle wrapper:

```bash
export SPRING_PROFILES_ACTIVE=local
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/oficina_db
export SPRING_DATASOURCE_USERNAME=oficina
export SPRING_DATASOURCE_PASSWORD=sua_senha
export JWT_SECRET=defina-um-segredo-com-no-minimo-256-bits-32-chars
./gradlew bootRun
```

> O `JWT_SECRET` precisa ter **no mínimo 256 bits (32 caracteres)**, caso contrário o login falha.
> O perfil `local` usa `ddl-auto=update`, criando/atualizando as tabelas automaticamente.
> No IntelliJ, defina essas variáveis em `Run` → `Edit Configurations` → *Environment variables*.

A documentação da API (Swagger UI) estará disponível em
`http://localhost:8080/swagger-ui.html` após a aplicação iniciar.

## API

Os endpoints são RESTful e, exceto `POST /auth/register` e `POST /auth/login`, exigem o header
`Authorization: Bearer <token>`.

| Área | Principais endpoints |
|---|---|
| Autenticação | `POST /auth/register`, `POST /auth/login` |
| Clientes | `POST /customers/register` |
| Veículos | `POST /vehicles/register` |
| Peças (estoque) | `POST/GET/PUT/DELETE /part`, `PATCH /part/{id}/stock/{increase\|decrease}` |
| Serviços | `POST/GET/PUT/DELETE /service` |
| Ordem de Serviço | `POST /service-orders`, `GET /service-orders/{id}`, `GET /service-orders?status=`, `GET /service-orders/customer/{id}`, `GET /service-orders/pullNext`, `PATCH .../priority/{increase\|decrease}`, `PATCH .../start-diagnosis`, `PATCH .../finalize-diagnosis`, `PATCH .../execute`, `PATCH .../reject-budget`, `POST .../time-records`, `PATCH .../finalize`, `PATCH .../deliver` |
| Orçamento | `POST /service-orders/{id}/budget`, `GET .../budget`, `POST .../budget/parts`, `POST .../budget/services`, `PATCH .../budget/finalize` |

A descrição completa, com corpo de cada requisição e a ordem de execução do fluxo, está em
[`CURL/README.md`](CURL/README.md). Uma collection pronta para o Insomnia está em
[`CURL/oficina-mecanica.json`](CURL/oficina-mecanica.json).

## Testes

A suíte cobre os domínios críticos (Ordem de Serviço, Orçamento e value objects de validação),
casos de uso e um teste de integração que exercita o **fluxo completo da OS de ponta a ponta**.
Os testes de integração rodam contra um banco **H2 em memória** (profile `test`), sem necessidade
de Docker ou PostgreSQL.

```bash
./gradlew test          # executa toda a suíte
./gradlew clean build   # compila, empacota e executa os testes
```

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
