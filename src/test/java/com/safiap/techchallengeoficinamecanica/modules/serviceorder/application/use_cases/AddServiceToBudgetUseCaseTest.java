package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.AddServiceCommand;
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

class AddServiceToBudgetUseCaseTest {

    private final ServiceOrderRepository serviceOrderRepository = mock(ServiceOrderRepository.class);
    private final BudgetRepository budgetRepository = mock(BudgetRepository.class);
    private final InventoryCatalogPort inventoryCatalogPort = mock(InventoryCatalogPort.class);
    private final AddServiceToBudgetUseCase useCase = new AddServiceToBudgetUseCase(
            new AddItemsToBudgetUseCase(
                    new BudgetAssemblyService(serviceOrderRepository, budgetRepository, inventoryCatalogPort),
                    budgetRepository));

    private ServiceOrder order(UUID id, ServiceOrderStatus status) {
        return ServiceOrder.build(id, UUID.randomUUID(), UUID.randomUUID(), "problema", null,
                status, LocalDateTime.now(), null, null, ServiceOrderPriority.LOW);
    }

    @Test
    @DisplayName("adds a service to the budget using the catalog price")
    void addsServiceUsingCatalogPrice() {
        UUID serviceOrderId = UUID.randomUUID();
        UUID serviceId = UUID.randomUUID();
        Budget budget = Budget.create(serviceOrderId);
        when(serviceOrderRepository.findById(serviceOrderId))
                .thenReturn(Optional.of(order(serviceOrderId, ServiceOrderStatus.IN_DIAGNOSIS)));
        when(budgetRepository.findByServiceOrderId(serviceOrderId)).thenReturn(Optional.of(budget));
        when(inventoryCatalogPort.getServicePrice(serviceId)).thenReturn(new BigDecimal("150.00"));

        BudgetResponse response = useCase.execute(new AddServiceCommand(serviceOrderId, serviceId, "Mão de obra", 1));

        assertThat(response.items()).hasSize(1);
        assertThat(response.items().get(0).type()).isEqualTo("SERVICE");
        assertThat(response.items().get(0).unitPrice()).isEqualByComparingTo("150.00");
        assertThat(response.totalAmount()).isEqualByComparingTo("150.00");
        verify(budgetRepository, times(1)).save(budget);
    }

    @Test
    @DisplayName("fails to add a service when the order is not in diagnosis")
    void failsWhenOrderNotInDiagnosis() {
        UUID serviceOrderId = UUID.randomUUID();
        when(serviceOrderRepository.findById(serviceOrderId))
                .thenReturn(Optional.of(order(serviceOrderId, ServiceOrderStatus.AWAITING_APPROVAL)));

        assertThatThrownBy(() -> useCase.execute(new AddServiceCommand(serviceOrderId, UUID.randomUUID(), "Serviço", 1)))
                .isInstanceOf(ConflictException.class);

        verify(budgetRepository, never()).save(any());
    }
}
