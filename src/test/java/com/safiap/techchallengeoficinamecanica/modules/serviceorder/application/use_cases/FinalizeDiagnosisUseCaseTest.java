package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.Budget;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceOrder;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.BudgetRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderPriority;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import com.safiap.techchallengeoficinamecanica.modules.shared.domain.events.DomainEventPublisher;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.ConflictException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FinalizeDiagnosisUseCaseTest {

    private final ServiceOrderRepository serviceOrderRepository = mock(ServiceOrderRepository.class);
    private final BudgetRepository budgetRepository = mock(BudgetRepository.class);
    private final DomainEventPublisher domainEventPublisher = mock(DomainEventPublisher.class);

    private final FinalizeDiagnosisUseCase useCase =
            new FinalizeDiagnosisUseCase(serviceOrderRepository, budgetRepository, domainEventPublisher);

    private ServiceOrder orderInDiagnosis(UUID id) {
        return ServiceOrder.build(
                id, UUID.randomUUID(), UUID.randomUUID(), "problema", null,
                ServiceOrderStatus.IN_DIAGNOSIS, LocalDateTime.now(), null, null,
                ServiceOrderPriority.LOW);
    }

    private Budget finalizedBudget(UUID soId) {
        Budget budget = Budget.create(soId);
        budget.addPart(UUID.randomUUID(), "Pastilha", 1, new BigDecimal("89.90"));
        budget.finalize();
        return budget;
    }

    @Test
    @DisplayName("finalizes the diagnosis when the budget is finalized with items")
    void finalizesDiagnosisWhenBudgetReady() {
        UUID soId = UUID.randomUUID();
        when(serviceOrderRepository.findById(soId)).thenReturn(Optional.of(orderInDiagnosis(soId)));
        when(budgetRepository.findByServiceOrderId(soId)).thenReturn(Optional.of(finalizedBudget(soId)));

        ServiceOrderResponse response = useCase.execute(soId, "Pastilhas gastas");

        assertThat(response.status()).isEqualTo(ServiceOrderStatus.AWAITING_APPROVAL);
        assertThat(response.diagnosis()).isEqualTo("Pastilhas gastas");
        verify(serviceOrderRepository, times(1)).save(any());
        verify(domainEventPublisher, times(1)).publishAll(any());
    }

    @Test
    @DisplayName("fails to finalize the diagnosis when the budget is not finalized")
    void failsWhenBudgetNotFinalized() {
        UUID soId = UUID.randomUUID();
        when(serviceOrderRepository.findById(soId)).thenReturn(Optional.of(orderInDiagnosis(soId)));
        when(budgetRepository.findByServiceOrderId(soId)).thenReturn(Optional.of(Budget.create(soId)));

        assertThatThrownBy(() -> useCase.execute(soId, "x"))
                .isInstanceOf(ConflictException.class);
    }
}
