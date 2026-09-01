package com.safiap.techchallengeoficinamecanica.modules.notifications.domain.value_objects;

import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailMessageTest {

    private static final UUID SERVICE_ORDER_ID = UUID.fromString("3f2504e0-4f89-11d3-9a0c-0305e82c3301");
    private static final NotificationRecipient RECIPIENT =
            new NotificationRecipient("João Silva", "joao@email.com");

    @Test
    @DisplayName("builds the status change email with recipient, short id and vehicle")
    void buildsStatusChangeEmail() {
        EmailMessage message = EmailMessage.serviceOrderStatusChanged(
                RECIPIENT, SERVICE_ORDER_ID, "Honda Civic (ABC1D23)", ServiceOrderStatus.RECEIVED);

        assertThat(message.to()).isEqualTo("joao@email.com");
        assertThat(message.subject()).isEqualTo("Ordem de serviço #3f2504e0 — recebida");
        assertThat(message.body())
                .contains("Olá, João Silva!")
                .contains("Sua ordem de serviço foi recebida.")
                .contains("Ordem de serviço: #3f2504e0")
                .contains("Veículo: Honda Civic (ABC1D23)");
    }

    @ParameterizedTest
    @CsvSource({
            "RECEIVED,          recebida,                          Sua ordem de serviço foi recebida.",
            "IN_DIAGNOSIS,      em diagnóstico,                    Iniciamos o diagnóstico do seu veículo.",
            "AWAITING_APPROVAL, orçamento aguardando aprovação,    Seu orçamento está pronto e aguarda aprovação.",
            "IN_EXECUTION,      em execução,                       O serviço no seu veículo foi iniciado.",
            "FINALIZED,         concluída,                         O serviço foi concluído e seu veículo está pronto.",
            "DELIVERED,         veículo entregue,                  Seu veículo foi entregue. Obrigado pela preferência!"
    })
    @DisplayName("uses a specific subject and message for each status")
    void usesSpecificTextPerStatus(ServiceOrderStatus status, String subjectSuffix, String expectedMessage) {
        EmailMessage message = EmailMessage.serviceOrderStatusChanged(
                RECIPIENT, SERVICE_ORDER_ID, "Honda Civic (ABC1D23)", status);

        assertThat(message.subject()).isEqualTo("Ordem de serviço #3f2504e0 — " + subjectSuffix);
        assertThat(message.body()).contains(expectedMessage);
    }

    @ParameterizedTest
    @EnumSource(ServiceOrderStatus.class)
    @DisplayName("never produces a blank subject or body for any status")
    void neverProducesBlankContent(ServiceOrderStatus status) {
        EmailMessage message = EmailMessage.serviceOrderStatusChanged(
                RECIPIENT, SERVICE_ORDER_ID, "não informado", status);

        assertThat(message.subject()).isNotBlank();
        assertThat(message.body()).isNotBlank();
    }

    @Test
    @DisplayName("rejects blank recipient, subject or body")
    void rejectsBlankFields() {
        assertThatThrownBy(() -> new EmailMessage(" ", "assunto", "corpo", false))
                .isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> new EmailMessage("joao@email.com", null, "corpo", false))
                .isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> new EmailMessage("joao@email.com", "assunto", "", false))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("rejects a null service order id")
    void rejectsNullServiceOrderId() {
        assertThatThrownBy(() -> EmailMessage.serviceOrderStatusChanged(
                RECIPIENT, null, "Honda Civic (ABC1D23)", ServiceOrderStatus.RECEIVED))
                .isInstanceOf(DomainException.class);
    }
}
