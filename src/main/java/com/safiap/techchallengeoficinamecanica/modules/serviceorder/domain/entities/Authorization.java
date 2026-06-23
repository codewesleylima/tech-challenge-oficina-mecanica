package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities;

import com.safiap.techchallengeoficinamecanica.modules.shared.common.Entity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Authorization extends Entity {

    private UUID authorizationId;
    private String notes;
    private LocalDateTime authorizedAt;

    private Authorization() {}

    private Authorization(UUID authorizationId, String notes, LocalDateTime authorizedAt) {
        this.authorizationId = authorizationId;
        this.notes = notes;
        this.authorizedAt = authorizedAt;
    }

    public static Authorization register(String notes) {
        Objects.requireNonNull(notes, "notes is null");
        return new Authorization(UUID.randomUUID(), notes, LocalDateTime.now());
    }

    public static Authorization build(UUID authorizationId, String notes, LocalDateTime authorizedAt) {
        Objects.requireNonNull(authorizationId, "authorizationId is null");
        Objects.requireNonNull(authorizedAt, "authorizedAt is null");
        return new Authorization(authorizationId, notes, authorizedAt);
    }

    public UUID getAuthorizationId() { return authorizationId; }
    public String getNotes() { return notes; }
    public LocalDateTime getAuthorizedAt() { return authorizedAt; }
}
