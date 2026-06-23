package com.safiap.techchallengeoficinamecanica.modules.auth.application.service;

import com.safiap.techchallengeoficinamecanica.modules.auth.application.responses.UserTokenResponse;

public interface TokenService {

    UserTokenResponse generateToken(AuthenticatedUser user);
}
