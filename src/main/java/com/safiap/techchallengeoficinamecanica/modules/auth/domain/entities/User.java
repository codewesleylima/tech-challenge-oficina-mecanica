package com.safiap.techchallengeoficinamecanica.modules.auth.domain.entities;

import com.safiap.techchallengeoficinamecanica.modules.auth.domain.value_objects.Email;

import java.util.UUID;

public class User {

    private UUID id;
    private Email email;
    private String password;
    private Role role;
    private UUID customerId;


    public User(UUID id, String email, String passwordHash, Role role) {
        this(id, email, passwordHash, role, null);
    }

    public User(UUID id, String email, String passwordHash, Role role, UUID customerId) {
        this.id = id;
        this.email = new Email(email);
        this.password = passwordHash;
        this.role = role;
        this.customerId = customerId;
    }

    public static User createUser(String email, String passwordHash) {
        return new User(UUID.randomUUID(), email, passwordHash, Role.ROLE_USER);
    }

    public static User createCustomerUser(String email, String passwordHash, UUID customerId) {
        return new User(UUID.randomUUID(), email, passwordHash, Role.ROLE_CUSTOMER, customerId);
    }

    public UUID getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public UUID getCustomerId() {
        return customerId;
    }
}
