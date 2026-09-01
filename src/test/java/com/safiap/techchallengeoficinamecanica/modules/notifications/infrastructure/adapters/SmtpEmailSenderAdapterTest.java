package com.safiap.techchallengeoficinamecanica.modules.notifications.infrastructure.adapters;

import com.safiap.techchallengeoficinamecanica.modules.notifications.domain.value_objects.EmailMessage;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SmtpEmailSenderAdapterTest {

    private final JavaMailSender mailSender = mock(JavaMailSender.class);
    private final SmtpEmailSenderAdapter adapter =
            new SmtpEmailSenderAdapter(mailSender, "nao-responda@oficina.local");

    /** O adapter monta um MimeMessage; o mock precisa devolver um real para ser preenchido. */
    private MimeMessage givenMimeMessage() {
        MimeMessage mime = new MimeMessage(Session.getInstance(new Properties()));
        when(mailSender.createMimeMessage()).thenReturn(mime);
        return mime;
    }

    @Test
    @DisplayName("maps the domain message to a MimeMessage and sends it")
    void sendsMimeMessage() throws Exception {
        givenMimeMessage();

        adapter.send(new EmailMessage("joao@email.com", "Ordem de serviço #3f2504e0 — recebida", "Olá, João!", false));

        ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender).send(captor.capture());

        MimeMessage mail = captor.getValue();
        assertThat(mail.getFrom()[0].toString()).isEqualTo("nao-responda@oficina.local");
        assertThat(mail.getAllRecipients()[0].toString()).isEqualTo("joao@email.com");
        assertThat(mail.getSubject()).isEqualTo("Ordem de serviço #3f2504e0 — recebida");
        assertThat(mail.getContent().toString()).contains("Olá, João!");
    }

    @Test
    @DisplayName("sends the approval message as HTML so the links are clickable")
    void sendsHtmlMessage() throws Exception {
        givenMimeMessage();

        adapter.send(new EmailMessage("joao@email.com", "Aprovação", "<html><body><a href=\"x\">Aceitar</a></body></html>", true));

        ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender).send(captor.capture());

        MimeMessage mail = captor.getValue();
        mail.saveChanges();   // sem isto o header Content-Type ainda nao foi escrito
        assertThat(mail.getContentType()).contains("text/html");
    }

    @Test
    @DisplayName("wraps a transport failure instead of leaking the mail exception")
    void wrapsTransportFailure() {
        givenMimeMessage();
        doThrow(new MailSendException("smtp down")).when(mailSender).send(any(MimeMessage.class));

        assertThatThrownBy(() -> adapter.send(
                new EmailMessage("joao@email.com", "Assunto", "Corpo", false)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("joao@email.com");
    }
}
