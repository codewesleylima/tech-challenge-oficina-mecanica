package com.safiap.techchallengeoficinamecanica.modules.notifications.application.ports;

import com.safiap.techchallengeoficinamecanica.modules.notifications.domain.value_objects.EmailMessage;

public interface EmailSenderPort {
    void send(EmailMessage message);
}
