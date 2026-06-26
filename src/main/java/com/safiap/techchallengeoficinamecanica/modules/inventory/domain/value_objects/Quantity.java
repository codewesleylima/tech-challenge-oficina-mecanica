package com.safiap.techchallengeoficinamecanica.modules.inventory.domain.value_objects;

import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;

public record Quantity(int value) {

    public Quantity {
        if (value < 0) {
            throw new DomainException("quantity cannot be negative");
        }
    }

    public Quantity add(int amount) {
        if (amount <= 0) {
            throw new DomainException("amount must be greater than zero");
        }
        return new Quantity(value + amount);
    }

    public Quantity subtract(int amount) {
        if (amount <= 0) {
            throw new DomainException("amount must be greater than zero");
        }
        if (amount > value) {
            throw new DomainException("insufficient stock");
        }
        return new Quantity(value - amount);
    }
}
