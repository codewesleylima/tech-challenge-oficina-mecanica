package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.AverageServiceTimeResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.BudgetRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.presentation.DTO.ServiceDurationDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetAverageServiceTimeUseCaseTest {

    private final BudgetRepository budgetRepository = mock(BudgetRepository.class);
    private final GetAverageServiceTimeUseCase useCase = new GetAverageServiceTimeUseCase(budgetRepository);

    @Test
    @DisplayName("teste agrupa por serviço e calcula a média em minutos com a contagem de amostras")
    void averagesPerService() {
        UUID serviceOrderId = UUID.randomUUID();
        UUID serviceA = UUID.randomUUID();
        UUID serviceB = UUID.randomUUID();
        when(budgetRepository.findServiceDurations(serviceOrderId)).thenReturn(List.of(
                new ServiceDurationDTO(serviceA, 60),
                new ServiceDurationDTO(serviceA, 120),
                new ServiceDurationDTO(serviceB, 300)
        ));

        Map<UUID, AverageServiceTimeResponse> byService = useCase.execute(serviceOrderId).stream()
                .collect(Collectors.toMap(AverageServiceTimeResponse::serviceId, r -> r));

        assertThat(byService.get(serviceA).averageMinutes()).isEqualTo(1.5);
        assertThat(byService.get(serviceA).sampleCount()).isEqualTo(2);
        assertThat(byService.get(serviceB).averageMinutes()).isEqualTo(5.0);
        assertThat(byService.get(serviceB).sampleCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("teste retorna lista vazia quando não há serviços concluídos")
    void emptyWhenNoSamples() {
        when(budgetRepository.findServiceDurations(any())).thenReturn(List.of());
        assertThat(useCase.execute(UUID.randomUUID())).isEmpty();
    }
}
