package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities.ServiceOrder;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetServiceOrderByIdUseCase {

    private final ServiceOrderRepository serviceOrderRepository;

    public GetServiceOrderByIdUseCase(ServiceOrderRepository serviceOrderRepository) {
        this.serviceOrderRepository = serviceOrderRepository;
    }

    public ServiceOrderResponse execute(UUID serviceOrderId) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(serviceOrderId)
                .orElseThrow(() -> new DomainException("Service order not found"));

        return new ServiceOrderResponse(
                serviceOrder.getServiceOrderId(),
                serviceOrder.getCustomerId(),
                serviceOrder.getVehicleId(),
                serviceOrder.getProblemDescription(),
                serviceOrder.getStatus(),
                serviceOrder.getOpenedAt(),
                serviceOrder.getConcludedAt()
        );
    }
}
