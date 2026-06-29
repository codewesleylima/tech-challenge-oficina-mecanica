package com.safiap.techchallengeoficinamecanica.modules.inventory.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.entities.Part;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.repositories.PartRepository;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.value_objects.Money;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.value_objects.Quantity;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeletePartUseCaseTest {

    @Mock
    private PartRepository partRepository;

    @InjectMocks
    private DeletePartUseCase useCase;

    @Test
    void shouldDeleteWhenPartExists() {
        UUID id = UUID.randomUUID();
        Part part = Part.buildPart(id, "Bolt", "desc", new Money(new BigDecimal("10.00")), new Quantity(5));
        when(partRepository.findById(id)).thenReturn(Optional.of(part));
        useCase.execute(id);
        verify(partRepository).delete(id);
    }

    @Test
    void shouldThrowNotFoundAndNotDeleteWhenPartDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(partRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(id))
                .isInstanceOf(NotFoundException.class);
        verify(partRepository, never()).delete(any());
    }
}
