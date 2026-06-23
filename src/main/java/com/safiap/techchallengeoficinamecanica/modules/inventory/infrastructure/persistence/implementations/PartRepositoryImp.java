package com.safiap.techchallengeoficinamecanica.modules.inventory.infrastructure.persistence.implementations;

import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.entities.Part;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.repositories.PartRepository;
import com.safiap.techchallengeoficinamecanica.modules.inventory.infrastructure.persistence.mappers.PartMapper;
import com.safiap.techchallengeoficinamecanica.modules.inventory.infrastructure.persistence.repositories.JpaPartRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class PartRepositoryImp implements PartRepository {

    private final JpaPartRepository jpaPartRepository;

    public PartRepositoryImp(JpaPartRepository jpaPartRepository) {
        this.jpaPartRepository = jpaPartRepository;
    }

    @Override
    public void save(Part part) {
        jpaPartRepository.save(PartMapper.toJPA(part));
    }

    @Override
    public Optional<Part> findById(UUID id) {
        return jpaPartRepository
                .findById(id)
                .map(PartMapper::toEntity);
    }
}
