package com.safiap.techchallengeoficinamecanica.modules.inventory.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.inventory.application.responses.part.GetPartResponse;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.entities.Part;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.repositories.PartRepository;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class GetPartByIdUseCase {

    private final PartRepository partRepository;

    public GetPartResponse execute(UUID partId) {
        Part part = partRepository.findById(partId)
                .orElseThrow(() -> new NotFoundException("part not found: " + partId));
        return new GetPartResponse(
                part.getId(),
                part.getName(),
                part.getDescription(),
                part.getQuantity().value(),
                part.getPrice().amount()
        );
    }
}
