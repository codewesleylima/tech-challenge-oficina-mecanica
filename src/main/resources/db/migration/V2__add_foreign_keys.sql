ALTER TABLE users
    ADD CONSTRAINT fk_users_customer
    FOREIGN KEY (customer_id) REFERENCES customers (id);

ALTER TABLE vehicles
    ADD CONSTRAINT fk_vehicles_customer
    FOREIGN KEY (customer_id) REFERENCES customers (id);

ALTER TABLE service_orders
    ADD CONSTRAINT fk_service_orders_customer
    FOREIGN KEY (customer_id) REFERENCES customers (id);

ALTER TABLE service_orders
    ADD CONSTRAINT fk_service_orders_vehicle
    FOREIGN KEY (vehicle_id) REFERENCES vehicles (id);

ALTER TABLE budgets
    ADD CONSTRAINT fk_budgets_service_order
    FOREIGN KEY (service_order_id) REFERENCES service_orders (id);

ALTER TABLE budget_items
    ADD CONSTRAINT fk_budget_items_budget
    FOREIGN KEY (budget_id) REFERENCES budgets (id) ON DELETE CASCADE;

-- Índices para as colunas de FK mais consultadas (acelera joins e validação de FK).
CREATE INDEX idx_vehicles_customer       ON vehicles (customer_id);
CREATE INDEX idx_service_orders_customer ON service_orders (customer_id);
CREATE INDEX idx_service_orders_vehicle  ON service_orders (vehicle_id);
CREATE INDEX idx_service_orders_status   ON service_orders (status);
CREATE INDEX idx_budget_items_budget     ON budget_items (budget_id);
