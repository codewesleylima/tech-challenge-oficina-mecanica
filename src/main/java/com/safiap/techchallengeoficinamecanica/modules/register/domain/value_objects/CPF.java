package com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects;

import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;

public record CPF(String cpf) {

    public CPF {
        if (cpf == null || cpf.length() != 11) {
            throw new DomainException("Invalid cpf");
        }

    }
}
