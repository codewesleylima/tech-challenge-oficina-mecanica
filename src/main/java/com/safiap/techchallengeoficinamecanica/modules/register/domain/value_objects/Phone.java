package com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects;

import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;

public record Phone(String value) {

    public Phone {
        if (value == null || value.isBlank()) {
            throw new DomainException("Phone cannot be null or empty.");
        }

        value = value.replaceAll("\\D", "");

        if (value.length() < 10 || value.length() > 11) {
            throw new DomainException("Invalid phone number.");
        }
    }
}
