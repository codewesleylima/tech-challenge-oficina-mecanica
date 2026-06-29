package com.safiap.techchallengeoficinamecanica.modules.inventory.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.inventory.application.responses.part.GetPartResponse;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetPartByIdUseCaseTest {

    @Mock
    private PartRepository partRepository;

    @InjectMocks
    private GetPartByIdUseCase useCase;

    @Test
    void shouldReturnPartWhenFound() {
        UUID id = UUID.randomUUID();
        Part part = Part.buildPart(id, "Bolt", "desc", new Money(new BigDecimal("10.00")), new Quantity(5));
        when(partRepository.findById(id)).thenReturn(Optional.of(part));

        GetPartResponse response = useCase.execute(id);
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.name()).isEqualTo("Bolt");
        assertThat(response.quantity()).isEqualTo(5);
        assertThat(response.price()).isEqualByComparingTo("10.00");
    }

    @Test
    void shouldThrowNotFoundWhenPartDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(partRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.execute(id))
                .isInstanceOf(NotFoundException.class);
    }
}
