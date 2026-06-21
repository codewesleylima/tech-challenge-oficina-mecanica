package com.safiap.techchallengeoficinamecanica.modules.register.domain.entities;

import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.CPF;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.Email;
import com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects.Phone;
import com.safiap.techchallengeoficinamecanica.modules.shared.common.AggregateRoot;

import java.util.Objects;
import java.util.UUID;

public class Customer extends AggregateRoot {

    private UUID customerId;
    private String name;
    private Email email;
    private Phone phone;
    private CPF cpf;

    private Customer() {}

    private Customer(UUID customerId,
                     String name,
                     Email email,
                     Phone phone,
                     CPF cpf) {

        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.cpf = cpf;
    }

    public static Customer createCustomer(
                                  String name,
                                  Email email,
                                  Phone phone,
                                  CPF cpf) {

        Objects.requireNonNull(name, " name is null");
        Objects.requireNonNull(email, " email is null");
        Objects.requireNonNull(phone, " phone is null");
        Objects.requireNonNull(cpf, " cpf is null");

        return new Customer(
                UUID.randomUUID(),
                name,
                email,
                phone,
                cpf
        );
    }

    public static Customer buildCustomer( UUID customerId,
                                  String name,
                                  Email email,
                                  Phone phone,
                                  CPF cpf) {

        Objects.requireNonNull(customerId, " customerId is null");
        Objects.requireNonNull(name, " name is null");
        Objects.requireNonNull(email, " email is null");
        Objects.requireNonNull(phone, " phone is null");
        Objects.requireNonNull(cpf, " cpf is null");

        return new Customer(
                customerId,
                name,
                email,
                phone,
                cpf
        );
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    public Phone getPhone() {
        return phone;
    }

    public CPF getCpf() {
        return cpf;
    }
}
