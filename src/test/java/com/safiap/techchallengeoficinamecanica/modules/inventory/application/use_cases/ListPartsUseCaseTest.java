package com.safiap.techchallengeoficinamecanica.modules.inventory.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.inventory.application.responses.part.GetPartResponse;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.entities.Part;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.repositories.PartRepository;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.value_objects.Money;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.value_objects.Quantity;
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
class ListPartsUseCaseTest {

    @Mock
    private PartRepository partRepository;

    @InjectMocks
    private ListPartsUseCase useCase;

    @Test
    void shouldReturnMappedParts() {
        Part first = Part.createPart("Bolt", "desc", new Money(new BigDecimal("10.00")), new Quantity(5));
        Part second = Part.createPart("Nut", "desc", new Money(new BigDecimal("20.00")), new Quantity(8));
        when(partRepository.findAll()).thenReturn(List.of(first, second));
        List<GetPartResponse> responses = useCase.execute();
        assertThat(responses)
                .hasSize(2)
                .extracting(GetPartResponse::name)
                .containsExactly("Bolt", "Nut");
    }

}
