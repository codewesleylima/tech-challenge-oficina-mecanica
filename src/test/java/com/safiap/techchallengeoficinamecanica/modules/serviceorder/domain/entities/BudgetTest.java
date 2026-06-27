package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.BudgetItemType;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.BudgetStatus;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.ConflictException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BudgetTest {

    private Budget draft() {
        return Budget.create(UUID.randomUUID());
    }

    @Test
    void createsAsDraftWithNoItems() {
        Budget budget = draft();
        assertThat(budget.getStatus()).isEqualTo(BudgetStatus.DRAFT);
        assertThat(budget.getItems()).isEmpty();
        assertThat(budget.calculateTotal()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void calculatesTotalFromPartsAndServices() {
        Budget budget = draft();
        budget.addPart(UUID.randomUUID(), "Pastilha", 2, new BigDecimal("89.90"));   // 179.80
        budget.addService(UUID.randomUUID(), "Mão de obra", 1, new BigDecimal("150.00")); // 150.00

        assertThat(budget.getItems()).hasSize(2);
        assertThat(budget.getItems().get(0).getType()).isEqualTo(BudgetItemType.PART);
        assertThat(budget.getItems().get(1).getType()).isEqualTo(BudgetItemType.SERVICE);
        assertThat(budget.calculateTotal()).isEqualByComparingTo(new BigDecimal("329.80"));
    }

    @Test
    void finalizeFailsWhenEmpty() {
        Budget budget = draft();
        assertThatThrownBy(budget::finalize)
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void finalizeMarksAsFinalized() {
        Budget budget = draft();
        budget.addPart(UUID.randomUUID(), "Pastilha", 1, new BigDecimal("89.90"));
        budget.finalize();
        assertThat(budget.getStatus()).isEqualTo(BudgetStatus.FINALIZED);
    }

    @Test
    void cannotModifyOrRefinalizeAfterFinalized() {
        Budget budget = draft();
        budget.addPart(UUID.randomUUID(), "Pastilha", 1, new BigDecimal("89.90"));
        budget.finalize();

        assertThatThrownBy(() -> budget.addPart(UUID.randomUUID(), "Outra", 1, BigDecimal.TEN))
                .isInstanceOf(ConflictException.class);
        assertThatThrownBy(budget::finalize)
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void itemsListIsUnmodifiable() {
        Budget budget = draft();
        budget.addPart(UUID.randomUUID(), "Pastilha", 1, new BigDecimal("10.00"));
        assertThatThrownBy(() -> budget.getItems().clear())
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
