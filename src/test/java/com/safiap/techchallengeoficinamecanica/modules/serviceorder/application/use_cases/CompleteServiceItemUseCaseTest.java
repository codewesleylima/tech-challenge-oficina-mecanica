package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.BudgetResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.Budget;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceOrder;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.BudgetRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderPriority;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.ConflictException;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
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

class CompleteServiceItemUseCaseTest {

    private final ServiceOrderRepository serviceOrderRepository = mock(ServiceOrderRepository.class);
    private final BudgetRepository budgetRepository = mock(BudgetRepository.class);
    private final CompleteServiceItemUseCase useCase =
            new CompleteServiceItemUseCase(serviceOrderRepository, budgetRepository);

    private ServiceOrder orderWithStatus(UUID id, ServiceOrderStatus status) {
        return ServiceOrder.build(id, UUID.randomUUID(), UUID.randomUUID(), "problema", null,
                status, LocalDateTime.now(), LocalDateTime.now(), null, ServiceOrderPriority.LOW);
    }

    private Budget budgetWithService(UUID soId) {
        Budget budget = Budget.create(soId);
        budget.addService(UUID.randomUUID(), "Mão de obra", 1, new BigDecimal("150.00"));
        return budget;
    }

    @Test
    @DisplayName("teste conclui o item de serviço quando a OS está em execução")
    void completesServiceItem() {
        UUID soId = UUID.randomUUID();
        Budget budget = budgetWithService(soId);
        UUID serviceItemId = budget.getItems().get(0).getBudgetItemId();
        when(serviceOrderRepository.findById(soId))
                .thenReturn(Optional.of(orderWithStatus(soId, ServiceOrderStatus.IN_EXECUTION)));
        when(budgetRepository.findByServiceOrderId(soId)).thenReturn(Optional.of(budget));

        BudgetResponse response = useCase.execute(soId, serviceItemId);

        assertThat(response.items().get(0).completedAt()).isNotNull();
        verify(budgetRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("teste falha ao concluir item quando a OS não está em execução")
    void failsWhenNotInExecution() {
        UUID soId = UUID.randomUUID();
        when(serviceOrderRepository.findById(soId))
                .thenReturn(Optional.of(orderWithStatus(soId, ServiceOrderStatus.AWAITING_APPROVAL)));

        assertThatThrownBy(() -> useCase.execute(soId, UUID.randomUUID()))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("teste falha ao concluir item quando a OS não existe")
    void failsWhenOrderNotFound() {
        UUID soId = UUID.randomUUID();
        when(serviceOrderRepository.findById(soId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(soId, UUID.randomUUID()))
                .isInstanceOf(NotFoundException.class);
    }
}
