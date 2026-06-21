package com.safiap.techchallengeoficinamecanica.modules.auth.domain.entities;

import com.safiap.techchallengeoficinamecanica.modules.auth.domain.value_objects.Email;

import java.util.UUID;

public class User {

    private UUID id;
    private Email email;
    private String password;
    private Role role;


    public User(UUID id, String email, String passwordHash, Role role) {
        this.id = id;
        this.email = new Email(email);
        this.password = passwordHash;
        this.role = role;
    }

    public static User createUser(String email, String passwordHash) {
        return new User(UUID.randomUUID(), email, passwordHash, Role.ROLE_USER);
    }
    public static User createEmployeeUser(String email, String passwordHash) {
        return new User(UUID.randomUUID(), email, passwordHash, Role.ROLE_EMPLOYEE);
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
}
