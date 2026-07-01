package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.AddPartCommand;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.ports.InventoryCatalogPort;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.BudgetResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.Budget;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.BudgetRepository;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
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

    private final BudgetRepository budgetRepository = mock(BudgetRepository.class);
    private final InventoryCatalogPort inventoryCatalogPort = mock(InventoryCatalogPort.class);
    private final AddPartToBudgetUseCase useCase =
            new AddPartToBudgetUseCase(budgetRepository, inventoryCatalogPort);

    @Test
    @DisplayName("adds a part to the budget using the catalog price")
    void addsPartUsingCatalogPrice() {
        UUID serviceOrderId = UUID.randomUUID();
        UUID partId = UUID.randomUUID();
        Budget budget = Budget.create(serviceOrderId);
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
    @DisplayName("fails to add a part when the budget does not exist")
    void failsWhenBudgetNotFound() {
        UUID serviceOrderId = UUID.randomUUID();
        when(budgetRepository.findByServiceOrderId(serviceOrderId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(new AddPartCommand(serviceOrderId, UUID.randomUUID(), "Filtro", 1)))
                .isInstanceOf(NotFoundException.class);

        verify(budgetRepository, never()).save(any());
    }
}
