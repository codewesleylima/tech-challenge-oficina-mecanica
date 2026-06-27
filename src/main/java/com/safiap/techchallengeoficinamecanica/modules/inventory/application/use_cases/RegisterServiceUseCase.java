package com.safiap.techchallengeoficinamecanica.modules.inventory.application.use_cases;


import com.safiap.techchallengeoficinamecanica.modules.inventory.application.commands.RegisterServiceCommand;
import com.safiap.techchallengeoficinamecanica.modules.inventory.application.responses.service.RegisterServiceResponse;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.entities.Service;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.repositories.ServiceRepository;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.value_objects.Money;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.ConflictException;
import lombok.AllArgsConstructor;


@org.springframework.stereotype.Service
@AllArgsConstructor
public class RegisterServiceUseCase {


    private final ServiceRepository serviceRepository;

    public RegisterServiceResponse execute(RegisterServiceCommand command) {
        return serviceRepository.save(Service.createService(
                command.name(),
                command.description(),
                new Money(command.price())
        )).map(p->
                new RegisterServiceResponse(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getPrice().amount()
                        ))
                .orElseThrow(()->new ConflictException("Fail to save service"));
    }
}
