package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.AddPartCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.ports.InventoryCatalogPort;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.BudgetResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.services.BudgetAssemblyService;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AddPartToBudgetUseCaseTest {

    private final ServiceOrderRepository serviceOrderRepository = mock(ServiceOrderRepository.class);
    private final BudgetRepository budgetRepository = mock(BudgetRepository.class);
    private final InventoryCatalogPort inventoryCatalogPort = mock(InventoryCatalogPort.class);
    private final AddPartToBudgetUseCase useCase = new AddPartToBudgetUseCase(
            new AddItemsToBudgetUseCase(
                    new BudgetAssemblyService(serviceOrderRepository, budgetRepository, inventoryCatalogPort),
                    budgetRepository));

    private ServiceOrder order(UUID id, ServiceOrderStatus status) {
        return ServiceOrder.build(id, UUID.randomUUID(), UUID.randomUUID(), "problema", null,
                status, LocalDateTime.now(), null, null, ServiceOrderPriority.LOW);
    }

    @Test
    @DisplayName("adds a part to the budget using the catalog price")
    void addsPartUsingCatalogPrice() {
        UUID serviceOrderId = UUID.randomUUID();
        UUID partId = UUID.randomUUID();
        Budget budget = Budget.create(serviceOrderId);
        when(serviceOrderRepository.findById(serviceOrderId))
                .thenReturn(Optional.of(order(serviceOrderId, ServiceOrderStatus.IN_DIAGNOSIS)));
        when(budgetRepository.findByServiceOrderId(serviceOrderId)).thenReturn(Optional.of(budget));
        when(inventoryCatalogPort.getPartPrice(partId)).thenReturn(new BigDecimal("99.90"));

        BudgetResponse response = useCase.execute(new AddPartCommand(serviceOrderId, partId, "Filtro de óleo", 2));

        assertThat(response.items()).hasSize(1);
        assertThat(response.items().get(0).type()).isEqualTo("PART");
        assertThat(response.items().get(0).unitPrice()).isEqualByComparingTo("99.90");
        assertThat(response.totalAmount()).isEqualByComparingTo("199.80");
        verify(budgetRepository, times(1)).save(budget);
    }

    @Test
    @DisplayName("creates the budget on demand when none exists yet")
    void createsBudgetOnDemand() {
        UUID serviceOrderId = UUID.randomUUID();
        UUID partId = UUID.randomUUID();
        when(serviceOrderRepository.findById(serviceOrderId))
                .thenReturn(Optional.of(order(serviceOrderId, ServiceOrderStatus.IN_DIAGNOSIS)));
        when(budgetRepository.findByServiceOrderId(serviceOrderId)).thenReturn(Optional.empty());
        when(inventoryCatalogPort.getPartPrice(partId)).thenReturn(new BigDecimal("89.90"));

        BudgetResponse response = useCase.execute(new AddPartCommand(serviceOrderId, partId, "Pastilha", 1));

        assertThat(response.serviceOrderId()).isEqualTo(serviceOrderId);
        assertThat(response.items()).hasSize(1);
        verify(budgetRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("fails to add a part when the order is not in diagnosis")
    void failsWhenOrderNotInDiagnosis() {
        UUID serviceOrderId = UUID.randomUUID();
        when(serviceOrderRepository.findById(serviceOrderId))
                .thenReturn(Optional.of(order(serviceOrderId, ServiceOrderStatus.RECEIVED)));

        assertThatThrownBy(() -> useCase.execute(new AddPartCommand(serviceOrderId, UUID.randomUUID(), "Filtro", 1)))
                .isInstanceOf(ConflictException.class);

        verify(budgetRepository, never()).save(any());
    }

    @Test
    @DisplayName("fails to add a part when the service order does not exist")
    void failsWhenServiceOrderNotFound() {
        UUID serviceOrderId = UUID.randomUUID();
        when(serviceOrderRepository.findById(serviceOrderId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(new AddPartCommand(serviceOrderId, UUID.randomUUID(), "Filtro", 1)))
                .isInstanceOf(NotFoundException.class);

        verify(budgetRepository, never()).save(any());
    }
}
