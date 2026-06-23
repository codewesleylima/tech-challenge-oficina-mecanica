package com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects;

import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;

public record Kilometers(Integer value) {

    public Kilometers {
        if (value == null || value < 0 ) {
            throw  new DomainException("Invalid value");
        }
    };
}
