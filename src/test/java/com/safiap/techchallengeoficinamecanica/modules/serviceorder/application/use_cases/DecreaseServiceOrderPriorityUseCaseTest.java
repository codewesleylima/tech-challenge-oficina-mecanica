package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceOrder;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderPriority;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

class DecreaseServiceOrderPriorityUseCaseTest {

    private final ServiceOrderRepository serviceOrderRepository = mock(ServiceOrderRepository.class);
    private final DecreaseServiceOrderPriorityUseCase useCase =
            new DecreaseServiceOrderPriorityUseCase(serviceOrderRepository);

    private ServiceOrder order(UUID id, ServiceOrderPriority priority) {
        return ServiceOrder.build(id, UUID.randomUUID(), UUID.randomUUID(), "problema", null,
                ServiceOrderStatus.RECEIVED, LocalDateTime.now(), null, null, priority);
    }

    @Test
    @DisplayName("decreases the order priority")
    void decreasesPriority() {
        UUID serviceOrderId = UUID.randomUUID();
        when(serviceOrderRepository.findById(serviceOrderId))
                .thenReturn(Optional.of(order(serviceOrderId, ServiceOrderPriority.HIGH)));

        ServiceOrderResponse response = useCase.execute(serviceOrderId);

        assertThat(response.priority()).isEqualTo(ServiceOrderPriority.NORMAL.name());
        verify(serviceOrderRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("keeps LOW priority as the floor")
    void keepsLowAsFloor() {
        UUID serviceOrderId = UUID.randomUUID();
        when(serviceOrderRepository.findById(serviceOrderId))
                .thenReturn(Optional.of(order(serviceOrderId, ServiceOrderPriority.LOW)));

        ServiceOrderResponse response = useCase.execute(serviceOrderId);

        assertThat(response.priority()).isEqualTo(ServiceOrderPriority.LOW.name());
    }

    @Test
    @DisplayName("fails to decrease priority when the order does not exist")
    void failsWhenOrderNotFound() {
        UUID serviceOrderId = UUID.randomUUID();
        when(serviceOrderRepository.findById(serviceOrderId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(serviceOrderId)).isInstanceOf(NotFoundException.class);
    }
}
