package com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects;

import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {

    @Test
    @DisplayName("normalizes the email to lowercase")
    void normalizesToLowercase() {
        Email email = new Email("Joao@Email.COM");
        assertThat(email.value()).isEqualTo("joao@email.com");
    }

    @Test
    @DisplayName("rejects an email with an invalid format")
    void rejectsInvalidFormat() {
        assertThatThrownBy(() -> new Email("joao@")).isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> new Email("joao")).isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("rejects a blank email")
    void rejectsBlank() {
        assertThatThrownBy(() -> new Email("  ")).isInstanceOf(DomainException.class);
    }
}
