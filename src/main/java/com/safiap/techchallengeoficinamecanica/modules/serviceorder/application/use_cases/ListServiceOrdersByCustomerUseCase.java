package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ListServiceOrdersByCustomerUseCase {

    private final ServiceOrderRepository serviceOrderRepository;

    public ListServiceOrdersByCustomerUseCase(ServiceOrderRepository serviceOrderRepository) {
        this.serviceOrderRepository = serviceOrderRepository;
    }

    public List<ServiceOrderResponse> execute(UUID customerId) {
        return serviceOrderRepository.findByCustomerId(customerId).stream()
                .map(serviceOrder -> new ServiceOrderResponse(
                        serviceOrder.getServiceOrderId(),
                        serviceOrder.getCustomerId(),
                        serviceOrder.getVehicleId(),
                        serviceOrder.getProblemDescription(),
                        serviceOrder.getStatus(),
                        serviceOrder.getOpenedAt(),
                        serviceOrder.getConcludedAt()
                ))
                .toList();
    }
}
