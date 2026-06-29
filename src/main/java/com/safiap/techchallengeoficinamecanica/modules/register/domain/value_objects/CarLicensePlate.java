package com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects;

import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;

public record CarLicensePlate(String plate) {

    private static final String MERCOSUL =
            "^[A-Z]{3}[0-9][A-Z][0-9]{2}$";

    public CarLicensePlate {
        if (plate == null || !plate.matches(MERCOSUL)) {
            throw new DomainException("Invalid license plate");
        }
    }
}
