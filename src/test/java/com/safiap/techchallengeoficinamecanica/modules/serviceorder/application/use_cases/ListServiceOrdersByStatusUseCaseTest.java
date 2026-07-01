package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceOrder;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderPriority;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ListServiceOrdersByStatusUseCaseTest {

    private final ServiceOrderRepository serviceOrderRepository = mock(ServiceOrderRepository.class);
    private final ListServiceOrdersByStatusUseCase useCase =
            new ListServiceOrdersByStatusUseCase(serviceOrderRepository);

    private ServiceOrder order(ServiceOrderStatus status) {
        return ServiceOrder.build(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "problema", null,
                status, LocalDateTime.now(), null, null, ServiceOrderPriority.LOW);
    }

    @Test
    @DisplayName("lists the orders with a given status")
    void listsOrdersByStatus() {
        when(serviceOrderRepository.findByStatus(ServiceOrderStatus.RECEIVED))
                .thenReturn(List.of(order(ServiceOrderStatus.RECEIVED), order(ServiceOrderStatus.RECEIVED)));

        List<ServiceOrderResponse> responses = useCase.execute(ServiceOrderStatus.RECEIVED);

        assertThat(responses).hasSize(2);
        assertThat(responses).allMatch(r -> r.status() == ServiceOrderStatus.RECEIVED);
    }

    @Test
    @DisplayName("returns an empty list when no order has the status")
    void returnsEmptyWhenNoOrders() {
        when(serviceOrderRepository.findByStatus(ServiceOrderStatus.DELIVERED)).thenReturn(List.of());

        assertThat(useCase.execute(ServiceOrderStatus.DELIVERED)).isEmpty();
    }
}
