-- A recusa do orçamento passou a encerrar a ordem de serviço em vez de devolvê-la ao diagnóstico.
ALTER TABLE service_orders
    DROP CONSTRAINT chk_service_orders_status;

ALTER TABLE service_orders
    ADD CONSTRAINT chk_service_orders_status CHECK (
        status IN ('RECEIVED', 'IN_DIAGNOSIS', 'AWAITING_APPROVAL', 'CANCELED', 'IN_EXECUTION', 'FINALIZED', 'DELIVERED')
    );
