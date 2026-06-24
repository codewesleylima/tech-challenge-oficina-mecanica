package com.safiap.techchallengeoficinamecanica.modules.inventory.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.repositories.PartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class DeletePartUseCase {


    private final PartRepository partRepository;

    public void execute(UUID partId){
        partRepository.delete(partId);
    }
}
