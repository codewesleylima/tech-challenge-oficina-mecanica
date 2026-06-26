package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects;

import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.NotFoundException;

import java.util.Arrays;

public enum ServiceOrderPriority {

    URGENT(4),
    HIGH(3),
    NORMAL(2),
    LOW(1);

    private final int value;

    ServiceOrderPriority(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }

    public ServiceOrderPriority increase() {
        return this == URGENT ? this : fromValue(value + 1);
    }

    public ServiceOrderPriority decrease() {
        return this == LOW ? this : fromValue(value - 1);
    }

    public static ServiceOrderPriority fromValue(int value) {
        return Arrays.stream(ServiceOrderPriority.values()).filter(x -> x.getValue() == value).findFirst().orElseThrow(()->new NotFoundException("Service priority not defined: "+value));
    }
}
