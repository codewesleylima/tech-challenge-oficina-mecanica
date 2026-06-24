package com.safiap.techchallengeoficinamecanica.modules.auth.application.commands;

public record AddUserCommand(
        String email,
        String password
) {
}
