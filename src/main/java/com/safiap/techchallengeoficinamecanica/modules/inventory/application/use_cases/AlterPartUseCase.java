package com.safiap.techchallengeoficinamecanica.modules.inventory.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.inventory.application.commands.AlterPartCommand;
import com.safiap.techchallengeoficinamecanica.modules.inventory.application.responses.part.AlterPartResponse;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.entities.Part;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.repositories.PartRepository;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.value_objects.Money;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.ConflictException;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.UUID;

@Service
@AllArgsConstructor
public class AlterPartUseCase {

    private final PartRepository partRepository;


    public AlterPartResponse execute(AlterPartCommand command, UUID partId) {
        Part part = partRepository.findById(partId)
                .orElseThrow(() -> new NotFoundException("part not found: " + partId));
        part.update(
                command.name(),
                command.description(),
                new Money(command.price())
        );
        return partRepository.save(part)
                .map(p -> new AlterPartResponse(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getQuantity().value(),
                        p.getPrice().amount()
                ))
                .orElseThrow(() -> new ConflictException("Fail to update part"));
    }
}
