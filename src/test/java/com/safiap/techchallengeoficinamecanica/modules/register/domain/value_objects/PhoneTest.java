package com.safiap.techchallengeoficinamecanica.modules.register.domain.value_objects;

import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PhoneTest {

    @Test
    @DisplayName("strips non-numeric characters from the phone")
    void stripsNonDigits() {
        Phone phone = new Phone("(11) 99999-8888");
        assertThat(phone.value()).isEqualTo("11999998888");
    }

    @Test
    @DisplayName("accepts a ten-digit phone")
    void acceptsTenDigits() {
        assertThat(new Phone("1133334444").value()).isEqualTo("1133334444");
    }

    @Test
    @DisplayName("rejects a too-short phone")
    void rejectsTooShort() {
        assertThatThrownBy(() -> new Phone("9999")).isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("rejects a blank phone")
    void rejectsBlank() {
        assertThatThrownBy(() -> new Phone(" ")).isInstanceOf(DomainException.class);
    }
}
