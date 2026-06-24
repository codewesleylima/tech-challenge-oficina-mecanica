package com.safiap.techchallengeoficinamecanica.modules.inventory.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.repositories.PartRepository;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class DeletePartUseCase {


    private final PartRepository partRepository;

    public void execute(UUID partId){
        partRepository.findById(partId)
                .orElseThrow(() -> new NotFoundException("part not found: " + partId));
        partRepository.delete(partId);
    }
}
