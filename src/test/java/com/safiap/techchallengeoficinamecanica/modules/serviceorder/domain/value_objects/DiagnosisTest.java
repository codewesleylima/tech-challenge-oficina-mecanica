package com.safiap.techchallengeoficinamecanica.modules.serviceorder.domain.value_objects;

import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DiagnosisTest {

    @Test
    void acceptsAndTrimsValidDescription() {
        Diagnosis diagnosis = new Diagnosis("  Pastilhas gastas  ");
        assertThat(diagnosis.value()).isEqualTo("Pastilhas gastas");
    }

    @Test
    void rejectsNull() {
        assertThatThrownBy(() -> new Diagnosis(null)).isInstanceOf(DomainException.class);
    }

    @Test
    void rejectsBlank() {
        assertThatThrownBy(() -> new Diagnosis("   ")).isInstanceOf(DomainException.class);
    }
}
