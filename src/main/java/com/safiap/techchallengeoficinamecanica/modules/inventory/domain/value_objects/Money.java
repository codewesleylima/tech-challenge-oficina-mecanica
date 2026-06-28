package com.safiap.techchallengeoficinamecanica.modules.inventory.domain.value_objects;

import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Money(BigDecimal amount) {

    public Money {
        DomainException.requireNonNull(amount, " amount is null");
        if (amount.signum() < 0) {
            throw new DomainException("amount cannot be negative");
        }
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public boolean isZero() {
        return amount.signum() == 0;
    }
}
