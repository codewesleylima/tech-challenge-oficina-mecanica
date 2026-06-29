package com.safiap.techchallengeoficinamecanica.modules.auth.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.auth.application.commands.RegisterAccountCommand;
import com.safiap.techchallengeoficinamecanica.modules.auth.application.responses.RegisterAccountResponse;
import com.safiap.techchallengeoficinamecanica.modules.auth.application.service.CustomerRegistration;
import com.safiap.techchallengeoficinamecanica.modules.auth.application.service.PasswordHasher;
import com.safiap.techchallengeoficinamecanica.modules.auth.domain.entities.User;
import com.safiap.techchallengeoficinamecanica.modules.auth.domain.repositories.UserRepository;
import com.safiap.techchallengeoficinamecanica.modules.auth.domain.value_objects.Email;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.ConflictException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RegisterAccountUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final CustomerRegistration customerRegistration;

    public RegisterAccountUseCase(UserRepository userRepository,
                                  PasswordHasher passwordHasher,
                                  CustomerRegistration customerRegistration) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.customerRegistration = customerRegistration;
    }

    @Transactional
    public RegisterAccountResponse execute(RegisterAccountCommand command) {
        userRepository.findByEmail(new Email(command.email())).ifPresent(existingUser -> {
            throw new ConflictException("Email already registered.");
        });

        UUID customerId = customerRegistration.register(
                command.name(),
                command.email(),
                command.phone(),
                command.cpf()
        );

        User user = User.createCustomerUser(
                command.email(),
                passwordHasher.hash(command.password()),
                customerId
        );
        userRepository.saveUser(user);

        return new RegisterAccountResponse(
                user.getId(),
                user.getEmail().value(),
                user.getRole(),
                customerId
        );
    }
}
