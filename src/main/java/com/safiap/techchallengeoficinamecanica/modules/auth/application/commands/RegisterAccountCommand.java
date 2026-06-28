package com.safiap.techchallengeoficinamecanica.modules.auth.application.commands;

public record RegisterAccountCommand(
        String email,
        String password,
        String name,
        String phone,
        String cpf
) {
}
