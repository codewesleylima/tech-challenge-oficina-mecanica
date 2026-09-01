ALTER TABLE budgets
DROP CONSTRAINT chk_budgets_status;

ALTER TABLE budgets
    ADD CONSTRAINT chk_budgets_status CHECK (
        status IN ('DRAFT', 'FINALIZED', 'DECLINED', 'APPROVED')
        );
