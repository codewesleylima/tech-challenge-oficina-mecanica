package com.safiap.techchallengeoficinamecanica.modules.auth.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record UserLoginDto(
        @NotBlank(message = "email is required")
        String email,

        @NotBlank(message = "password is required")
        String password
) {
}
