package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.property.Address;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyName;

/**
 * Headless tests for {@link PersonCard#renderPropertyTexts(java.util.List)}.
 * These tests do not require JavaFX and guarantee coverage on PersonCard.java lines changed.
 */
public class PersonCardLogicTest {

    @Test
    public void renderPropertyTexts_empty_returnsEmptyList() {
        List<String> texts = PersonCard.renderPropertyTexts(List.of());
        assertEquals(List.of(), texts);
    }

    @Test
    public void renderPropertyTexts_single_noTrailingComma() {
        Property p = new Property(new Address("A"), new Price(1), new PropertyName("Alpha"));
        List<String> texts = PersonCard.renderPropertyTexts(List.of(p));
        assertEquals(List.of("Alpha"), texts);
    }

    @Test
    public void renderPropertyTexts_multiple_commaAndSpaceBetweenItemsOnly() {
        Property a = new Property(new Address("A"), new Price(1), new PropertyName("Alpha"));
        Property b = new Property(new Address("B"), new Price(2), new PropertyName("Beta"));
        Property c = new Property(new Address("C"), new Price(3), new PropertyName("Gamma"));

        List<String> texts = PersonCard.renderPropertyTexts(List.of(a, b, c));

        assertEquals(List.of("Alpha, ", "Beta, ", "Gamma"), texts);
        assertEquals("Alpha, ", texts.get(0));
        assertEquals("Beta, ", texts.get(1));
        assertEquals("Gamma", texts.get(2));
    }
}
