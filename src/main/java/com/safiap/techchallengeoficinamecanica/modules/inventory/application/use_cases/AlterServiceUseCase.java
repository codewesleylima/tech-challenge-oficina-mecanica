package com.safiap.techchallengeoficinamecanica.modules.inventory.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.inventory.application.commands.AlterServiceCommand;
import com.safiap.techchallengeoficinamecanica.modules.inventory.application.responses.service.AlterServiceResponse;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.entities.Service;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.repositories.ServiceRepository;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.value_objects.Money;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.ConflictException;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import lombok.AllArgsConstructor;

import java.util.UUID;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class AlterServiceUseCase {

    private final ServiceRepository serviceRepository;

    public AlterServiceResponse execute(AlterServiceCommand command, UUID serviceId) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new NotFoundException("service not found: " + serviceId));
        service.update(
                command.name(),
                command.description(),
                new Money(command.price())
        );
        return serviceRepository.save(service)
                .map(s -> new AlterServiceResponse(
                        s.getId(),
                        s.getName(),
                        s.getDescription(),
                        s.getPrice().amount()
                ))
                .orElseThrow(() -> new ConflictException("Fail to update service"));
    }
}
