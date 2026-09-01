package com.safiap.techchallengeoficinamecanica.modules.notifications.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.notifications.application.ports.EmailSenderPort;
import com.safiap.techchallengeoficinamecanica.modules.notifications.application.ports.NotificationRecipientPort;
import com.safiap.techchallengeoficinamecanica.modules.notifications.domain.value_objects.EmailMessage;
import com.safiap.techchallengeoficinamecanica.modules.notifications.domain.value_objects.NotificationRecipient;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.events.ServiceOrderStatusChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SendServiceOrderStatusNotificationUseCase {

    private static final Logger log = LoggerFactory.getLogger(SendServiceOrderStatusNotificationUseCase.class);

    private static final String UNKNOWN_VEHICLE = "não informado";

    private final NotificationRecipientPort notificationRecipientPort;
    private final EmailSenderPort emailSenderPort;
    private final String publicBaseUrl;

    public SendServiceOrderStatusNotificationUseCase(NotificationRecipientPort notificationRecipientPort,
                                                     EmailSenderPort emailSenderPort,
                                                     @Value("${app.public-url}") String publicBaseUrl) {
        this.notificationRecipientPort = notificationRecipientPort;
        this.emailSenderPort = emailSenderPort;
        this.publicBaseUrl = publicBaseUrl;
    }

    public void execute(ServiceOrderStatusChangedEvent event) {
        Optional<NotificationRecipient> recipient = notificationRecipientPort.findRecipient(event.customerId());

        if (recipient.isEmpty()) {
            log.warn("No recipient found for customer {}; skipping notification of service order {}.",
                    event.customerId(), event.serviceOrderId());
            return;
        }

        String vehicleLabel = notificationRecipientPort.findVehicleLabel(event.vehicleId())
                .orElse(UNKNOWN_VEHICLE);

        EmailMessage message = EmailMessage.serviceOrderStatusChanged(
                recipient.get(), event.serviceOrderId(), vehicleLabel, event.newStatus(), publicBaseUrl);

        emailSenderPort.send(message);

        log.info("Notification sent for service order {} ({} -> {}).",
                event.serviceOrderId(), event.previousStatus(), event.newStatus());
    }
}
