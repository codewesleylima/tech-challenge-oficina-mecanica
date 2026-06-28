package com.safiap.techchallengeoficinamecanica.modules.register.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name="customers")
@Getter
@NoArgsConstructor
public class JPACustomerEntity {
    @Id
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private String cpf;

    public JPACustomerEntity(String name, String email, String phone, String cpf) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.cpf = cpf;
    }

    public JPACustomerEntity(UUID customerId, String name, String email, String phone, String cpf) {
        this.id = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.cpf = cpf;
    }
}
