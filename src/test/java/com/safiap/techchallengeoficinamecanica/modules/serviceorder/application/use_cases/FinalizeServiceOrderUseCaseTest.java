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
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FinalizeServiceOrderUseCaseTest {

    private final ServiceOrderRepository serviceOrderRepository = mock(ServiceOrderRepository.class);
    private final BudgetRepository budgetRepository = mock(BudgetRepository.class);
    private final DomainEventPublisher domainEventPublisher = mock(DomainEventPublisher.class);
    private final FinalizeServiceOrderUseCase useCase =
            new FinalizeServiceOrderUseCase(serviceOrderRepository, budgetRepository, domainEventPublisher);

    private ServiceOrder order(UUID id, ServiceOrderStatus status) {
        return ServiceOrder.build(id, UUID.randomUUID(), UUID.randomUUID(), "problema", null,
                status, LocalDateTime.now(), LocalDateTime.now(), null, ServiceOrderPriority.LOW);
    }

    @Test
    @DisplayName("finalizes an in-execution order when there is no budget")
    void finalizesWhenNoBudget() {
        UUID serviceOrderId = UUID.randomUUID();
        when(serviceOrderRepository.findById(serviceOrderId))
                .thenReturn(Optional.of(order(serviceOrderId, ServiceOrderStatus.IN_EXECUTION)));
        when(budgetRepository.findByServiceOrderId(serviceOrderId)).thenReturn(Optional.empty());

        ServiceOrderResponse response = useCase.execute(serviceOrderId);

        assertThat(response.status()).isEqualTo(ServiceOrderStatus.FINALIZED);
        verify(serviceOrderRepository, times(1)).save(any());
        verify(domainEventPublisher, times(1)).publishAll(any());
    }

    @Test
    @DisplayName("finalizes the order when all budget service items are completed")
    void finalizesWhenAllServiceItemsCompleted() {
        UUID serviceOrderId = UUID.randomUUID();
        Budget budget = Budget.create(serviceOrderId);
        budget.addService(UUID.randomUUID(), "Mão de obra", 1, new BigDecimal("150.00"));
        budget.completeServiceItem(budget.getItems().get(0).getBudgetItemId());
        when(serviceOrderRepository.findById(serviceOrderId))
                .thenReturn(Optional.of(order(serviceOrderId, ServiceOrderStatus.IN_EXECUTION)));
        when(budgetRepository.findByServiceOrderId(serviceOrderId)).thenReturn(Optional.of(budget));

        ServiceOrderResponse response = useCase.execute(serviceOrderId);

        assertThat(response.status()).isEqualTo(ServiceOrderStatus.FINALIZED);
    }

    @Test
    @DisplayName("fails to finalize when there are uncompleted budget service items")
    void failsWhenServiceItemsNotCompleted() {
        UUID serviceOrderId = UUID.randomUUID();
        Budget budget = Budget.create(serviceOrderId);
        budget.addService(UUID.randomUUID(), "Mão de obra", 1, new BigDecimal("150.00"));
        when(serviceOrderRepository.findById(serviceOrderId))
                .thenReturn(Optional.of(order(serviceOrderId, ServiceOrderStatus.IN_EXECUTION)));
        when(budgetRepository.findByServiceOrderId(serviceOrderId)).thenReturn(Optional.of(budget));

        assertThatThrownBy(() -> useCase.execute(serviceOrderId)).isInstanceOf(ConflictException.class);

        verify(serviceOrderRepository, never()).save(any());
    }

    @Test
    @DisplayName("fails to finalize when the order does not exist")
    void failsWhenOrderNotFound() {
        UUID serviceOrderId = UUID.randomUUID();
        when(serviceOrderRepository.findById(serviceOrderId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(serviceOrderId)).isInstanceOf(NotFoundException.class);
    }
}
