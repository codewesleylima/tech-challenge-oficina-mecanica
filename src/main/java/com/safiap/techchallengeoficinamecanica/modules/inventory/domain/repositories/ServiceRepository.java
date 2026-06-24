package com.safiap.techchallengeoficinamecanica.modules.inventory.domain.repositories;

import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.entities.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceRepository {

    Optional<Service> findById(UUID id);
    List<Service> findAll();
    Optional<Service> save(Service service);
    void delete(UUID id);

}
