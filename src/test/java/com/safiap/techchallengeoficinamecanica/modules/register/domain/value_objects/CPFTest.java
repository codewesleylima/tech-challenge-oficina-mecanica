package com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects;

import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CPFTest {

    @Test
    @DisplayName("teste aceita CPF com onze dígitos")
    void acceptsElevenDigits() {
        CPF cpf = new CPF("12345678900");
        assertThat(cpf.cpf()).isEqualTo("12345678900");
    }

    @Test
    @DisplayName("teste rejeita CPF nulo")
    void rejectsNull() {
        assertThatThrownBy(() -> new CPF(null)).isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("teste rejeita CPF com tamanho inválido ou com pontuação")
    void rejectsWrongLength() {
        assertThatThrownBy(() -> new CPF("123")).isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> new CPF("123.456.789-00")).isInstanceOf(DomainException.class);
    }
}
