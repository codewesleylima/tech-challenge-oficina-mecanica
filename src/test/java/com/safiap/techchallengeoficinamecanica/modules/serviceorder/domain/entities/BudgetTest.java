package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.BudgetItemType;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.BudgetStatus;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.ConflictException;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("teste cria o orçamento como DRAFT e sem itens")
    void createsAsDraftWithNoItems() {
        Budget budget = draft();
        assertThat(budget.getStatus()).isEqualTo(BudgetStatus.DRAFT);
        assertThat(budget.getItems()).isEmpty();
        assertThat(budget.calculateTotal()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("teste calcula o total somando peças e serviços")
    void calculatesTotalFromPartsAndServices() {
        Budget budget = draft();
        budget.addPart(UUID.randomUUID(), "Pastilha", 2, new BigDecimal("89.90"));
        budget.addService(UUID.randomUUID(), "Mão de obra", 1, new BigDecimal("150.00"));

        assertThat(budget.getItems()).hasSize(2);
        assertThat(budget.getItems().get(0).getType()).isEqualTo(BudgetItemType.PART);
        assertThat(budget.getItems().get(1).getType()).isEqualTo(BudgetItemType.SERVICE);
        assertThat(budget.calculateTotal()).isEqualByComparingTo(new BigDecimal("329.80"));
    }

    @Test
    @DisplayName("teste falha ao finalizar orçamento sem itens")
    void finalizeFailsWhenEmpty() {
        Budget budget = draft();
        assertThatThrownBy(budget::finalize)
                .isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("teste finaliza o orçamento marcando como FINALIZED")
    void finalizeMarksAsFinalized() {
        Budget budget = draft();
        budget.addPart(UUID.randomUUID(), "Pastilha", 1, new BigDecimal("89.90"));
        budget.finalize();
        assertThat(budget.getStatus()).isEqualTo(BudgetStatus.FINALIZED);
    }

    @Test
    @DisplayName("teste impede alterar ou refinalizar um orçamento já finalizado")
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
    @DisplayName("teste expõe a lista de itens como imutável")
    void itemsListIsUnmodifiable() {
        Budget budget = draft();
        budget.addPart(UUID.randomUUID(), "Pastilha", 1, new BigDecimal("10.00"));
        assertThatThrownBy(() -> budget.getItems().clear())
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("teste conclui um item de serviço marcando a data de conclusão")
    void completesServiceItem() {
        Budget budget = draft();
        budget.addService(UUID.randomUUID(), "Mão de obra", 1, new BigDecimal("150.00"));
        UUID serviceItemId = budget.getItems().get(0).getBudgetItemId();

        budget.completeServiceItem(serviceItemId);

        assertThat(budget.getItems().get(0).getCompletedAt()).isNotNull();
        assertThat(budget.getItems().get(0).isCompleted()).isTrue();
    }

    @Test
    @DisplayName("teste falha ao concluir um item que não é serviço")
    void cannotCompleteNonServiceItem() {
        Budget budget = draft();
        budget.addPart(UUID.randomUUID(), "Pastilha", 1, new BigDecimal("89.90"));
        UUID partItemId = budget.getItems().get(0).getBudgetItemId();

        assertThatThrownBy(() -> budget.completeServiceItem(partItemId))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("teste falha ao concluir um item de serviço já concluído")
    void cannotCompleteServiceItemTwice() {
        Budget budget = draft();
        budget.addService(UUID.randomUUID(), "Mão de obra", 1, new BigDecimal("150.00"));
        UUID serviceItemId = budget.getItems().get(0).getBudgetItemId();
        budget.completeServiceItem(serviceItemId);

        assertThatThrownBy(() -> budget.completeServiceItem(serviceItemId))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("teste falha ao concluir um item inexistente")
    void cannotCompleteUnknownItem() {
        Budget budget = draft();
        budget.addService(UUID.randomUUID(), "Mão de obra", 1, new BigDecimal("150.00"));

        assertThatThrownBy(() -> budget.completeServiceItem(UUID.randomUUID()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("teste só considera todos os serviços concluídos quando cada serviço tem data de conclusão")
    void allServiceItemsCompletedReflectsServiceItems() {
        Budget budget = draft();
        budget.addPart(UUID.randomUUID(), "Pastilha", 1, new BigDecimal("89.90"));
        budget.addService(UUID.randomUUID(), "Mão de obra", 1, new BigDecimal("150.00"));

        assertThat(budget.allServiceItemsCompleted()).isFalse();

        UUID serviceItemId = budget.getItems().stream()
                .filter(i -> i.getType() == BudgetItemType.SERVICE)
                .findFirst().orElseThrow().getBudgetItemId();
        budget.completeServiceItem(serviceItemId);

        assertThat(budget.allServiceItemsCompleted()).isTrue();
    }

    @Test
    @DisplayName("teste considera vacuamente concluído um orçamento só com peças")
    void allServiceItemsCompletedIsTrueWhenNoServices() {
        Budget budget = draft();
        budget.addPart(UUID.randomUUID(), "Pastilha", 1, new BigDecimal("89.90"));

        assertThat(budget.allServiceItemsCompleted()).isTrue();
    }
}
