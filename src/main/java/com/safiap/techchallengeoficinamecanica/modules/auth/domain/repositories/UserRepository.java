package com.safiap.techchallengeoficinamecanica.modules.auth.domain.repositories;

import com.safiap.techchallengeoficinamecanica.modules.auth.domain.entities.User;
import com.safiap.techchallengeoficinamecanica.modules.auth.domain.value_objects.Email;

import java.util.Optional;

public interface UserRepository {

    void saveUser(User user);

    Optional<User> findByEmail(Email email);

}
