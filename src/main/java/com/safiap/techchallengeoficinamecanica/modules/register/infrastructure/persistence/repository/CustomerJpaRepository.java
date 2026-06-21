package com.safiap.techchallengeoficinamecanica.modules.register.infrastructure.persistence.repository;

import com.safiap.techchallengeoficinamecanica.modules.register.infrastructure.persistence.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, UUID> {

    Optional<CustomerEntity> findByCpfEntity(String cpf);
}
