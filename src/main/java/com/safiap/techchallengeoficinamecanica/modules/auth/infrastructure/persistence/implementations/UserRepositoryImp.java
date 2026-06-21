package com.safiap.techchallengeoficinamecanica.modules.auth.infrastructure.persistence.implementations;

import com.safiap.techchallengeoficinamecanica.modules.auth.domain.entities.User;
import com.safiap.techchallengeoficinamecanica.modules.auth.domain.repositories.UserRepository;
import com.safiap.techchallengeoficinamecanica.modules.auth.infrastructure.persistence.mappers.UserMapper;
import com.safiap.techchallengeoficinamecanica.modules.auth.infrastructure.persistence.repositories.JpaUserRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class UserRepositoryImp implements UserRepository {


    private final JpaUserRepository jpaUserRepository;

    public UserRepositoryImp(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public void saveUser(User user) {
        jpaUserRepository.save(UserMapper.toJpa(user));
    }
}
