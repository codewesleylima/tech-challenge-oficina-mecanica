package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.BudgetItemInput;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.FinalizeDiagnosisCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.ports.InventoryCatalogPort;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.services.BudgetAssemblyService;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.Budget;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceOrder;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.BudgetRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.BudgetItemType;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.BudgetStatus;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderPriority;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import com.safiap.techchallengeoficinamecanica.modules.shared.domain.events.DomainEventPublisher;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.ConflictException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FinalizeDiagnosisUseCaseTest {

    private final ServiceOrderRepository serviceOrderRepository = mock(ServiceOrderRepository.class);
    private final BudgetRepository budgetRepository = mock(BudgetRepository.class);
    private final InventoryCatalogPort inventoryCatalogPort = mock(InventoryCatalogPort.class);
    private final DomainEventPublisher domainEventPublisher = mock(DomainEventPublisher.class);

    private final FinalizeDiagnosisUseCase useCase = new FinalizeDiagnosisUseCase(
            serviceOrderRepository, budgetRepository,
            new BudgetAssemblyService(serviceOrderRepository, budgetRepository, inventoryCatalogPort),
            domainEventPublisher);

    private ServiceOrder orderInDiagnosis(UUID id) {
        return ServiceOrder.build(
                id, UUID.randomUUID(), UUID.randomUUID(), "problema", null,
                ServiceOrderStatus.IN_DIAGNOSIS, LocalDateTime.now(), null, null,
                ServiceOrderPriority.LOW);
    }

    private Budget budgetWithItems(UUID soId) {
        Budget budget = Budget.create(soId);
        budget.addPart(UUID.randomUUID(), "Pastilha", 1, new BigDecimal("89.90"));
        return budget;
    }

    @Test
    @DisplayName("finalizes the diagnosis and the budget when items were already added")
    void finalizesDiagnosisAndBudget() {
        UUID soId = UUID.randomUUID();
        Budget budget = budgetWithItems(soId);
        when(serviceOrderRepository.findById(soId)).thenReturn(Optional.of(orderInDiagnosis(soId)));
        when(budgetRepository.findByServiceOrderId(soId)).thenReturn(Optional.of(budget));

        ServiceOrderResponse response = useCase.execute(
                new FinalizeDiagnosisCommand(soId, "Pastilhas gastas", List.of()));

        assertThat(response.status()).isEqualTo(ServiceOrderStatus.AWAITING_APPROVAL);
        assertThat(response.diagnosis()).isEqualTo("Pastilhas gastas");
        assertThat(budget.getStatus()).isEqualTo(BudgetStatus.FINALIZED);
        verify(budgetRepository, times(1)).save(budget);
        verify(serviceOrderRepository, times(1)).save(any());
        verify(domainEventPublisher, times(1)).publishAll(any());
    }

    @Test
    @DisplayName("accepts the budget items inline, building and finalizing the budget in one call")
    void buildsBudgetFromInlineItems() {
        UUID soId = UUID.randomUUID();
        UUID partId = UUID.randomUUID();
        UUID serviceId = UUID.randomUUID();
        when(serviceOrderRepository.findById(soId)).thenReturn(Optional.of(orderInDiagnosis(soId)));
        when(budgetRepository.findByServiceOrderId(soId)).thenReturn(Optional.empty());
        when(inventoryCatalogPort.getPartPrice(partId)).thenReturn(new BigDecimal("89.90"));
        when(inventoryCatalogPort.getServicePrice(serviceId)).thenReturn(new BigDecimal("150.00"));

        ServiceOrderResponse response = useCase.execute(new FinalizeDiagnosisCommand(soId, "Pastilhas gastas", List.of(
                new BudgetItemInput(BudgetItemType.PART, partId, "Pastilha de freio", 1),
                new BudgetItemInput(BudgetItemType.SERVICE, serviceId, "Mão de obra", 1))));

        assertThat(response.status()).isEqualTo(ServiceOrderStatus.AWAITING_APPROVAL);

        ArgumentCaptor<Budget> savedBudget = ArgumentCaptor.forClass(Budget.class);
        verify(budgetRepository, times(1)).save(savedBudget.capture());
        assertThat(savedBudget.getValue().getStatus()).isEqualTo(BudgetStatus.FINALIZED);
        assertThat(savedBudget.getValue().getItems()).hasSize(2);
        assertThat(savedBudget.getValue().calculateTotal()).isEqualByComparingTo("239.90");
    }

    @Test
    @DisplayName("keeps an already finalized budget untouched")
    void keepsFinalizedBudget() {
        UUID soId = UUID.randomUUID();
        Budget budget = budgetWithItems(soId);
        budget.finalizeBudget();
        when(serviceOrderRepository.findById(soId)).thenReturn(Optional.of(orderInDiagnosis(soId)));
        when(budgetRepository.findByServiceOrderId(soId)).thenReturn(Optional.of(budget));

        ServiceOrderResponse response = useCase.execute(
                new FinalizeDiagnosisCommand(soId, "Pastilhas gastas", null));

        assertThat(response.status()).isEqualTo(ServiceOrderStatus.AWAITING_APPROVAL);
        assertThat(budget.getStatus()).isEqualTo(BudgetStatus.FINALIZED);
    }

    @Test
    @DisplayName("fails to finalize the diagnosis when the budget has no items")
    void failsWhenBudgetHasNoItems() {
        UUID soId = UUID.randomUUID();
        when(serviceOrderRepository.findById(soId)).thenReturn(Optional.of(orderInDiagnosis(soId)));
        when(budgetRepository.findByServiceOrderId(soId)).thenReturn(Optional.of(Budget.create(soId)));

        assertThatThrownBy(() -> useCase.execute(new FinalizeDiagnosisCommand(soId, "x", List.of())))
                .isInstanceOf(ConflictException.class);

        verify(budgetRepository, never()).save(any());
        verify(serviceOrderRepository, never()).save(any());
    }

    @Test
    @DisplayName("fails to finalize the diagnosis when the order is not in diagnosis")
    void failsWhenOrderNotInDiagnosis() {
        UUID soId = UUID.randomUUID();
        ServiceOrder order = ServiceOrder.build(soId, UUID.randomUUID(), UUID.randomUUID(), "problema", null,
                ServiceOrderStatus.RECEIVED, LocalDateTime.now(), null, null, ServiceOrderPriority.LOW);
        when(serviceOrderRepository.findById(soId)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> useCase.execute(new FinalizeDiagnosisCommand(soId, "x", List.of())))
                .isInstanceOf(ConflictException.class);

        verify(serviceOrderRepository, never()).save(any());
    }
}
