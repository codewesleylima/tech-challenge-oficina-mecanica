package com.safiap.techchallengeoficinamecanica.modules.register.infrastructure.persistence.repositories;

import com.safiap.techchallengeoficinamecanica.modules.register.infrastructure.persistence.entities.JPACustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JPACustomerRepository extends JpaRepository<JPACustomerEntity, UUID> {

    Optional<JPACustomerEntity> findByCnpjCpf(String cnpjCpf);
}
