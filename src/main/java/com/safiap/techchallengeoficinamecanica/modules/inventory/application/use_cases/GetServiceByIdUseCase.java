package com.safiap.techchallengeoficinamecanica.modules.inventory.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.inventory.application.responses.service.GetServiceResponse;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.entities.Service;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.repositories.ServiceRepository;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import lombok.AllArgsConstructor;

import java.util.UUID;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class GetServiceByIdUseCase {

    private final ServiceRepository serviceRepository;

    public GetServiceResponse execute(UUID serviceId) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new NotFoundException("service not found: " + serviceId));
        return new GetServiceResponse(
                service.getId(),
                service.getName(),
                service.getDescription(),
                service.getPrice().amount()
        );
    }
}
