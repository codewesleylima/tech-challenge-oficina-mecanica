package com.safiap.techchallengeoficinamecanica.modules.auth.application.responses;

import com.safiap.techchallengeoficinamecanica.modules.auth.domain.entities.Role;

import java.util.UUID;

public record UserCreateResponse(
        UUID id,
        String email,
        Role role
) {
}
