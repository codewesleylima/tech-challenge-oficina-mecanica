package com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects;

import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PhoneTest {

    @Test
    @DisplayName("teste remove caracteres não numéricos do telefone")
    void stripsNonDigits() {
        Phone phone = new Phone("(11) 99999-8888");
        assertThat(phone.value()).isEqualTo("11999998888");
    }

    @Test
    @DisplayName("teste aceita telefone com dez dígitos")
    void acceptsTenDigits() {
        assertThat(new Phone("1133334444").value()).isEqualTo("1133334444");
    }

    @Test
    @DisplayName("teste rejeita telefone curto demais")
    void rejectsTooShort() {
        assertThatThrownBy(() -> new Phone("9999")).isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("teste rejeita telefone em branco")
    void rejectsBlank() {
        assertThatThrownBy(() -> new Phone(" ")).isInstanceOf(DomainException.class);
    }
}
