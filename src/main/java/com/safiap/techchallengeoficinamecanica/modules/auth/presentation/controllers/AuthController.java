package com.safiap.techchallengeoficinamecanica.modules.auth.presentation.controllers;

import com.safiap.techchallengeoficinamecanica.modules.auth.application.commands.AddUserCommand;
import com.safiap.techchallengeoficinamecanica.modules.auth.application.commands.UserLoginCommand;
import com.safiap.techchallengeoficinamecanica.modules.auth.application.responses.UserCreateResponse;
import com.safiap.techchallengeoficinamecanica.modules.auth.application.responses.UserTokenResponse;
import com.safiap.techchallengeoficinamecanica.modules.auth.application.use_cases.LoginUseCase;
import com.safiap.techchallengeoficinamecanica.modules.auth.application.use_cases.RegisterUserUseCase;
import com.safiap.techchallengeoficinamecanica.modules.auth.presentation.dto.UserLoginDto;
import com.safiap.techchallengeoficinamecanica.modules.auth.presentation.dto.AddUserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUseCase loginUseCase;

    public AuthController(RegisterUserUseCase registerUserUseCase, LoginUseCase loginUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUseCase = loginUseCase;
    }
    @PostMapping("/register")
    public ResponseEntity<UserCreateResponse> register(@RequestBody AddUserDto dto){
        return ResponseEntity.status(201).body(registerUserUseCase.execute(new AddUserCommand(dto.email(),dto.password())));
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenResponse> login(@RequestBody UserLoginDto dto){
        return ResponseEntity.ok(loginUseCase.execute(new UserLoginCommand(dto.email(), dto.password())));
    }
}
