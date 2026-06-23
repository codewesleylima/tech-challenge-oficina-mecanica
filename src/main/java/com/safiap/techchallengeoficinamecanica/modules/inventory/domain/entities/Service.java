package com.safiap.techchallengeoficinamecanica.modules.inventory.domain.entities;

import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.value_objects.Money;
import com.safiap.techchallengeoficinamecanica.modules.shared.common.AggregateRoot;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;

import java.util.Objects;
import java.util.UUID;

public class Service extends AggregateRoot {

    private static final int MAX_DESCRIPTION_LENGTH = 150;

    private UUID id;
    private String name;
    private String description;
    private Money price;

    private Service() {}

    private Service(UUID id, String name, String description, Money price) {
        description = description == null ? null : description.trim();
        validate(id, name, description, price);
        this.id = id;
        this.name = name.trim();
        this.description = description;
        this.price = price;
    }

    public static Service createService(String name, String description, Money price) {
        return new Service(UUID.randomUUID(), name, description, price);
    }

    public static Service buildService(UUID id, String name, String description, Money price) {
        return new Service(id, name, description, price);
    }

    private void validate(UUID id, String name, String description, Money price) {
        Objects.requireNonNull(id, " id is null");
        Objects.requireNonNull(name, " name is null");
        if (name.isBlank()) {
            throw new DomainException("name is blank");
        }
        if (description != null && description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new DomainException("description must have at most " + MAX_DESCRIPTION_LENGTH + " characters");
        }
        Objects.requireNonNull(price, " price is null");
        if (price.isZero()) {
            throw new DomainException("price must be greater than zero");
        }
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Money getPrice() {
        return price;
    }
}
