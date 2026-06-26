package com.safiap.techchallengeoficinamecanica.modules.inventory.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.inventory.application.responses.part.GetPartResponse;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.repositories.PartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ListPartsUseCase {

    private final PartRepository partRepository;

    public List<GetPartResponse> execute() {
        return partRepository.findAll()
                .stream()
                .map(part -> new GetPartResponse(
                        part.getId(),
                        part.getName(),
                        part.getDescription(),
                        part.getQuantity().value(),
                        part.getPrice().amount()
                ))
                .toList();
    }
}
