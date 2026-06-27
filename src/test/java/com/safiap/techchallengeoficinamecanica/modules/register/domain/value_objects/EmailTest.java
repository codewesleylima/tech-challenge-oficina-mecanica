package com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects;

import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {

    @Test
    @DisplayName("teste normaliza o e-mail para minúsculas")
    void normalizesToLowercase() {
        Email email = new Email("Joao@Email.COM");
        assertThat(email.value()).isEqualTo("joao@email.com");
    }

    @Test
    @DisplayName("teste rejeita e-mail com formato inválido")
    void rejectsInvalidFormat() {
        assertThatThrownBy(() -> new Email("joao@")).isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> new Email("joao")).isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("teste rejeita e-mail em branco")
    void rejectsBlank() {
        assertThatThrownBy(() -> new Email("  ")).isInstanceOf(DomainException.class);
    }
}
