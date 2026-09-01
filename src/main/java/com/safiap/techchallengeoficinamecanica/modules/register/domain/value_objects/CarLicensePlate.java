package com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects;

import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;

public record CarLicensePlate(String plate) {

    private static final String MERCOSUL =
            "^[A-Z]{3}[0-9][A-Z][0-9]{2}$";

    private static final String OLDPLATEPATTERN =
            "^[A-Z]{3}[0-9]{4}$";

    public CarLicensePlate {
        if(plate == null) {
            throw new DomainException("Invalid license plate");
        }

        String normalizePlate = normalizeCarLicensePlate(plate);

        if (!normalizePlate.matches(MERCOSUL) && !normalizePlate.matches(OLDPLATEPATTERN)) {
            throw new DomainException("Invalid license plate");
        }

        plate = normalizePlate;
    }

    private static String normalizeCarLicensePlate (String plate) {
        return plate.trim().toUpperCase().replace("-", "");
    }
}
