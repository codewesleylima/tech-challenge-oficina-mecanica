package com.safiap.techchallengeoficinamecanica.modules.inventory.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.inventory.application.responses.service.GetServiceResponse;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.entities.Service;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.repositories.ServiceRepository;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.value_objects.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListServicesUseCaseTest {

    @Mock
    private ServiceRepository serviceRepository;
    @InjectMocks
    private ListServicesUseCase useCase;

    @Test
    void shouldReturnMappedServices() {
        Service first = Service.createService("Oil Change", "desc", new Money(new BigDecimal("120.00")));
        Service second = Service.createService("Alignment", "desc", new Money(new BigDecimal("80.00")));
        when(serviceRepository.findAll()).thenReturn(List.of(first, second));
        List<GetServiceResponse> responses = useCase.execute();

        assertThat(responses)
                .hasSize(2)
                .extracting(GetServiceResponse::name)
                .containsExactly("Oil Change", "Alignment");
    }

    @Test
    void shouldReturnEmptyListWhenNoServices() {
        when(serviceRepository.findAll()).thenReturn(List.of());
        assertThat(useCase.execute()).isEmpty();
    }
}
