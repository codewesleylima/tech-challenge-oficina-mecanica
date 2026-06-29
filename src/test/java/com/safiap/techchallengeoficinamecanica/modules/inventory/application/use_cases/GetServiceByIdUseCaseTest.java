package com.safiap.techchallengeoficinamecanica.modules.inventory.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.inventory.application.responses.service.GetServiceResponse;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.entities.Service;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.repositories.ServiceRepository;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.value_objects.Money;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetServiceByIdUseCaseTest {

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private GetServiceByIdUseCase useCase;

    @Test
    void shouldReturnServiceWhenFound() {
        UUID id = UUID.randomUUID();
        Service service = Service.buildService(id, "Oil Change", "desc", new Money(new BigDecimal("120.00")));
        when(serviceRepository.findById(id)).thenReturn(Optional.of(service));

        GetServiceResponse response = useCase.execute(id);
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.name()).isEqualTo("Oil Change");
        assertThat(response.price()).isEqualByComparingTo("120.00");
    }

    @Test
    void shouldThrowNotFoundWhenServiceDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(serviceRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.execute(id))
                .isInstanceOf(NotFoundException.class);
    }
}
