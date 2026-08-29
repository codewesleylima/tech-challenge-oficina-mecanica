package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.services;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.commands.BudgetItemInput;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.ports.InventoryCatalogPort;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.Budget;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceOrder;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.BudgetRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.BudgetItemType;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.ConflictException;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Monta o orçamento da OS: cria o rascunho sob demanda no primeiro item adicionado
 * e resolve o preço de cada item no catálogo de peças/serviços.
 */
@Service
public class BudgetAssemblyService {

    private final ServiceOrderRepository serviceOrderRepository;
    private final BudgetRepository budgetRepository;
    private final InventoryCatalogPort inventoryCatalogPort;

    public BudgetAssemblyService(ServiceOrderRepository serviceOrderRepository,
                                 BudgetRepository budgetRepository,
                                 InventoryCatalogPort inventoryCatalogPort) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.budgetRepository = budgetRepository;
        this.inventoryCatalogPort = inventoryCatalogPort;
    }

    public Budget getOrCreate(UUID serviceOrderId) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(serviceOrderId)
                .orElseThrow(() -> new NotFoundException("Service order not found: " + serviceOrderId));
        return getOrCreate(serviceOrder);
    }

    public Budget getOrCreate(ServiceOrder serviceOrder) {
        if (serviceOrder.getStatus() != ServiceOrderStatus.IN_DIAGNOSIS)
            throw new ConflictException("Service order must be in IN_DIAGNOSIS status to build its budget");

        return budgetRepository.findByServiceOrderId(serviceOrder.getServiceOrderId())
                .orElseGet(() -> Budget.create(serviceOrder.getServiceOrderId()));
    }

    public void addItems(Budget budget, List<BudgetItemInput> items) {
        if (items == null || items.isEmpty()) return;
        items.forEach(item -> addItem(budget, item));
    }

    private void addItem(Budget budget, BudgetItemInput item) {
        if (item.type() == null)
            throw new DomainException("Budget item type is required (PART or SERVICE)");

        if (item.type() == BudgetItemType.PART)
            budget.addPart(item.itemId(), item.description(), item.quantity(),
                    inventoryCatalogPort.getPartPrice(item.itemId()));
        else
            budget.addService(item.itemId(), item.description(), item.quantity(),
                    inventoryCatalogPort.getServicePrice(item.itemId()));
    }
}
