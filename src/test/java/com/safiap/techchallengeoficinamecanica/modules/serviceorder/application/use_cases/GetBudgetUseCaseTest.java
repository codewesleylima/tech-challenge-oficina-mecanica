package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.BudgetResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.Budget;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.BudgetRepository;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetBudgetUseCaseTest {

    private final BudgetRepository budgetRepository = mock(BudgetRepository.class);
    private final GetBudgetUseCase useCase = new GetBudgetUseCase(budgetRepository);

    @Test
    @DisplayName("returns the order budget when it exists")
    void returnsBudgetWhenPresent() {
        UUID serviceOrderId = UUID.randomUUID();
        when(budgetRepository.findByServiceOrderId(serviceOrderId))
                .thenReturn(Optional.of(Budget.create(serviceOrderId)));

        BudgetResponse response = useCase.execute(serviceOrderId);

        assertThat(response.serviceOrderId()).isEqualTo(serviceOrderId);
    }

    @Test
    @DisplayName("fails to fetch a non-existent budget")
    void failsWhenBudgetNotFound() {
        UUID serviceOrderId = UUID.randomUUID();
        when(budgetRepository.findByServiceOrderId(serviceOrderId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(serviceOrderId)).isInstanceOf(NotFoundException.class);
    }
}
