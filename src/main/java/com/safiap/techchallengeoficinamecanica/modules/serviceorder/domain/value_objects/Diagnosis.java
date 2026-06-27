package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects;

import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;

public record Diagnosis(String value) {

    public Diagnosis {
        if (value == null || value.isBlank()) {
            throw new DomainException("Diagnosis description cannot be null or empty");
        }
        value = value.trim();
    }
}
