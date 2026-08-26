package com.safiap.techchallengeoficinamecanica.modules.notifications.domain.value_objects;

import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NotificationRecipientTest {

    @Test
    @DisplayName("trims name and email")
    void trimsFields() {
        NotificationRecipient recipient = new NotificationRecipient("  João Silva  ", " joao@email.com ");

        assertThat(recipient.name()).isEqualTo("João Silva");
        assertThat(recipient.email()).isEqualTo("joao@email.com");
    }

    @Test
    @DisplayName("rejects blank name or email")
    void rejectsBlankFields() {
        assertThatThrownBy(() -> new NotificationRecipient(" ", "joao@email.com"))
                .isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> new NotificationRecipient("João Silva", null))
                .isInstanceOf(DomainException.class);
    }
}
