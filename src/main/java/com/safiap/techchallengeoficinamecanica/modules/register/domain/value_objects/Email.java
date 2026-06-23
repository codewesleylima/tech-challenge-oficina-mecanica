package com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects;

import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;

import java.util.regex.Pattern;

public record Email(String value) {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public Email {
        if (value == null || value.isBlank()) {
            throw new DomainException("Email cannot be null or empty.");
        }

        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new DomainException("Invalid email format.");
        }

        value = value.trim().toLowerCase();
    }
}
