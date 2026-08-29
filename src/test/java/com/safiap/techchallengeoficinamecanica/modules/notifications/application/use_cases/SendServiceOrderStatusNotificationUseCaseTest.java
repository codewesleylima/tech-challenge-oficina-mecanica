package com.safiap.techchallengeoficinamecanica.modules.notifications.application.use_cases;

import com.safiap.techchallengeoficinamecanica.modules.notifications.application.ports.EmailSenderPort;
import com.safiap.techchallengeoficinamecanica.modules.notifications.application.ports.NotificationRecipientPort;
import com.safiap.techchallengeoficinamecanica.modules.notifications.domain.value_objects.EmailMessage;
import com.safiap.techchallengeoficinamecanica.modules.notifications.domain.value_objects.NotificationRecipient;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.events.ServiceOrderStatusChangedEvent;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SendServiceOrderStatusNotificationUseCaseTest {

    private final NotificationRecipientPort recipientPort = mock(NotificationRecipientPort.class);
    private final EmailSenderPort emailSenderPort = mock(EmailSenderPort.class);
    private final SendServiceOrderStatusNotificationUseCase useCase =
            new SendServiceOrderStatusNotificationUseCase(recipientPort, emailSenderPort);

    private final UUID serviceOrderId = UUID.randomUUID();
    private final UUID customerId = UUID.randomUUID();
    private final UUID vehicleId = UUID.randomUUID();

    @Test
    @DisplayName("sends the email to the customer resolved from the register module")
    void sendsEmailToCustomer() {
        when(recipientPort.findRecipient(customerId))
                .thenReturn(Optional.of(new NotificationRecipient("João Silva", "joao@email.com")));
        when(recipientPort.findVehicleLabel(vehicleId))
                .thenReturn(Optional.of("Honda Civic (ABC1D23)"));

        useCase.execute(event(ServiceOrderStatus.RECEIVED, ServiceOrderStatus.IN_DIAGNOSIS));

        ArgumentCaptor<EmailMessage> captor = ArgumentCaptor.forClass(EmailMessage.class);
        verify(emailSenderPort).send(captor.capture());

        EmailMessage sent = captor.getValue();
        assertThat(sent.to()).isEqualTo("joao@email.com");
        assertThat(sent.subject()).contains("em diagnóstico");
        assertThat(sent.body())
                .contains("João Silva")
                .contains("Iniciamos o diagnóstico do seu veículo.")
                .contains("Honda Civic (ABC1D23)");
    }

    @Test
    @DisplayName("falls back to a placeholder when the vehicle cannot be resolved")
    void fallsBackWhenVehicleIsMissing() {
        when(recipientPort.findRecipient(customerId))
                .thenReturn(Optional.of(new NotificationRecipient("João Silva", "joao@email.com")));
        when(recipientPort.findVehicleLabel(vehicleId)).thenReturn(Optional.empty());

        useCase.execute(event(ServiceOrderStatus.IN_EXECUTION, ServiceOrderStatus.FINALIZED));

        ArgumentCaptor<EmailMessage> captor = ArgumentCaptor.forClass(EmailMessage.class);
        verify(emailSenderPort).send(captor.capture());
        assertThat(captor.getValue().body()).contains("Veículo: não informado");
    }

    @Test
    @DisplayName("skips the notification without failing when the customer is unknown")
    void skipsWhenRecipientIsMissing() {
        when(recipientPort.findRecipient(customerId)).thenReturn(Optional.empty());

        useCase.execute(event(ServiceOrderStatus.RECEIVED, ServiceOrderStatus.IN_DIAGNOSIS));

        verify(emailSenderPort, never()).send(any());
    }

    private ServiceOrderStatusChangedEvent event(ServiceOrderStatus previous, ServiceOrderStatus current) {
        return ServiceOrderStatusChangedEvent.of(serviceOrderId, customerId, vehicleId, previous, current);
    }
}
