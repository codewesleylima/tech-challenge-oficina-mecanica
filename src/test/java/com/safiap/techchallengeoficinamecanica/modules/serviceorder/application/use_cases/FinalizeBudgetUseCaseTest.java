package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.BudgetResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.Budget;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.BudgetRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.BudgetStatus;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.ConflictException;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FinalizeBudgetUseCaseTest {

    private final BudgetRepository budgetRepository = mock(BudgetRepository.class);
    private final FinalizeBudgetUseCase useCase = new FinalizeBudgetUseCase(budgetRepository);

    private Budget budgetWithItem(UUID serviceOrderId) {
        Budget budget = Budget.create(serviceOrderId);
        budget.addService(UUID.randomUUID(), "Mão de obra", 1, new BigDecimal("150.00"));
        return budget;
    }

    @Test
    @DisplayName("finalizes a budget with items")
    void finalizesBudgetWithItems() {
        UUID serviceOrderId = UUID.randomUUID();
        when(budgetRepository.findByServiceOrderId(serviceOrderId))
                .thenReturn(Optional.of(budgetWithItem(serviceOrderId)));

        BudgetResponse response = useCase.execute(serviceOrderId);

        assertThat(response.status()).isEqualTo(BudgetStatus.FINALIZED);
        verify(budgetRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("fails to finalize a budget with no items")
    void failsWhenBudgetHasNoItems() {
        UUID serviceOrderId = UUID.randomUUID();
        when(budgetRepository.findByServiceOrderId(serviceOrderId))
                .thenReturn(Optional.of(Budget.create(serviceOrderId)));

        assertThatThrownBy(() -> useCase.execute(serviceOrderId)).isInstanceOf(ConflictException.class);

        verify(budgetRepository, never()).save(any());
    }

    @Test
    @DisplayName("fails to finalize a budget that does not exist")
    void failsWhenBudgetNotFound() {
        UUID serviceOrderId = UUID.randomUUID();
        when(budgetRepository.findByServiceOrderId(serviceOrderId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(serviceOrderId)).isInstanceOf(NotFoundException.class);
    }
}
