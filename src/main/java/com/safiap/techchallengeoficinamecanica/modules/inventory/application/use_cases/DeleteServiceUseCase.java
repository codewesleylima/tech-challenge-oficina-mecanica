package com.safiap.techchallengeoficinamecanica.modules.inventory.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.repositories.ServiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class DeleteServiceUseCase {

    private final ServiceRepository serviceRepository;

    public void execute(UUID serviceId) {
        serviceRepository.delete(serviceId);
    }
}
