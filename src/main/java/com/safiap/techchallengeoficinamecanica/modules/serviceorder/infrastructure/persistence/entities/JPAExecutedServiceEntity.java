package com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "executed_services")
@Getter
@NoArgsConstructor
public class JPAExecutedServiceEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime executedAt;

    public JPAExecutedServiceEntity(UUID id, String description, LocalDateTime executedAt) {
        this.id = id;
        this.description = description;
        this.executedAt = executedAt;
    }
}
