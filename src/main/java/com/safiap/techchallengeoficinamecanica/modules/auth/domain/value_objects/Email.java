package com.safiap.techchallengeoficinamecanica.modules.auth.domain.value_objects;

import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;

import java.util.regex.Pattern;

public record Email(String value) {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public Email {
        if (value == null) {
            throw new DomainException("Email cannot be null or empty.");
        }
        value = value.trim().toLowerCase();
        if (value.isBlank()) {
            throw new DomainException("Email cannot be null or empty.");
        }
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new DomainException("Invalid email format.");
        }
}
