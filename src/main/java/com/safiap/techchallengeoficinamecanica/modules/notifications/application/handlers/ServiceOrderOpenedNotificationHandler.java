package com.safiap.techchallengeoficinamecanica.modules.notifications.application.handlers;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.events.ServiceOrderOpenedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ServiceOrderOpenedNotificationHandler {

    private static final Logger log = LoggerFactory.getLogger(ServiceOrderOpenedNotificationHandler.class);

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(ServiceOrderOpenedEvent event) {
        log.info("Servie order {} opended for the customer {} (vehicle {}). Sending notification...",
                event.serviceOrderId(), event.customerId(), event.vehicleId());
        // TODO: implement notificatio logic in the next step
    }
}
