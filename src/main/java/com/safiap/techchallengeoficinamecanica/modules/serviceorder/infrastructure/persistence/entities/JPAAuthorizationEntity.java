package com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "authorizations")
@Getter
@NoArgsConstructor
public class JPAAuthorizationEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String notes;

    @Column(nullable = false)
    private LocalDateTime authorizedAt;

    public JPAAuthorizationEntity(UUID id, String notes, LocalDateTime authorizedAt) {
        this.id = id;
        this.notes = notes;
        this.authorizedAt = authorizedAt;
    }
}
