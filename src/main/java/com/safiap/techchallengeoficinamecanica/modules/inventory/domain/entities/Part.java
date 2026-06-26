package com.safiap.techchallengeoficinamecanica.modules.inventory.domain.entities;

import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.value_objects.Money;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.value_objects.Quantity;
import com.safiap.techchallengeoficinamecanica.modules.shared.common.AggregateRoot;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;

import java.util.UUID;

public class Part extends AggregateRoot {

    private static final int MAX_DESCRIPTION_LENGTH = 150;

    private UUID id;
    private String name;
    private String description;
    private Money price;
    private Quantity quantity;

    private Part() {}

    private Part(UUID id, String name, String description, Money price, Quantity quantity) {
        description = description == null ? null : description.trim();
        validate(id, name, description, price, quantity);
        this.id = id;
        this.name = name.trim();
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public static Part createPart(String name, String description, Money price, Quantity quantity) {
        return new Part(UUID.randomUUID(), name, description, price, quantity);
    }

    public static Part buildPart(UUID id, String name, String description, Money price, Quantity quantity) {
        return new Part(id, name, description, price, quantity);
    }

    public void decreaseStock(int amount) {
        this.quantity = this.quantity.subtract(amount);
    }

    public void increaseStock(int amount) {
        this.quantity = this.quantity.add(amount);
    }

    public void update(String name, String description, Money price) {
        description = description == null ? null : description.trim();
        validate(this.id, name, description, price, quantity);
        this.name = name.trim();
        this.description = description;
        this.price = price;

    }

    private void validate(UUID id, String name, String description, Money price, Quantity quantity) {
        DomainException.requireNonNull(id, " id is null");
        DomainException.requireNotBlank(name, "name is blank");
        if (description != null && description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new DomainException("description must have at most " + MAX_DESCRIPTION_LENGTH + " characters");
        }
        DomainException.requireNonNull(price, " price is null");
        if (price.isZero()) {
            throw new DomainException("price must be greater than zero");
        }
        DomainException.requireNonNull(quantity, " quantity is null");
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

    public Quantity getQuantity() {
        return quantity;
    }
}
