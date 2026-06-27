package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects;

import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DiagnosisTest {

    @Test
    @DisplayName("teste aceita descrição válida e remove espaços nas pontas")
    void acceptsAndTrimsValidDescription() {
        Diagnosis diagnosis = new Diagnosis("  Pastilhas gastas  ");
        assertThat(diagnosis.value()).isEqualTo("Pastilhas gastas");
    }

    @Test
    @DisplayName("teste rejeita diagnóstico nulo")
    void rejectsNull() {
        assertThatThrownBy(() -> new Diagnosis(null)).isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("teste rejeita diagnóstico em branco")
    void rejectsBlank() {
        assertThatThrownBy(() -> new Diagnosis("   ")).isInstanceOf(DomainException.class);
    }
}
