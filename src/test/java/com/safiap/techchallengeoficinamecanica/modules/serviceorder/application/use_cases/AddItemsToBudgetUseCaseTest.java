package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.AddBudgetItemsCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.BudgetItemInput;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.ports.InventoryCatalogPort;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.BudgetResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.services.BudgetAssemblyService;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.Budget;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceOrder;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.BudgetRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.BudgetItemType;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.BudgetStatus;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderPriority;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.ConflictException;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
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

class AddItemsToBudgetUseCaseTest {

    private final ServiceOrderRepository serviceOrderRepository = mock(ServiceOrderRepository.class);
    private final BudgetRepository budgetRepository = mock(BudgetRepository.class);
    private final InventoryCatalogPort inventoryCatalogPort = mock(InventoryCatalogPort.class);
    private final AddItemsToBudgetUseCase useCase = new AddItemsToBudgetUseCase(
            new BudgetAssemblyService(serviceOrderRepository, budgetRepository, inventoryCatalogPort),
            budgetRepository);

    private ServiceOrder order(UUID id, ServiceOrderStatus status) {
        return ServiceOrder.build(id, UUID.randomUUID(), UUID.randomUUID(), "problema", null,
                status, LocalDateTime.now(), null, null, ServiceOrderPriority.LOW);
    }

    @Test
    @DisplayName("adds parts and services in a single call, creating the budget on demand")
    void addsMixedItemsCreatingBudget() {
        UUID serviceOrderId = UUID.randomUUID();
        UUID partId = UUID.randomUUID();
        UUID serviceId = UUID.randomUUID();
        when(serviceOrderRepository.findById(serviceOrderId))
                .thenReturn(Optional.of(order(serviceOrderId, ServiceOrderStatus.IN_DIAGNOSIS)));
        when(budgetRepository.findByServiceOrderId(serviceOrderId)).thenReturn(Optional.empty());
        when(inventoryCatalogPort.getPartPrice(partId)).thenReturn(new BigDecimal("89.90"));
        when(inventoryCatalogPort.getServicePrice(serviceId)).thenReturn(new BigDecimal("150.00"));

        BudgetResponse response = useCase.execute(new AddBudgetItemsCommand(serviceOrderId, List.of(
                new BudgetItemInput(BudgetItemType.PART, partId, "Pastilha de freio", 1),
                new BudgetItemInput(BudgetItemType.SERVICE, serviceId, "Mão de obra", 1))));

        assertThat(response.serviceOrderId()).isEqualTo(serviceOrderId);
        assertThat(response.status()).isEqualTo(BudgetStatus.DRAFT);
        assertThat(response.items()).hasSize(2);
        assertThat(response.totalAmount()).isEqualByComparingTo("239.90");
        verify(budgetRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("appends items to an existing budget")
    void appendsItemsToExistingBudget() {
        UUID serviceOrderId = UUID.randomUUID();
        UUID partId = UUID.randomUUID();
        Budget budget = Budget.create(serviceOrderId);
        budget.addService(UUID.randomUUID(), "Mão de obra", 1, new BigDecimal("150.00"));
        when(serviceOrderRepository.findById(serviceOrderId))
                .thenReturn(Optional.of(order(serviceOrderId, ServiceOrderStatus.IN_DIAGNOSIS)));
        when(budgetRepository.findByServiceOrderId(serviceOrderId)).thenReturn(Optional.of(budget));
        when(inventoryCatalogPort.getPartPrice(partId)).thenReturn(new BigDecimal("89.90"));

        BudgetResponse response = useCase.execute(new AddBudgetItemsCommand(serviceOrderId,
                List.of(new BudgetItemInput(BudgetItemType.PART, partId, "Pastilha", 1))));

        assertThat(response.items()).hasSize(2);
        assertThat(response.totalAmount()).isEqualByComparingTo("239.90");
        verify(budgetRepository, times(1)).save(budget);
    }

    @Test
    @DisplayName("fails when no item is sent")
    void failsWhenNoItems() {
        UUID serviceOrderId = UUID.randomUUID();

        assertThatThrownBy(() -> useCase.execute(new AddBudgetItemsCommand(serviceOrderId, List.of())))
                .isInstanceOf(DomainException.class);

        verify(budgetRepository, never()).save(any());
    }

    @Test
    @DisplayName("fails when an item has no type")
    void failsWhenItemTypeIsMissing() {
        UUID serviceOrderId = UUID.randomUUID();
        when(serviceOrderRepository.findById(serviceOrderId))
                .thenReturn(Optional.of(order(serviceOrderId, ServiceOrderStatus.IN_DIAGNOSIS)));
        when(budgetRepository.findByServiceOrderId(serviceOrderId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(new AddBudgetItemsCommand(serviceOrderId,
                Arrays.asList(new BudgetItemInput(null, UUID.randomUUID(), "Item sem tipo", 1)))))
                .isInstanceOf(DomainException.class);

        verify(budgetRepository, never()).save(any());
    }

    @Test
    @DisplayName("fails when the order is not in diagnosis")
    void failsWhenOrderNotInDiagnosis() {
        UUID serviceOrderId = UUID.randomUUID();
        when(serviceOrderRepository.findById(serviceOrderId))
                .thenReturn(Optional.of(order(serviceOrderId, ServiceOrderStatus.IN_EXECUTION)));

        assertThatThrownBy(() -> useCase.execute(new AddBudgetItemsCommand(serviceOrderId,
                List.of(new BudgetItemInput(BudgetItemType.PART, UUID.randomUUID(), "Pastilha", 1)))))
                .isInstanceOf(ConflictException.class);

        verify(budgetRepository, never()).save(any());
    }

    @Test
    @DisplayName("fails when the service order does not exist")
    void failsWhenServiceOrderNotFound() {
        UUID serviceOrderId = UUID.randomUUID();
        when(serviceOrderRepository.findById(serviceOrderId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(new AddBudgetItemsCommand(serviceOrderId,
                List.of(new BudgetItemInput(BudgetItemType.PART, UUID.randomUUID(), "Pastilha", 1)))))
                .isInstanceOf(NotFoundException.class);

        verify(budgetRepository, never()).save(any());
    }
}
