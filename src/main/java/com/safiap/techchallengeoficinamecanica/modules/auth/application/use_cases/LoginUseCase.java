package com.safiap.techchallengeoficinamecanica.modules.auth.application.use_cases;


import com.safiap.techchallengeoficinamecanica.modules.auth.application.commands.UserLoginCommand;
import com.safiap.techchallengeoficinamecanica.modules.auth.application.responses.UserTokenResponse;
import com.safiap.techchallengeoficinamecanica.modules.auth.application.service.AuthenticatedUser;
import com.safiap.techchallengeoficinamecanica.modules.auth.application.service.Authenticator;
import com.safiap.techchallengeoficinamecanica.modules.auth.application.service.TokenService;
import org.springframework.stereotype.Service;

@Service
public class LoginUseCase {


    private final Authenticator authenticator;
    private final TokenService tokenService;

    public LoginUseCase(Authenticator authenticator, TokenService tokenService) {
        this.authenticator = authenticator;
        this.tokenService = tokenService;
    }

    public UserTokenResponse execute(UserLoginCommand command) {
        AuthenticatedUser user = authenticator.authenticate(command.email(), command.password());
        return tokenService.generateToken(user);
    }
}
