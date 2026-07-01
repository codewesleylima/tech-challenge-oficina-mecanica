package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.BudgetItemType;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.ConflictException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BudgetItemTest {

    private BudgetItem service() {
        return BudgetItem.create(UUID.randomUUID(), BudgetItemType.SERVICE,
                UUID.randomUUID(), "Troca de óleo", 1, new BigDecimal("120.00"));
    }

    private BudgetItem part() {
        return BudgetItem.create(UUID.randomUUID(), BudgetItemType.PART,
                UUID.randomUUID(), "Filtro", 2, new BigDecimal("50.00"));
    }

    @Test
    @DisplayName("creates an item with a generated id and not completed")
    void createsItemWithGeneratedIdAndNotCompleted() {
        BudgetItem item = service();

        assertThat(item.getBudgetItemId()).isNotNull();
        assertThat(item.getCompletedAt()).isNull();
        assertThat(item.isCompleted()).isFalse();
    }

    @Test
    @DisplayName("rebuilds an item keeping the provided data")
    void buildsItemKeepingProvidedData() {
        UUID budgetItemId = UUID.randomUUID();
        UUID budgetId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        LocalDateTime completedAt = LocalDateTime.now();

        BudgetItem item = BudgetItem.build(budgetItemId, budgetId, BudgetItemType.SERVICE,
                itemId, "Alinhamento", 1, new BigDecimal("80.00"), completedAt);

        assertThat(item.getBudgetItemId()).isEqualTo(budgetItemId);
        assertThat(item.getBudgetId()).isEqualTo(budgetId);
        assertThat(item.getItemId()).isEqualTo(itemId);
        assertThat(item.getDescription()).isEqualTo("Alinhamento");
        assertThat(item.getQuantity()).isEqualTo(1);
        assertThat(item.getUnitPrice()).isEqualByComparingTo("80.00");
        assertThat(item.getCompletedAt()).isEqualTo(completedAt);
    }

    @Test
    @DisplayName("identifies a service item")
    void identifiesServiceItem() {
        assertThat(service().isService()).isTrue();
        assertThat(part().isService()).isFalse();
    }

    @Test
    @DisplayName("completes a service item setting the completion date")
    void completesServiceItem() {
        BudgetItem item = service();

        item.complete();

        assertThat(item.isCompleted()).isTrue();
        assertThat(item.getCompletedAt()).isNotNull();
    }

    @Test
    @DisplayName("fails to complete a non-service item")
    void failsToCompleteNonServiceItem() {
        assertThatThrownBy(() -> part().complete()).isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("fails to complete an already completed service item")
    void failsToCompleteAlreadyCompletedItem() {
        BudgetItem item = service();
        item.complete();

        assertThatThrownBy(item::complete).isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("calculates total price multiplying unit price by quantity")
    void calculatesTotalPrice() {
        BudgetItem item = part();

        assertThat(item.totalPrice()).isEqualByComparingTo("100.00");
    }
}
