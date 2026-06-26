package com.safiap.techchallengeoficinamecanica.modules.inventory.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.inventory.application.responses.part.AlterPartResponse;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.entities.Part;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.repositories.PartRepository;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.ConflictException;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class DecreasePartStockUseCase {

    private final PartRepository partRepository;

    public AlterPartResponse execute(UUID partId, int amount) {
        Part part = partRepository.findById(partId)
                .orElseThrow(() -> new NotFoundException("part not found: " + partId));
        part.decreaseStock(amount);
        return partRepository.save(part)
                .map(p -> new AlterPartResponse(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getQuantity().value(),
                        p.getPrice().amount()
                ))
                .orElseThrow(() -> new ConflictException("Fail to update part stock"));
    }
}
