package com.safiap.techchallengeoficinamecanica.modules.shared.common;

import java.time.Instant;

public interface DomainEvent {
    Instant occurredOn();
}
