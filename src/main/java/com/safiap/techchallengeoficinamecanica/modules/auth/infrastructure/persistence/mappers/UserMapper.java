package com.safiap.techchallengeoficinamecanica.modules.auth.infrastructure.persistence.mappers;

import com.safiap.techchallengeoficinamecanica.modules.auth.domain.entities.User;
import com.safiap.techchallengeoficinamecanica.modules.auth.infrastructure.persistence.entities.JpaUserEntity;

public class UserMapper {

    public static JpaUserEntity toJpa(User user) {
        return new JpaUserEntity(
                user.getId(),
                user.getEmail().value(),
                user.getPassword(),
                user.getRole()
        );
    }
    public static User toEntity(JpaUserEntity jpaUserEntity) {
        return new User(
                jpaUserEntity.getId(),
                jpaUserEntity.getEmail(),
                jpaUserEntity.getPassword(),
                jpaUserEntity.getRole()
        );
    }
}
