package com.safiap.techchallengeoficinamecanica.modules.inventory.domain.entities;

import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.value_objects.Money;
import com.safiap.techchallengeoficinamecanica.modules.inventory.domain.value_objects.Quantity;
import com.safiap.techchallengeoficinamecanica.modules.shared.exceptions.DomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PartTest {

    private static Money price() {
        return new Money(new BigDecimal("25.50"));
    }

    private static Quantity qty(int value) {
        return new Quantity(value);
    }

    @Test
    void createPartShouldGenerateIdAndTrimTextFields() {
        Part part = Part.createPart("  Oil Filter  ", "  premium  ", price(), qty(10));

        assertThat(part.getId()).isNotNull();
        assertThat(part.getName()).isEqualTo("Oil Filter");
        assertThat(part.getDescription()).isEqualTo("premium");
        assertThat(part.getQuantity().value()).isEqualTo(10);
        assertThat(part.getPrice().amount()).isEqualByComparingTo("25.50");
    }
    @Test
    void createPartShouldAllowNullDescription() {
        Part part = Part.createPart("Bolt", null, price(), qty(1));

        assertThat(part.getDescription()).isNull();
    }
    @Test
    void buildPartShouldKeepProvidedId() {
        UUID id = UUID.randomUUID();

        Part part = Part.buildPart(id, "Bolt", "desc", price(), qty(1));

        assertThat(part.getId()).isEqualTo(id);
    }
    @Test
    void shouldRejectBlankName() {
        assertThatThrownBy(() -> Part.createPart("   ", "desc", price(), qty(1)))
                .isInstanceOf(DomainException.class);
    }
    @Test
    void shouldRejectNullPrice() {
        assertThatThrownBy(() -> Part.createPart("Bolt", "desc", null, qty(1)))
                .isInstanceOf(DomainException.class);
    }
    @Test
    void shouldRejectZeroPrice() {
        assertThatThrownBy(() -> Part.createPart("Bolt", "desc", new Money(BigDecimal.ZERO), qty(1)))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("price");
    }
    @Test
    void shouldRejectNullQuantity() {
        assertThatThrownBy(() -> Part.createPart("Bolt", "desc", price(), null))
                .isInstanceOf(DomainException.class);
    }
    @Test
    void shouldRejectDescriptionLongerThanMax() {
        String longDescription = "a".repeat(151);
        assertThatThrownBy(() -> Part.createPart("Bolt", longDescription, price(), qty(1)))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("150");
    }
    @Test
    void increaseStockShouldAddToQuantity() {
        Part part = Part.createPart("Bolt", "desc", price(), qty(10));
        part.increaseStock(5);
        assertThat(part.getQuantity().value()).isEqualTo(15);
    }

    @Test
    void decreaseStockShouldSubtractFromQuantity() {
        Part part = Part.createPart("Bolt", "desc", price(), qty(10));

        part.decreaseStock(4);

        assertThat(part.getQuantity().value()).isEqualTo(6);
    }

    @Test
    void decreaseStockShouldFailWhenInsufficient() {
        Part part = Part.createPart("Bolt", "desc", price(), qty(3));

        assertThatThrownBy(() -> part.decreaseStock(10))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("insufficient");
    }

    @Test
    void updateShouldChangeFieldsButKeepIdAndQuantity() {
        Part part = Part.createPart("Bolt", "desc", price(), qty(10));
        UUID id = part.getId();

        part.update("  Nut  ", "  new desc  ", new Money(new BigDecimal("99.90")));

        assertThat(part.getName()).isEqualTo("Nut");
        assertThat(part.getDescription()).isEqualTo("new desc");
        assertThat(part.getPrice().amount()).isEqualByComparingTo("99.90");
        assertThat(part.getQuantity().value()).isEqualTo(10);
        assertThat(part.getId()).isEqualTo(id);
    }
    @Test
    void updateShouldRejectBlankName() {
        Part part = Part.createPart("Bolt", "desc", price(), qty(10));
        assertThatThrownBy(() -> part.update("   ", "desc", price()))
                .isInstanceOf(DomainException.class);
    }
}
