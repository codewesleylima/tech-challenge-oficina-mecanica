package com.safiap.techchallengeoficinamecanica.modules.inventory.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.inventory.application.commands.RegisterServiceCommand;
import com.safiap.techchallengeoficinamecanica.modules.inventory.application.responses.service.RegisterServiceResponse;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.entities.Service;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.repositories.ServiceRepository;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.ConflictException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterServiceUseCaseTest {

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private RegisterServiceUseCase useCase;

    @Test
    void shouldRegisterServiceAndReturnResponse() {
        RegisterServiceCommand command = new RegisterServiceCommand("Oil Change", "full synthetic", new BigDecimal("120.00"));
        when(serviceRepository.save(any(Service.class))).thenAnswer(invocation -> Optional.of(invocation.getArgument(0)));

        RegisterServiceResponse response = useCase.execute(command);

        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo("Oil Change");
        assertThat(response.description()).isEqualTo("full synthetic");
        assertThat(response.price()).isEqualByComparingTo("120.00");
        ArgumentCaptor<Service> captor = ArgumentCaptor.forClass(Service.class);
        verify(serviceRepository).save(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo("Oil Change");
    }

    @Test
    void shouldThrowConflictWhenSaveReturnsEmpty() {
        RegisterServiceCommand command = new RegisterServiceCommand("Oil Change", "full synthetic", new BigDecimal("120.00"));
        when(serviceRepository.save(any(Service.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(command))
                .isInstanceOf(ConflictException.class);
    }
}
