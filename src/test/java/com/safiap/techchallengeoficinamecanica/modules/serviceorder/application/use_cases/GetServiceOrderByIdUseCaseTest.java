package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceOrder;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderPriority;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetServiceOrderByIdUseCaseTest {

    private final ServiceOrderRepository serviceOrderRepository = mock(ServiceOrderRepository.class);
    private final GetServiceOrderByIdUseCase useCase = new GetServiceOrderByIdUseCase(serviceOrderRepository);

    @Test
    @DisplayName("returns the service order when it exists")
    void returnsOrderWhenPresent() {
        UUID serviceOrderId = UUID.randomUUID();
        ServiceOrder order = ServiceOrder.build(serviceOrderId, UUID.randomUUID(), UUID.randomUUID(), "problema",
                null, ServiceOrderStatus.RECEIVED, LocalDateTime.now(), null, null, ServiceOrderPriority.LOW);
        when(serviceOrderRepository.findById(serviceOrderId)).thenReturn(Optional.of(order));

        ServiceOrderResponse response = useCase.execute(serviceOrderId);

        assertThat(response.serviceOrderId()).isEqualTo(serviceOrderId);
        assertThat(response.status()).isEqualTo(ServiceOrderStatus.RECEIVED);
    }

    @Test
    @DisplayName("fails to fetch a non-existent service order")
    void failsWhenOrderNotFound() {
        UUID serviceOrderId = UUID.randomUUID();
        when(serviceOrderRepository.findById(serviceOrderId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(serviceOrderId)).isInstanceOf(DomainException.class);
    }
}
