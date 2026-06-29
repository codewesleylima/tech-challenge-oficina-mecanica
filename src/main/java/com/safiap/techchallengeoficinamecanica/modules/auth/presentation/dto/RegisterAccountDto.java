package com.safiap.techchallengeoficinamecanica.modules.auth.presentation.dto;

public record RegisterAccountDto(
        String email,
        String password,
        String name,
        String phone,
        String cnpjCpf
) {
}
