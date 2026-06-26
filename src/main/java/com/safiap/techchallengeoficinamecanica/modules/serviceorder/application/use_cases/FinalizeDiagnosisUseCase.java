package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceOrder;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.BudgetRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.BudgetStatus;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.ConflictException;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class FinalizeDiagnosisUseCase {

    private final ServiceOrderRepository serviceOrderRepository;
    private final BudgetRepository budgetRepository;

    public FinalizeDiagnosisUseCase(ServiceOrderRepository serviceOrderRepository,
                                    BudgetRepository budgetRepository) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.budgetRepository = budgetRepository;
    }

    @Transactional
    public ServiceOrderResponse execute(UUID serviceOrderId) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(serviceOrderId)
                .orElseThrow(() -> new NotFoundException("Service order not found: " + serviceOrderId));

        budgetRepository.findByServiceOrderId(serviceOrderId)
                .filter(b -> b.getStatus() == BudgetStatus.FINALIZED && !b.getItems().isEmpty())
                .orElseThrow(() -> new ConflictException("A finalized budget with items is required before closing diagnosis"));

        serviceOrder.finalizeDiagnosis();
        serviceOrderRepository.save(serviceOrder);

        return ServiceOrderResponse.from(serviceOrder);
    }
}
