package com.safiap.techchallengeoficinamecanica.modules.auth.application.use_cases;


import com.safiap.techchallengeoficinamecanica.modules.auth.application.commands.AddUserCommand;
import com.safiap.techchallengeoficinamecanica.modules.auth.application.responses.UserCreateResponse;
import com.safiap.techchallengeoficinamecanica.modules.auth.application.service.PasswordHasher;
import com.safiap.techchallengeoficinamecanica.modules.auth.domain.entities.User;
import com.safiap.techchallengeoficinamecanica.modules.auth.domain.repositories.UserRepository;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserUseCase {


    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;


    public RegisterUserUseCase(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public UserCreateResponse execute(AddUserCommand command){
        User user = User.createUser(command.email(), passwordHasher.hash(command.password()));

        userRepository.findByEmail(user.getEmail()).ifPresent(existingUser -> {
            throw new DomainException("already exists email " + existingUser.getEmail().value());
        });

        userRepository.saveUser(user);
        return new UserCreateResponse(
                user.getId(),
                user.getEmail().value(),
                user.getRole()
        );
    }
}
