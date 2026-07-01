CREATE TABLE users (
    id          uuid           NOT NULL,
    customer_id uuid,
    email       varchar(255)   NOT NULL,
    password    varchar(255)   NOT NULL,
    role        varchar(255)   NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email),
    CONSTRAINT chk_users_role CHECK (role IN ('ROLE_USER', 'ROLE_ADMIN', 'ROLE_CUSTOMER'))
);


CREATE TABLE customers (
    id        uuid         NOT NULL,
    name      varchar(255) NOT NULL,
    email     varchar(255) NOT NULL,
    phone     varchar(255) NOT NULL,
    cnpj_cpf  varchar(255) NOT NULL,
    CONSTRAINT pk_customers PRIMARY KEY (id)
);

CREATE TABLE vehicles (
    id                 uuid         NOT NULL,
    customer_id        uuid         NOT NULL,
    car_license_plate  varchar(255) NOT NULL,
    manufacturer       varchar(255) NOT NULL,
    model              varchar(255) NOT NULL,
    year               integer      NOT NULL,
    kilometers         integer      NOT NULL,
    CONSTRAINT pk_vehicles PRIMARY KEY (id)
);


CREATE TABLE parts (
    id          uuid           NOT NULL,
    name        varchar(255)   NOT NULL,
    description varchar(150),
    price       numeric(38, 2) NOT NULL,
    quantity    integer        NOT NULL,
    CONSTRAINT pk_parts PRIMARY KEY (id)
);

CREATE TABLE services (
    id          uuid           NOT NULL,
    name        varchar(255)   NOT NULL,
    description varchar(150),
    price       numeric(38, 2) NOT NULL,
    CONSTRAINT pk_services PRIMARY KEY (id)
);


CREATE TABLE service_orders (
    id                    uuid         NOT NULL,
    customer_id           uuid         NOT NULL,
    vehicle_id            uuid         NOT NULL,
    problem_description   varchar(255) NOT NULL,
    diagnosis             text,
    status                varchar(255) NOT NULL,
    priority              integer      NOT NULL,
    opened_at             timestamp(6) without time zone NOT NULL,
    execution_started_at  timestamp(6) without time zone,
    concluded_at          timestamp(6) without time zone,
    CONSTRAINT pk_service_orders PRIMARY KEY (id),
    CONSTRAINT chk_service_orders_status CHECK (
        status IN ('RECEIVED', 'IN_DIAGNOSIS', 'AWAITING_APPROVAL', 'IN_EXECUTION', 'FINALIZED', 'DELIVERED')
    )
);

CREATE TABLE budgets (
    id                uuid         NOT NULL,
    service_order_id  uuid         NOT NULL,
    status            varchar(255) NOT NULL,
    created_at        timestamp(6) without time zone NOT NULL,
    CONSTRAINT pk_budgets PRIMARY KEY (id),
    CONSTRAINT uk_budgets_service_order_id UNIQUE (service_order_id),
    CONSTRAINT chk_budgets_status CHECK (status IN ('DRAFT', 'FINALIZED'))
);

CREATE TABLE budget_items (
    id           uuid           NOT NULL,
    budget_id    uuid           NOT NULL,
    type         varchar(255)   NOT NULL,
    item_id      uuid           NOT NULL,
    description  varchar(255)   NOT NULL,
    quantity     integer        NOT NULL,
    unit_price   numeric(10, 2) NOT NULL,
    completed_at timestamp(6) without time zone,
    CONSTRAINT pk_budget_items PRIMARY KEY (id),
    CONSTRAINT chk_budget_items_type CHECK (type IN ('PART', 'SERVICE'))
);
