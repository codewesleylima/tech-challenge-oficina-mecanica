package com.safiap.techchallengeoficinamecanica.modules.notifications.infrastructure.adapters;

import com.safiap.techchallengeoficinamecanica.modules.notifications.application.ports.EmailSenderPort;
import com.safiap.techchallengeoficinamecanica.modules.notifications.domain.value_objects.EmailMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Fallback usado quando notifications.email.enabled=false (testes ou execução sem SMTP):
 * registra o e-mail que seria enviado em vez de sair pela rede.
 */
@Component
@ConditionalOnProperty(name = "notifications.email.enabled", havingValue = "false")
public class LoggingEmailSenderAdapter implements EmailSenderPort {

    private static final Logger log = LoggerFactory.getLogger(LoggingEmailSenderAdapter.class);

    @Override
    public void send(EmailMessage message) {
        log.info("[email disabled] to={} subject={} body={}",
                message.to(), message.subject(), message.body());
    }
}
