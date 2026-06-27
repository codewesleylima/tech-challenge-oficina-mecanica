package com.safiap.techchallengeoficinamecanica.modules.inventory.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.inventory.application.commands.AlterPartCommand;
import com.safiap.techchallengeoficinamecanica.modules.inventory.application.responses.part.AlterPartResponse;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.entities.Part;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.repositories.PartRepository;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.value_objects.Money;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.value_objects.Quantity;
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
class AlterPartUseCaseTest {

    @Mock
    private PartRepository partRepository;
    @InjectMocks
    private AlterPartUseCase useCase;

    private static Part existingPart(UUID id) {
        return Part.buildPart(id, "Old", "old desc", new Money(new BigDecimal("10.00")), new Quantity(5));
    }
    @Test
    void shouldUpdatePartAndReturnResponse() {
        UUID id = UUID.randomUUID();
        when(partRepository.findById(id)).thenReturn(Optional.of(existingPart(id)));
        when(partRepository.save(any(Part.class))).thenAnswer(invocation -> Optional.of(invocation.getArgument(0)));

        AlterPartResponse response = useCase.execute(new AlterPartCommand("New", "new desc", new BigDecimal("20.00")), id);

        assertThat(response.name()).isEqualTo("New");
        assertThat(response.description()).isEqualTo("new desc");
        assertThat(response.price()).isEqualByComparingTo("20.00");
        assertThat(response.quantity()).isEqualTo(5);
    }

    @Test
    void shouldThrowNotFoundWhenPartDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(partRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.execute(new AlterPartCommand("New", "new desc", new BigDecimal("20.00")), id))
                .isInstanceOf(NotFoundException.class);
        verify(partRepository, never()).save(any());
    }

    @Test
    void shouldThrowConflictWhenSaveReturnsEmpty() {
        UUID id = UUID.randomUUID();
        when(partRepository.findById(id)).thenReturn(Optional.of(existingPart(id)));
        when(partRepository.save(any(Part.class))).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.execute(new AlterPartCommand("New", "new desc", new BigDecimal("20.00")), id))
                .isInstanceOf(ConflictException.class);
    }
}
