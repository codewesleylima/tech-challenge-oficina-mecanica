package com.safiap.techchallengeoficinamecanica.modules.inventory.domain.entities;

import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.value_objects.Money;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ServiceTest {

    private static Money price() {
        return new Money(new BigDecimal("120.00"));
    }

    @Test
    void createServiceShouldGenerateIdAndTrimTextFields() {
        Service service = Service.createService("  Oil Change  ", "  full synthetic  ", price());
        assertThat(service.getId()).isNotNull();
        assertThat(service.getName()).isEqualTo("Oil Change");
        assertThat(service.getDescription()).isEqualTo("full synthetic");
        assertThat(service.getPrice().amount()).isEqualByComparingTo("120.00");
    }
    @Test
    void createServiceShouldAllowNullDescription() {
        Service service = Service.createService("Alignment", null, price());
        assertThat(service.getDescription()).isNull();
    }
    @Test
    void shouldRejectBlankName() {
        assertThatThrownBy(() -> Service.createService("   ", "desc", price()))
                .isInstanceOf(DomainException.class);
    }
    @Test
    void shouldRejectNullPrice() {
        assertThatThrownBy(() -> Service.createService("Alignment", "desc", null))
                .isInstanceOf(DomainException.class);
    }
    @Test
    void shouldRejectZeroPrice() {
        assertThatThrownBy(() -> Service.createService("Alignment", "desc", new Money(BigDecimal.ZERO)))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("price");
    }
    @Test
    void shouldRejectDescriptionLongerThanMax() {
        String longDescription = "a".repeat(151);

        assertThatThrownBy(() -> Service.createService("Alignment", longDescription, price()))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("150");
    }
    @Test
    void updateShouldChangeFieldsButKeepId() {
        Service service = Service.createService("Alignment", "desc", price());
        UUID id = service.getId();
        service.update("  Balancing  ", "  new desc  ", new Money(new BigDecimal("80.00")));
        assertThat(service.getName()).isEqualTo("Balancing");
        assertThat(service.getDescription()).isEqualTo("new desc");
        assertThat(service.getPrice().amount()).isEqualByComparingTo("80.00");
        assertThat(service.getId()).isEqualTo(id);
    }
    @Test
    void updateShouldRejectBlankName() {
        Service service = Service.createService("Alignment", "desc", price());
        assertThatThrownBy(() -> service.update("   ", "desc", price()))
                .isInstanceOf(DomainException.class);
    }
}
