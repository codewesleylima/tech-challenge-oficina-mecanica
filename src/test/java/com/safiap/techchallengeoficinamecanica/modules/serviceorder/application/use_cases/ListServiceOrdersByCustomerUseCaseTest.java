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

class ListServiceOrdersByCustomerUseCaseTest {

    private final ServiceOrderRepository serviceOrderRepository = mock(ServiceOrderRepository.class);
    private final ListServiceOrdersByCustomerUseCase useCase =
            new ListServiceOrdersByCustomerUseCase(serviceOrderRepository);

    private ServiceOrder order(UUID customerId) {
        return ServiceOrder.build(UUID.randomUUID(), customerId, UUID.randomUUID(), "problema", null,
                ServiceOrderStatus.RECEIVED, LocalDateTime.now(), null, null, ServiceOrderPriority.LOW);
    }

    @Test
    @DisplayName("lists the orders of a customer")
    void listsOrdersByCustomer() {
        UUID customerId = UUID.randomUUID();
        when(serviceOrderRepository.findByCustomerId(customerId))
                .thenReturn(List.of(order(customerId), order(customerId)));

        List<ServiceOrderResponse> responses = useCase.execute(customerId);

        assertThat(responses).hasSize(2);
        assertThat(responses).allMatch(r -> r.customerId().equals(customerId));
    }

    @Test
    @DisplayName("returns an empty list when the customer has no orders")
    void returnsEmptyWhenNoOrders() {
        UUID customerId = UUID.randomUUID();
        when(serviceOrderRepository.findByCustomerId(customerId)).thenReturn(List.of());

        assertThat(useCase.execute(customerId)).isEmpty();
    }
}
