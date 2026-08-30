package com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.application.responses.ServiceOrderResponse;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.repositories.ServiceOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetAllServiceOrdersUseCase {

    private final ServiceOrderRepository serviceOrderRepository;

    public GetAllServiceOrdersUseCase(ServiceOrderRepository serviceOrderRepository) {
        this.serviceOrderRepository = serviceOrderRepository;
    }

    public List<ServiceOrderResponse> execute() {
        return serviceOrderRepository.getAllServiceOrdersFiltered()
                .stream().map(ServiceOrderResponse::from).collect(Collectors.toList());
    }
}
