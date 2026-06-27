package com.safiap.techchallengeoficinamecanica.modules.inventory.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.inventory.application.commands.AlterServiceCommand;
import com.safiap.techchallengeoficinamecanica.modules.inventory.application.responses.service.AlterServiceResponse;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.entities.Service;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.repositories.ServiceRepository;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.value_objects.Money;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.ConflictException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlterServiceUseCaseTest {

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private AlterServiceUseCase useCase;

    private static Service existingService(UUID id) {
        return Service.buildService(id, "Old", "old desc", new Money(new BigDecimal("100.00")));
    }

    @Test
    void shouldUpdateServiceAndReturnResponse() {
        UUID id = UUID.randomUUID();
        when(serviceRepository.findById(id)).thenReturn(Optional.of(existingService(id)));
        when(serviceRepository.save(any(Service.class))).thenAnswer(invocation -> Optional.of(invocation.getArgument(0)));

        AlterServiceResponse response = useCase.execute(new AlterServiceCommand("New", "new desc", new BigDecimal("150.00")), id);
        assertThat(response.name()).isEqualTo("New");
        assertThat(response.description()).isEqualTo("new desc");
        assertThat(response.price()).isEqualByComparingTo("150.00");
    }

    @Test
    void shouldThrowNotFoundWhenServiceDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(serviceRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(new AlterServiceCommand("New", "new desc", new BigDecimal("150.00")), id))
                .isInstanceOf(NotFoundException.class);

        verify(serviceRepository, never()).save(any());
    }

    @Test
    void shouldThrowConflictWhenSaveReturnsEmpty() {
        UUID id = UUID.randomUUID();
        when(serviceRepository.findById(id)).thenReturn(Optional.of(existingService(id)));
        when(serviceRepository.save(any(Service.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(new AlterServiceCommand("New", "new desc", new BigDecimal("150.00")), id))
                .isInstanceOf(ConflictException.class);
    }
}
