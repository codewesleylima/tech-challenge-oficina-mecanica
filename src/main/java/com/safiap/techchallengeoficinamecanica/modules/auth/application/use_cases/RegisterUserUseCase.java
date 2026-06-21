package com.safiap.techchallengeoficinamecanica.modules.auth.application.use_cases;


import com.safiap.techchallengeoficinamecanica.modules.auth.application.commands.AddUserCommand;
import com.safiap.techchallengeoficinamecanica.modules.auth.application.responses.UserCreateResponse;
import com.safiap.techchallengeoficinamecanica.modules.auth.application.responses.UserTokenResponse;
import com.safiap.techchallengeoficinamecanica.modules.auth.domain.entities.User;
import com.safiap.techchallengeoficinamecanica.modules.auth.domain.repositories.UserRepository;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserUseCase {


    private final UserRepository userRepository;


    public RegisterUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserCreateResponse execute(AddUserCommand command){
        User user = User.createUser(command.email(), command.password());
        userRepository.saveUser(user);
        return new UserCreateResponse(
                user.getId(),
                user.getEmail().value(),
                user.getRole()
        );
    }
}
