package com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects;

import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CPFTest {

    @Test
    void acceptsElevenDigits() {
        CPF cpf = new CPF("12345678900");
        assertThat(cpf.cpf()).isEqualTo("12345678900");
    }

    @Test
    void rejectsNull() {
        assertThatThrownBy(() -> new CPF(null)).isInstanceOf(DomainException.class);
    }

    @Test
    void rejectsWrongLength() {
        assertThatThrownBy(() -> new CPF("123")).isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> new CPF("123.456.789-00")).isInstanceOf(DomainException.class);
    }
}
