package com.safiap.techchallengeoficinamecanica.modules.inventory.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.inventory.application.responses.service.GetServiceResponse;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.repositories.ServiceRepository;
import lombok.AllArgsConstructor;

import java.util.List;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class ListServicesUseCase {

    private final ServiceRepository serviceRepository;

    public List<GetServiceResponse> execute() {
        return serviceRepository.findAll()
                .stream()
                .map(service -> new GetServiceResponse(
                        service.getId(),
                        service.getName(),
                        service.getDescription(),
                        service.getPrice().amount()
                ))
                .toList();
    }
}
