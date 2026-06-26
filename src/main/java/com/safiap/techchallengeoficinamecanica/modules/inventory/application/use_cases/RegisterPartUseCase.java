package com.safiap.techchallengeoficinamecanica.modules.inventory.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.inventory.application.commands.RegisterPartCommand;
import com.safiap.techchallengeoficinamecanica.modules.inventory.application.responses.part.RegisterPartResponse;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.entities.Part;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.repositories.PartRepository;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.value_objects.Money;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.value_objects.Quantity;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.ConflictException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@AllArgsConstructor
public class RegisterPartUseCase {


    private final PartRepository partRepository;

    public RegisterPartResponse execute(RegisterPartCommand command) {
        return partRepository.save(Part.createPart(
                command.name(),
                command.description(),
                new Money(command.price()),
                new Quantity(command.quantity())
        )).map(p->
                new RegisterPartResponse(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getQuantity().value(),
                        p.getPrice().amount()
                        ))
                .orElseThrow(()->new ConflictException("Fail to save part"));
    }
}
