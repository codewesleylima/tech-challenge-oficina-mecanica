package com.safiap.techchallengeoficinamecanica.modules.notifications.infrastructure.adapters;

import com.safiap.techchallengeoficinamecanica.modules.notifications.domain.value_objects.EmailMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class SmtpEmailSenderAdapterTest {

    private final JavaMailSender mailSender = mock(JavaMailSender.class);
    private final SmtpEmailSenderAdapter adapter =
            new SmtpEmailSenderAdapter(mailSender, "nao-responda@oficina.local");

    @Test
    @DisplayName("maps the domain message to a SimpleMailMessage and sends it")
    void sendsSimpleMailMessage() {
        adapter.send(new EmailMessage("joao@email.com", "Ordem de serviço #3f2504e0 — recebida", "Olá, João!"));

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage mail = captor.getValue();
        assertThat(mail.getFrom()).isEqualTo("nao-responda@oficina.local");
        assertThat(mail.getTo()).containsExactly("joao@email.com");
        assertThat(mail.getSubject()).isEqualTo("Ordem de serviço #3f2504e0 — recebida");
        assertThat(mail.getText()).isEqualTo("Olá, João!");
    }
}
