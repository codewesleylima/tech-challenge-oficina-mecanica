package com.safiap.techchallengeoficinamecanica.modules.notifications.application.handlers;

import com.safiap.techchallengeoficinamecanica.modules.notifications.application.use_cases.SendServiceOrderStatusNotificationUseCase;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.events.ServiceOrderStatusChangedEvent;
import com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects.ServiceOrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ServiceOrderStatusChangedNotificationHandlerTest {

    private final SendServiceOrderStatusNotificationUseCase useCase =
            mock(SendServiceOrderStatusNotificationUseCase.class);
    private final ServiceOrderStatusChangedNotificationHandler handler =
            new ServiceOrderStatusChangedNotificationHandler(useCase);

    private final ServiceOrderStatusChangedEvent event = ServiceOrderStatusChangedEvent.of(
            UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
            ServiceOrderStatus.RECEIVED, ServiceOrderStatus.IN_DIAGNOSIS);

    @Test
    @DisplayName("delegates the event to the notification use case")
    void delegatesToUseCase() {
        handler.on(event);

        verify(useCase).execute(event);
    }

    @Test
    @DisplayName("swallows send failures so a committed service order is never affected")
    void swallowsSendFailures() {
        doThrow(new RuntimeException("SMTP unavailable")).when(useCase).execute(event);

        assertThatCode(() -> handler.on(event)).doesNotThrowAnyException();
    }
}
