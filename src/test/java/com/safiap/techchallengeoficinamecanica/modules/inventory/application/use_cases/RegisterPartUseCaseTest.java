package com.safiap.techchallengeoficinamecanica.modules.inventory.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.inventory.application.commands.RegisterPartCommand;
import com.safiap.techchallengeoficinamecanica.modules.inventory.application.responses.part.RegisterPartResponse;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.entities.Part;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.repositories.PartRepository;
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
class RegisterPartUseCaseTest {

    @Mock
    private PartRepository partRepository;

    @InjectMocks
    private RegisterPartUseCase useCase;

    @Test
    void shouldRegisterPartAndReturnResponse() {
        RegisterPartCommand command = new RegisterPartCommand("Oil Filter", "premium", 10, new BigDecimal("25.50"));
        when(partRepository.save(any(Part.class))).thenAnswer(invocation -> Optional.of(invocation.getArgument(0)));

        RegisterPartResponse response = useCase.execute(command);

        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo("Oil Filter");
        assertThat(response.description()).isEqualTo("premium");
        assertThat(response.quantity()).isEqualTo(10);
        assertThat(response.price()).isEqualByComparingTo("25.50");

        ArgumentCaptor<Part> captor = ArgumentCaptor.forClass(Part.class);
        verify(partRepository).save(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo("Oil Filter");
        assertThat(captor.getValue().getQuantity().value()).isEqualTo(10);
    }

    @Test
    void shouldThrowConflictWhenSaveReturnsEmpty() {
        RegisterPartCommand command = new RegisterPartCommand("Oil Filter", "premium", 10, new BigDecimal("25.50"));
        when(partRepository.save(any(Part.class))).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.execute(command))
                .isInstanceOf(ConflictException.class);
    }
}
