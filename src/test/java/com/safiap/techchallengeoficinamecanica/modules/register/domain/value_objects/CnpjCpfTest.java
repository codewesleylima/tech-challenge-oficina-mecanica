package com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects;

import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CnpjCpfTest {

    @Test
    @DisplayName("aceita CPF válido e normaliza para apenas dígitos")
    void acceptsValidCpf() {
        CnpjCpf document = new CnpjCpf("111.444.777-35");
        assertThat(document.value()).isEqualTo("11144477735");
    }

    @Test
    @DisplayName("aceita CNPJ válido e normaliza para apenas dígitos")
    void acceptsValidCnpj() {
        CnpjCpf document = new CnpjCpf("11.222.333/0001-81");
        assertThat(document.value()).isEqualTo("11222333000181");
    }

    @Test
    @DisplayName("rejeita documento nulo")
    void rejectsNull() {
        assertThatThrownBy(() -> new CnpjCpf(null)).isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("rejeita CPF com dígito verificador inválido")
    void rejectsCpfWithInvalidCheckDigit() {
        assertThatThrownBy(() -> new CnpjCpf("12345678900")).isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("rejeita CNPJ com dígito verificador inválido")
    void rejectsCnpjWithInvalidCheckDigit() {
        assertThatThrownBy(() -> new CnpjCpf("11222333000100")).isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("rejeita sequência de dígitos repetidos")
    void rejectsRepeatedDigits() {
        assertThatThrownBy(() -> new CnpjCpf("00000000000")).isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> new CnpjCpf("11111111111111")).isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("rejeita documento com quantidade de dígitos inválida")
    void rejectsWrongLength() {
        assertThatThrownBy(() -> new CnpjCpf("123")).isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> new CnpjCpf("123456789012")).isInstanceOf(DomainException.class);
    }
}
