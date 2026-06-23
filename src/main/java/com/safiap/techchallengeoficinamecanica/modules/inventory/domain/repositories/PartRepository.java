package com.safiap.techchallengeoficinamecanica.modules.inventory.domain.repositories;

import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.entities.Part;

import java.util.Optional;
import java.util.UUID;

public interface PartRepository {

    Optional<Part> findById(UUID id);
    void save(Part part);

}
