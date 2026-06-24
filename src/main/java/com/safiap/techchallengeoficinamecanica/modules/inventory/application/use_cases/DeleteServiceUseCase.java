package com.safiap.techchallengeoficinamecanica.modules.inventory.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.repositories.ServiceRepository;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class DeleteServiceUseCase {

    private final ServiceRepository serviceRepository;

    public void execute(UUID serviceId) {
        serviceRepository.findById(serviceId)
                .orElseThrow(() -> new NotFoundException("service not found: " + serviceId));
        serviceRepository.delete(serviceId);
    }
}
