package com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "diagnoses")
@Getter
@NoArgsConstructor
public class JPADiagnosisEntity {

    @Id
    private UUID id;

    @Column
    private String notes;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    @Column
    private LocalDateTime concludedAt;

    public JPADiagnosisEntity(UUID id, String notes, LocalDateTime startedAt, LocalDateTime concludedAt) {
        this.id = id;
        this.notes = notes;
        this.startedAt = startedAt;
        this.concludedAt = concludedAt;
    }
}
