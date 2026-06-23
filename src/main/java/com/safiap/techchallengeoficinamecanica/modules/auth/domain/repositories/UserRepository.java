package com.safiap.techchallengeoficinamecanica.modules.auth.domain.repositories;

import com.safiap.techchallengeoficinamecanica.modules.auth.domain.entities.User;


public interface UserRepository {

    void saveUser(User user);

}
