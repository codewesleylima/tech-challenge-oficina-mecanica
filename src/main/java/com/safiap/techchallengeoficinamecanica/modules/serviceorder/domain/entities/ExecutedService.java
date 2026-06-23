package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.entities;

import com.safiap.techchallengeoficinamecanica.modules.shared.common.Entity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class ExecutedService extends Entity {

    private UUID executedServiceId;
    private String description;
    private LocalDateTime executedAt;

    private ExecutedService() {}

    private ExecutedService(UUID executedServiceId, String description, LocalDateTime executedAt) {
        this.executedServiceId = executedServiceId;
        this.description = description;
        this.executedAt = executedAt;
    }

    public static ExecutedService register(String description) {
        Objects.requireNonNull(description, "description is null");
        return new ExecutedService(UUID.randomUUID(), description, LocalDateTime.now());
    }

    public static ExecutedService build(UUID executedServiceId, String description, LocalDateTime executedAt) {
        Objects.requireNonNull(executedServiceId, "executedServiceId is null");
        return new ExecutedService(executedServiceId, description, executedAt);
    }

    public UUID getExecutedServiceId() { return executedServiceId; }
    public String getDescription() { return description; }
    public LocalDateTime getExecutedAt() { return executedAt; }
}
