package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.ports.PartStockPort;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StartServiceOrderExecutionUseCaseTest {

    private final ServiceOrderRepository serviceOrderRepository = mock(ServiceOrderRepository.class);
    private final BudgetRepository budgetRepository = mock(BudgetRepository.class);
    private final PartStockPort partStockPort = mock(PartStockPort.class);
    private final DomainEventPublisher domainEventPublisher = mock(DomainEventPublisher.class);
    private final StartServiceOrderExecutionUseCase useCase = new StartServiceOrderExecutionUseCase(
            serviceOrderRepository, budgetRepository, partStockPort, domainEventPublisher);

    private ServiceOrder order(UUID id, ServiceOrderStatus status) {
        return ServiceOrder.build(id, UUID.randomUUID(), UUID.randomUUID(), "problema", null,
                status, LocalDateTime.now(), null, null, ServiceOrderPriority.LOW);
    }

    @Test
    @DisplayName("starts execution and consumes the stock of budget parts")
    void startsExecutionAndConsumesPartStock() {
        UUID serviceOrderId = UUID.randomUUID();
        UUID partId = UUID.randomUUID();
        Budget budget = Budget.create(serviceOrderId);
        budget.addPart(partId, "Filtro", 3, new BigDecimal("40.00"));
        when(serviceOrderRepository.findById(serviceOrderId))
                .thenReturn(Optional.of(order(serviceOrderId, ServiceOrderStatus.AWAITING_APPROVAL)));
        when(budgetRepository.findByServiceOrderId(serviceOrderId)).thenReturn(Optional.of(budget));

        ServiceOrderResponse response = useCase.execute(serviceOrderId);

        assertThat(response.status()).isEqualTo(ServiceOrderStatus.IN_EXECUTION);
        verify(partStockPort, times(1)).decreaseStock(partId, 3);
        verify(serviceOrderRepository, times(1)).save(any());
        verify(domainEventPublisher, times(1)).publishAll(any());
    }

    @Test
    @DisplayName("starts execution without consuming stock when there is no budget")
    void startsExecutionWithoutBudget() {
        UUID serviceOrderId = UUID.randomUUID();
        when(serviceOrderRepository.findById(serviceOrderId))
                .thenReturn(Optional.of(order(serviceOrderId, ServiceOrderStatus.AWAITING_APPROVAL)));
        when(budgetRepository.findByServiceOrderId(serviceOrderId)).thenReturn(Optional.empty());

        ServiceOrderResponse response = useCase.execute(serviceOrderId);

        assertThat(response.status()).isEqualTo(ServiceOrderStatus.IN_EXECUTION);
        verify(partStockPort, never()).decreaseStock(any(), anyInt());
    }

    @Test
    @DisplayName("fails to start execution when the order is not awaiting approval")
    void failsWhenNotAwaitingApproval() {
        UUID serviceOrderId = UUID.randomUUID();
        when(serviceOrderRepository.findById(serviceOrderId))
                .thenReturn(Optional.of(order(serviceOrderId, ServiceOrderStatus.RECEIVED)));

        assertThatThrownBy(() -> useCase.execute(serviceOrderId)).isInstanceOf(ConflictException.class);

        verify(partStockPort, never()).decreaseStock(any(), anyInt());
        verify(serviceOrderRepository, never()).save(any());
    }

    @Test
    @DisplayName("fails to start execution when the order does not exist")
    void failsWhenOrderNotFound() {
        UUID serviceOrderId = UUID.randomUUID();
        when(serviceOrderRepository.findById(serviceOrderId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(serviceOrderId)).isInstanceOf(NotFoundException.class);
    }
}
