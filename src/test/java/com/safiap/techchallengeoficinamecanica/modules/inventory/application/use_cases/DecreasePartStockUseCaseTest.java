package com.safiap.techchallengeoficinamecanica.modules.inventory.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.inventory.application.responses.part.AlterPartResponse;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.entities.Part;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.repositories.PartRepository;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.value_objects.Money;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.value_objects.Quantity;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
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
class DecreasePartStockUseCaseTest {

    @Mock
    private PartRepository partRepository;

    @InjectMocks
    private DecreasePartStockUseCase useCase;

    private static Part partWithStock(UUID id, int stock) {
        return Part.buildPart(id, "Bolt", "desc", new Money(new BigDecimal("10.00")), new Quantity(stock));
    }

    @Test
    void shouldDecreaseStock() {
        UUID id = UUID.randomUUID();
        when(partRepository.findById(id)).thenReturn(Optional.of(partWithStock(id, 5)));
        when(partRepository.save(any(Part.class))).thenAnswer(invocation -> Optional.of(invocation.getArgument(0)));

        AlterPartResponse response = useCase.execute(id, 2);
        assertThat(response.quantity()).isEqualTo(3);
    }

    @Test
    void shouldThrowNotFoundWhenPartDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(partRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(id, 2))
                .isInstanceOf(NotFoundException.class);

        verify(partRepository, never()).save(any());
    }

    @Test
    void shouldNotSaveWhenStockIsInsufficient() {
        UUID id = UUID.randomUUID();
        when(partRepository.findById(id)).thenReturn(Optional.of(partWithStock(id, 2)));

        assertThatThrownBy(() -> useCase.execute(id, 10))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("insufficient");

        verify(partRepository, never()).save(any());
    }
}
