package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceOrder;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderPriority;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.ConflictException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PullServiceOrderUseCaseTest {

    private final ServiceOrderRepository serviceOrderRepository = mock(ServiceOrderRepository.class);
    private final PullServiceOrderUseCase useCase = new PullServiceOrderUseCase(serviceOrderRepository);

    @Test
    @DisplayName("pulls the next received order from the queue")
    void pullsNextReceivedOrder() {
        UUID serviceOrderId = UUID.randomUUID();
        ServiceOrder order = ServiceOrder.build(serviceOrderId, UUID.randomUUID(), UUID.randomUUID(), "problema",
                null, ServiceOrderStatus.RECEIVED, LocalDateTime.now(), null, null, ServiceOrderPriority.URGENT);
        when(serviceOrderRepository.pullNextOrderService(ServiceOrderStatus.RECEIVED)).thenReturn(Optional.of(order));

        ServiceOrderResponse response = useCase.execute();

        assertThat(response.serviceOrderId()).isEqualTo(serviceOrderId);
    }

    @Test
    @DisplayName("fails to pull when there is no pending order")
    void failsWhenNoPendingOrder() {
        when(serviceOrderRepository.pullNextOrderService(ServiceOrderStatus.RECEIVED)).thenReturn(Optional.empty());

        assertThatThrownBy(useCase::execute).isInstanceOf(ConflictException.class);
    }
}
