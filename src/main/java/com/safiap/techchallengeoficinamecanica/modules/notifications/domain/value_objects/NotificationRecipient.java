package com.safiap.techchallengeoficinamecanica.modules.notifications.domain.value_objects;

import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;

public record NotificationRecipient(String name, String email) {

    public NotificationRecipient {
        if (name == null || name.isBlank()) {
            throw new DomainException("Recipient name cannot be null or empty.");
        }

        if (email == null || email.isBlank()) {
            throw new DomainException("Recipient email cannot be null or empty.");
        }

        name = name.trim();
        email = email.trim();
    }
}
