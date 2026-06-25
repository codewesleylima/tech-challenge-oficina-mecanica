package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.ConflictException;
import org.springframework.stereotype.Service;

@Service
public class PullServiceOrderUseCase {

    private final ServiceOrderRepository serviceOrderRepository;

    public PullServiceOrderUseCase(ServiceOrderRepository serviceOrderRepository) {
        this.serviceOrderRepository = serviceOrderRepository;
    }


    public ServiceOrderResponse execute(){
        return serviceOrderRepository.pullNextOrderService(ServiceOrderStatus.RECEIVED)
                .map(serviceOrder->new ServiceOrderResponse(
                        serviceOrder.getServiceOrderId(),
                        serviceOrder.getCustomerId(),
                        serviceOrder.getVehicleId(),
                        serviceOrder.getProblemDescription(),
                        serviceOrder.getStatus(),
                        serviceOrder.getOpenedAt(),
                        serviceOrder.getConcludedAt(),
                        serviceOrder.getPriority().name()
                ))
                .orElseThrow(()->new ConflictException("there is no pending service orders"));
    }
}
