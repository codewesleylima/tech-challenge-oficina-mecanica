package com.safiap.techchallengeoficinamecanica.modules.inventory.infrastructure.persistence.implementations;

import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.entities.Service;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.repositories.ServiceRepository;
import com.safiap.techchallengeoficinamecanica.modules.inventory.infrastructure.persistence.mappers.ServiceMapper;
import com.safiap.techchallengeoficinamecanica.modules.inventory.infrastructure.persistence.repositories.JpaServiceRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ServiceRepositoryImp implements ServiceRepository {

    private final JpaServiceRepository jpaServiceRepository;

    public ServiceRepositoryImp(JpaServiceRepository jpaServiceRepository) {
        this.jpaServiceRepository = jpaServiceRepository;
    }

    @Override
    public void save(Service service) {
        jpaServiceRepository.save(ServiceMapper.toJPA(service));
    }

    @Override
    public Optional<Service> findById(UUID id) {
        return jpaServiceRepository
                .findById(id)
                .map(ServiceMapper::toEntity);
    }
}
