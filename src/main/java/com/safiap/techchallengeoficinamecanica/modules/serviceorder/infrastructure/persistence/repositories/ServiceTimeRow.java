package com.safiap.techchallengeoficinamecanica.modules.serviceorder.infrastructure.persistence.repositories;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ServiceTimeRow {
    UUID getServiceId();
    LocalDateTime getStartedAt();
    LocalDateTime getCompletedAt();
}
