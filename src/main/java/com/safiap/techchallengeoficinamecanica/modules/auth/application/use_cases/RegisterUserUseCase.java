package com.safiap.techchallengeoficinamecanica.modules.auth.application.use_cases;


import com.safiap.techchallengeoficinamecanica.modules.auth.application.commands.AddUserCommand;
import com.safiap.techchallengeoficinamecanica.modules.auth.application.responses.UserTokenResponse;
import com.safiap.techchallengeoficinamecanica.modules.auth.domain.entities.User;
import com.safiap.techchallengeoficinamecanica.modules.auth.domain.repositories.UserRepository;
import io.swagger.v3.oas.annotations.servers.Server;

@Server
public class RegisterUserUseCase {


    private final UserRepository userRepository;


    public RegisterUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserTokenResponse execute(AddUserCommand command){
        userRepository.saveUser(User.createUser(command.email(), command.password()));
        //TODO
        return new UserTokenResponse("",0l, "");
    }
}
