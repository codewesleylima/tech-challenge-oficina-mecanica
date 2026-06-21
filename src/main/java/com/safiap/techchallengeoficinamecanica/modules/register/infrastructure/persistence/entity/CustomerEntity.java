package com.safiap.techchallengeoficinamecanica.modules.register.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity {

    @Id
    private UUID customerIdEntity;

    @Column(nullable = false)
    private String nameEntity;

    @Column(nullable = false, unique = true)
    private String cpfEntity;

    @Column(nullable = false)
    private String emailEntity;

    @Column(nullable = false)
    private String phoneEntity;
}
