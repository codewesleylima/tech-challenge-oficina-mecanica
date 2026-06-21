package com.safiap.techchallengeoficinamecanica.modules.auth.infrastructure.persistence.repositories;

import com.safiap.techchallengeoficinamecanica.modules.auth.infrastructure.persistence.entities.JpaUserEntity;
import com.safiap.techchallengeoficinamecanica.modules.register.infrastructure.persistence.entities.JPACustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaUserRepository extends JpaRepository<JpaUserEntity, UUID> {

    Optional<JpaUserEntity> findByEmail(String email);

}
