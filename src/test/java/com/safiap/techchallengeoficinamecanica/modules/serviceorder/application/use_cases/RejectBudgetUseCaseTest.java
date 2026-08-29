package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceOrder;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderPriority;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import com.safiap.techchallengeoficinamecanica.modules.shared.domain.events.DomainEventPublisher;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.ConflictException;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

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

class RejectBudgetUseCaseTest {

    private final ServiceOrderRepository serviceOrderRepository = mock(ServiceOrderRepository.class);
    private final DomainEventPublisher domainEventPublisher = mock(DomainEventPublisher.class);
    private final RejectBudgetUseCase useCase =
            new RejectBudgetUseCase(serviceOrderRepository, domainEventPublisher);

    private ServiceOrder order(UUID id, UUID customerId, ServiceOrderStatus status) {
        return ServiceOrder.build(id, customerId, UUID.randomUUID(), "problema", null,
                status, LocalDateTime.now(), null, null, ServiceOrderPriority.LOW);
    }

    @Test
    @DisplayName("rejects the budget canceling the service order")
    void rejectsBudgetCancelingOrder() {
        UUID serviceOrderId = UUID.randomUUID();
        when(serviceOrderRepository.findById(serviceOrderId))
                .thenReturn(Optional.of(order(serviceOrderId, UUID.randomUUID(), ServiceOrderStatus.AWAITING_APPROVAL)));

        ServiceOrderResponse response = useCase.execute(serviceOrderId);

        assertThat(response.status()).isEqualTo(ServiceOrderStatus.CANCELED);
        verify(serviceOrderRepository, times(1)).save(any());
        verify(domainEventPublisher, times(1)).publishAll(any());
    }

    @Test
    @DisplayName("lets the customer reject the budget of their own service order")
    void customerRejectsOwnOrder() {
        UUID serviceOrderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        when(serviceOrderRepository.findById(serviceOrderId))
                .thenReturn(Optional.of(order(serviceOrderId, customerId, ServiceOrderStatus.AWAITING_APPROVAL)));

        ServiceOrderResponse response = useCase.executeAsCustomer(serviceOrderId, customerId);

        assertThat(response.status()).isEqualTo(ServiceOrderStatus.CANCELED);
        verify(serviceOrderRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("blocks a customer from rejecting the budget of someone else's service order")
    void customerCannotRejectOrderOfAnotherCustomer() {
        UUID serviceOrderId = UUID.randomUUID();
        when(serviceOrderRepository.findById(serviceOrderId))
                .thenReturn(Optional.of(order(serviceOrderId, UUID.randomUUID(), ServiceOrderStatus.AWAITING_APPROVAL)));

        assertThatThrownBy(() -> useCase.executeAsCustomer(serviceOrderId, UUID.randomUUID()))
                .isInstanceOf(AccessDeniedException.class);

        verify(serviceOrderRepository, never()).save(any());
        verify(domainEventPublisher, never()).publishAll(any());
    }

    @Test
    @DisplayName("fails to reject the budget when the order is not awaiting approval")
    void failsWhenNotAwaitingApproval() {
        UUID serviceOrderId = UUID.randomUUID();
        when(serviceOrderRepository.findById(serviceOrderId))
                .thenReturn(Optional.of(order(serviceOrderId, UUID.randomUUID(), ServiceOrderStatus.RECEIVED)));

        assertThatThrownBy(() -> useCase.execute(serviceOrderId)).isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("fails to reject the budget when the order does not exist")
    void failsWhenOrderNotFound() {
        UUID serviceOrderId = UUID.randomUUID();
        when(serviceOrderRepository.findById(serviceOrderId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(serviceOrderId)).isInstanceOf(NotFoundException.class);
    }
}
