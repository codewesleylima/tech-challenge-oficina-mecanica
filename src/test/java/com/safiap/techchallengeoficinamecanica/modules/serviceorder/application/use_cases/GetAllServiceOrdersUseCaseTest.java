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

class GetAllServiceOrdersUseCaseTest {

    private final ServiceOrderRepository serviceOrderRepository = mock(ServiceOrderRepository.class);
    private final GetAllServiceOrdersUseCase useCase = new GetAllServiceOrdersUseCase(serviceOrderRepository);

    private ServiceOrder order(ServiceOrderStatus status) {
        return ServiceOrder.build(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "problema", null,
                status, LocalDateTime.now(), null, null, ServiceOrderPriority.LOW);
    }

    @Test
    @DisplayName("maps every order returned by the repository, preserving its order")
    void mapsOrdersPreservingOrder() {
        when(serviceOrderRepository.getAllServiceOrdersFiltered()).thenReturn(List.of(
                order(ServiceOrderStatus.IN_EXECUTION),
                order(ServiceOrderStatus.AWAITING_APPROVAL),
                order(ServiceOrderStatus.RECEIVED)));

        List<ServiceOrderResponse> responses = useCase.execute();

        assertThat(responses).extracting(ServiceOrderResponse::status).containsExactly(
                ServiceOrderStatus.IN_EXECUTION,
                ServiceOrderStatus.AWAITING_APPROVAL,
                ServiceOrderStatus.RECEIVED);
    }

    @Test
    @DisplayName("returns an empty list when there is no open order")
    void returnsEmptyList() {
        when(serviceOrderRepository.getAllServiceOrdersFiltered()).thenReturn(List.of());

        assertThat(useCase.execute()).isEmpty();
    }
}
