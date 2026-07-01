package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects;

import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DiagnosisTest {

    @Test
    @DisplayName("accepts a valid description and trims surrounding spaces")
    void acceptsAndTrimsValidDescription() {
        Diagnosis diagnosis = new Diagnosis("  Pastilhas gastas  ");
        assertThat(diagnosis.value()).isEqualTo("Pastilhas gastas");
    }

    @Test
    @DisplayName("rejects a null diagnosis")
    void rejectsNull() {
        assertThatThrownBy(() -> new Diagnosis(null)).isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("rejects a blank diagnosis")
    void rejectsBlank() {
        assertThatThrownBy(() -> new Diagnosis("   ")).isInstanceOf(DomainException.class);
    }
}
