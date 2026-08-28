package com.safiap.techchallengeoficinamecanica.modules.auth.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterAccountDto(
        @NotBlank(message = "email is required")
        @Email(message = "email must be valid")
        String email,

        @NotBlank(message = "password is required")
        String password,

        @NotBlank(message = "name is required")
        String name,

        @NotBlank(message = "phone is required")
        String phone,

        @NotBlank(message = "cnpjCpf is required")
        String cnpjCpf
) {
}
