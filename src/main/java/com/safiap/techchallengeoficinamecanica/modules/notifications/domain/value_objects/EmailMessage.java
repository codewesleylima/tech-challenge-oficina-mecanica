package com.safiap.techchallengeoficinamecanica.modules.notifications.domain.value_objects;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;

import java.util.UUID;

public record EmailMessage(String to, String subject, String body, boolean html) {

    private static final String APPROVE_URL = "http://localhost:8080/service-orders/%s/budget/approve";
    private static final String REJECT_URL = "http://localhost:8080/service-orders/%s/reject-budget";
    private static final String GET_BUDGET_URL = "http://localhost:8080/service-orders/%s/budget";

    public EmailMessage {
        if (to == null || to.isBlank()) {
            throw new DomainException("Email recipient cannot be null or empty.");
        }

        if (subject == null || subject.isBlank()) {
            throw new DomainException("Email subject cannot be null or empty.");
        }

        if (body == null || body.isBlank()) {
            throw new DomainException("Email body cannot be null or empty.");
        }
    }

    public static EmailMessage serviceOrderStatusChanged(NotificationRecipient recipient,
                                                         UUID serviceOrderId,
                                                         String vehicleLabel,
                                                         ServiceOrderStatus status) {
        String shortId = shortId(serviceOrderId);
        String subject = "Ordem de serviço #%s — %s".formatted(shortId, titleFor(status));

        boolean awaitingApproval = status == ServiceOrderStatus.AWAITING_APPROVAL;

        String body = awaitingApproval
                ? approvalBody(recipient, serviceOrderId, shortId, vehicleLabel)
                : plainBody(recipient, shortId, vehicleLabel, status);

        return new EmailMessage(recipient.email(), subject, body, awaitingApproval);
    }

    private static String plainBody(NotificationRecipient recipient,
                                    String shortId,
                                    String vehicleLabel,
                                    ServiceOrderStatus status) {
        return """
                Olá, %s!

                %s

                Ordem de serviço: #%s
                Veículo: %s

                Esta é uma mensagem automática, não é necessário respondê-la.

                Oficina Mecânica"""
                .formatted(recipient.name(), messageFor(status), shortId, vehicleLabel);
    }

    private static String approvalBody(NotificationRecipient recipient,
                                       UUID serviceOrderId,
                                       String shortId,
                                       String vehicleLabel) {
        String approveUrl = APPROVE_URL.formatted(serviceOrderId);
        String rejectUrl = REJECT_URL.formatted(serviceOrderId);
        String getBudgetUrl = GET_BUDGET_URL.formatted(serviceOrderId);

        return """
                <html>
                  <body>
                    <p>Olá, %s!</p>
                    <p>%s</p>
                    <p>
                      Ordem de serviço: #%s<br>
                      Veículo: %s
                    </p>
                    <p>
                        Confira o orçamento aqui: <a href="%s">Orçamento</a>
                    </p>
                    <p>
                      <a href="%s">Aceitar</a> | <a href="%s">Rejeitar</a>
                    </p>
                    <p>Esta é uma mensagem automática, não é necessário respondê-la.</p>
                    <p>Oficina Mecânica</p>
                  </body>
                </html>
                """.formatted(recipient.name(), messageFor(ServiceOrderStatus.AWAITING_APPROVAL),
                shortId, vehicleLabel, getBudgetUrl, approveUrl, rejectUrl);
    }

    private static String shortId(UUID serviceOrderId) {
        if (serviceOrderId == null) {
            throw new DomainException("Service order id cannot be null.");
        }
        return serviceOrderId.toString().substring(0, 8);
    }

    private static String titleFor(ServiceOrderStatus status) {
        return switch (status) {
            case RECEIVED -> "recebida";
            case IN_DIAGNOSIS -> "em diagnóstico";
            case AWAITING_APPROVAL -> "orçamento aguardando aprovação";
            case CANCELED -> "cancelada";
            case IN_EXECUTION -> "em execução";
            case FINALIZED -> "concluída";
            case DELIVERED -> "veículo entregue";
        };
    }

    private static String messageFor(ServiceOrderStatus status) {
        return switch (status) {
            case RECEIVED -> "Sua ordem de serviço foi recebida.";
            case IN_DIAGNOSIS -> "Iniciamos o diagnóstico do seu veículo.";
            case AWAITING_APPROVAL -> "Seu orçamento está pronto e aguarda aprovação.";
            case CANCELED -> "O orçamento foi recusado e a ordem de serviço foi cancelada.";
            case IN_EXECUTION -> "O serviço no seu veículo foi iniciado.";
            case FINALIZED -> "O serviço foi concluído e seu veículo está pronto.";
            case DELIVERED -> "Seu veículo foi entregue. Obrigado pela preferência!";
        };
    }
}