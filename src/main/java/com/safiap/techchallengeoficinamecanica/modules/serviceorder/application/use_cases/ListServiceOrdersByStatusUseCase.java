package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListServiceOrdersByStatusUseCase {

    private final ServiceOrderRepository serviceOrderRepository;

    public ListServiceOrdersByStatusUseCase(ServiceOrderRepository serviceOrderRepository) {
        this.serviceOrderRepository = serviceOrderRepository;
    }

    public List<ServiceOrderResponse> execute(ServiceOrderStatus status) {
        return serviceOrderRepository.findByStatus(status)
                .stream().map(ServiceOrderResponse::from).collect(Collectors.toList());
    }
}
