package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceOrder;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RejectBudgetUseCase {

    private static final Logger log = LoggerFactory.getLogger(RejectBudgetUseCase.class);

    private final ServiceOrderRepository serviceOrderRepository;

    public RejectBudgetUseCase(ServiceOrderRepository serviceOrderRepository) {
        this.serviceOrderRepository = serviceOrderRepository;
    }

    @Transactional
    public ServiceOrderResponse execute(UUID serviceOrderId) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(serviceOrderId)
                .orElseThrow(() -> new NotFoundException("Service order not found: " + serviceOrderId));

        serviceOrder.rejectBudget();
        serviceOrderRepository.save(serviceOrder);

        log.warn("Budget rejected by customer for service order {} - returned to diagnosis", serviceOrderId);

        return ServiceOrderResponse.from(serviceOrder);
    }
}
