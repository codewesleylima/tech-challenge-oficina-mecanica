package com.safiap.techchallengeoficinamecanica.modules.auth.presentation.controllers;

import com.safiap.techchallengeoficinamecanica.modules.auth.application.commands.AddUserCommand;
import com.safiap.techchallengeoficinamecanica.modules.auth.application.commands.RegisterAccountCommand;
import com.safiap.techchallengeoficinamecanica.modules.auth.application.commands.UserLoginCommand;
import com.safiap.techchallengeoficinamecanica.modules.auth.application.responses.RegisterAccountResponse;
import com.safiap.techchallengeoficinamecanica.modules.auth.application.responses.UserCreateResponse;
import com.safiap.techchallengeoficinamecanica.modules.auth.application.responses.UserTokenResponse;
import com.safiap.techchallengeoficinamecanica.modules.auth.application.use_cases.LoginUseCase;
import com.safiap.techchallengeoficinamecanica.modules.auth.application.use_cases.RegisterAccountUseCase;
import com.safiap.techchallengeoficinamecanica.modules.auth.application.use_cases.RegisterUserUseCase;
import com.safiap.techchallengeoficinamecanica.modules.auth.presentation.dto.RegisterAccountDto;
import com.safiap.techchallengeoficinamecanica.modules.auth.presentation.dto.UserLoginDto;
import com.safiap.techchallengeoficinamecanica.modules.auth.presentation.dto.AddUserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final RegisterUserUseCase registerUserUseCase;
    private final RegisterAccountUseCase registerAccountUseCase;
    private final LoginUseCase loginUseCase;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public AuthController(RegisterUserUseCase registerUserUseCase, RegisterAccountUseCase registerAccountUseCase, LoginUseCase loginUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.registerAccountUseCase = registerAccountUseCase;
        this.loginUseCase = loginUseCase;
    }
    @PostMapping("/register")
    public ResponseEntity<UserCreateResponse> register(@Valid @RequestBody AddUserDto dto){
        return ResponseEntity.status(201).body(registerUserUseCase.execute(new AddUserCommand(dto.email(),dto.password())));
    }

    @PostMapping("/register-account")
    public ResponseEntity<RegisterAccountResponse> registerAccount(@Valid @RequestBody RegisterAccountDto dto){
        return ResponseEntity.status(201).body(registerAccountUseCase.execute(
                new RegisterAccountCommand(dto.email(), dto.password(), dto.name(), dto.phone(), dto.cnpjCpf())
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenResponse> login(@RequestBody UserLoginDto dto){
        log.info("1 - User login: {}", dto.email());
        return ResponseEntity.ok(loginUseCase.execute(new UserLoginCommand(dto.email(), dto.password())));
    }
}
