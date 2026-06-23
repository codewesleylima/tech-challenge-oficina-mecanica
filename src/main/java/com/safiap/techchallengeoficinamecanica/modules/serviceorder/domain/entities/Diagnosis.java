package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities;

import com.safiap.techchallengeoficinamecanica.modules.shared.common.Entity;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Diagnosis extends Entity {

    private UUID diagnosisId;
    private String notes;
    private LocalDateTime startedAt;
    private LocalDateTime concludedAt;

    private Diagnosis() {}

    private Diagnosis(UUID diagnosisId, String notes, LocalDateTime startedAt, LocalDateTime concludedAt) {
        this.diagnosisId = diagnosisId;
        this.notes = notes;
        this.startedAt = startedAt;
        this.concludedAt = concludedAt;
    }

    public static Diagnosis start() {
        return new Diagnosis(UUID.randomUUID(), null, LocalDateTime.now(), null);
    }

    public static Diagnosis build(UUID diagnosisId, String notes, LocalDateTime startedAt, LocalDateTime concludedAt) {
        Objects.requireNonNull(diagnosisId, "diagnosisId is null");
        Objects.requireNonNull(startedAt, "startedAt is null");
        return new Diagnosis(diagnosisId, notes, startedAt, concludedAt);
    }

    public void conclude(String notes) {
        if (this.concludedAt != null) throw new DomainException("Diagnosis already concluded");
        Objects.requireNonNull(notes, "notes is null");
        this.notes = notes;
        this.concludedAt = LocalDateTime.now();
    }

    public boolean isConcluded() { return concludedAt != null; }

    public UUID getDiagnosisId() { return diagnosisId; }
    public String getNotes() { return notes; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public LocalDateTime getConcludedAt() { return concludedAt; }
}
