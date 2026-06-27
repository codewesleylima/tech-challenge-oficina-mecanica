package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.Budget;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceOrder;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.BudgetRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderPriority;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.ConflictException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FinalizeDiagnosisUseCaseTest {

    private final ServiceOrderRepository serviceOrderRepository = mock(ServiceOrderRepository.class);
    private final BudgetRepository budgetRepository = mock(BudgetRepository.class);

    private final FinalizeDiagnosisUseCase useCase =
            new FinalizeDiagnosisUseCase(serviceOrderRepository, budgetRepository);

    private ServiceOrder orderInDiagnosis(UUID id) {
        return ServiceOrder.build(
                id, UUID.randomUUID(), UUID.randomUUID(), "problema", null,
                ServiceOrderStatus.IN_DIAGNOSIS, LocalDateTime.now(), null,
                ServiceOrderPriority.LOW);
    }

    private Budget finalizedBudget(UUID soId) {
        Budget budget = Budget.create(soId);
        budget.addPart(UUID.randomUUID(), "Pastilha", 1, new BigDecimal("89.90"));
        budget.finalize();
        return budget;
    }

    @Test
    @DisplayName("teste finaliza o diagnóstico quando o orçamento está finalizado com itens")
    void finalizesDiagnosisWhenBudgetReady() {
        UUID soId = UUID.randomUUID();
        when(serviceOrderRepository.findById(soId)).thenReturn(Optional.of(orderInDiagnosis(soId)));
        when(budgetRepository.findByServiceOrderId(soId)).thenReturn(Optional.of(finalizedBudget(soId)));

        ServiceOrderResponse response = useCase.execute(soId, "Pastilhas gastas");

        assertThat(response.status()).isEqualTo(ServiceOrderStatus.AWAITING_APPROVAL);
        assertThat(response.diagnosis()).isEqualTo("Pastilhas gastas");
        verify(serviceOrderRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("teste falha ao finalizar o diagnóstico quando o orçamento não está finalizado")
    void failsWhenBudgetNotFinalized() {
        UUID soId = UUID.randomUUID();
        when(serviceOrderRepository.findById(soId)).thenReturn(Optional.of(orderInDiagnosis(soId)));
        when(budgetRepository.findByServiceOrderId(soId)).thenReturn(Optional.of(Budget.create(soId)));

        assertThatThrownBy(() -> useCase.execute(soId, "x"))
                .isInstanceOf(ConflictException.class);
    }
}
