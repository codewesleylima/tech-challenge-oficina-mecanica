package com.safiap.techchallengeoficinamecanica.modules.notifications.application.handlers;

import com.safiap.techchallengeoficinamecanica.modules.notifications.application.use_cases.SendServiceOrderStatusNotificationUseCase;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.events.ServiceOrderStatusChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ServiceOrderStatusChangedNotificationHandler {

    private static final Logger log = LoggerFactory.getLogger(ServiceOrderStatusChangedNotificationHandler.class);

    private final SendServiceOrderStatusNotificationUseCase sendServiceOrderStatusNotificationUseCase;

    public ServiceOrderStatusChangedNotificationHandler(
            SendServiceOrderStatusNotificationUseCase sendServiceOrderStatusNotificationUseCase) {
        this.sendServiceOrderStatusNotificationUseCase = sendServiceOrderStatusNotificationUseCase;
    }

    @Async("notificationExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(ServiceOrderStatusChangedEvent event) {
        try {
            sendServiceOrderStatusNotificationUseCase.execute(event);
        } catch (Exception e) {
            // A ordem de serviço já foi commitada: uma falha de envio nunca pode escapar daqui.
            log.error("Failed to notify status change of service order {} ({} -> {}): {}",
                    event.serviceOrderId(), event.previousStatus(), event.newStatus(), e.getMessage(), e);
        }
    }
}
