package com.safiap.techchallengeoficinamecanica.modules.notifications.application.ports;

import com.safiap.techchallengeoficinamecanica.modules.notifications.domain.value_objects.NotificationRecipient;

import java.util.Optional;
import java.util.UUID;

public interface NotificationRecipientPort {

    Optional<NotificationRecipient> findRecipient(UUID customerId);

    Optional<String> findVehicleLabel(UUID vehicleId);
}
