package com.safiap.techchallengeoficinamecanica.modules.notifications.application.handlers;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.events.ServiceOrderStatusChangedEvent;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ServiceOrderStatusChangedNotificationHandler {

    private static final Logger log = LoggerFactory.getLogger(ServiceOrderStatusChangedNotificationHandler.class);

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(ServiceOrderStatusChangedEvent event) {
        log.info("Service order {} changed status from {} to {} for customer {} (vehicle {}). {}",
                event.serviceOrderId(), event.previousStatus(), event.newStatus(),
                event.customerId(), event.vehicleId(), messageFor(event.newStatus()));
    }

    private String messageFor(ServiceOrderStatus status) {
        return switch (status) {
            case RECEIVED -> "Sua ordem de serviço foi recebida.";
            case IN_DIAGNOSIS -> "Iniciamos o diagnóstico do seu veículo.";
            case AWAITING_APPROVAL -> "Seu orçamento está pronto e aguarda aprovação.";
            case IN_EXECUTION -> "O serviço no seu veículo foi iniciado.";
            case FINALIZED -> "O serviço foi concluído e seu veículo está pronto.";
            case DELIVERED -> "Seu veículo foi entregue. Obrigado pela preferência!";
        };
    }
}
