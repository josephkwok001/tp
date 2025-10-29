package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.property.Address;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyName;

/**
 * Headless tests for PersonCard.renderPropertyTexts (no JavaFX required).
 */
public class PersonCardLogicTest {

    /**
     * Returns empty when there are no properties.
     */
    @Test
    public void renderPropertyTexts_empty_returnsEmptyList() {
        List<String> texts = PersonCard.renderPropertyTexts(List.of());
        assertEquals(List.of(), texts);
    }

    /**
     * Does not append comma and space for a single item.
     */
    @Test
    public void renderPropertyTexts_single_noTrailingComma() {
        Property p = new Property(new Address("A"), new Price(1), new PropertyName("Alpha"));
        List<String> texts = PersonCard.renderPropertyTexts(List.of(p));
        assertEquals(List.of("Alpha"), texts);
    }

    /**
     * Appends ", " only to non-last items.
     */
    @Test
    public void renderPropertyTexts_multiple_commasOnlyBetweenItems() {
        Property a = new Property(new Address("A"), new Price(1), new PropertyName("Alpha"));
        Property b = new Property(new Address("B"), new Price(2), new PropertyName("Beta"));
        Property c = new Property(new Address("C"), new Price(3), new PropertyName("Gamma"));
        List<String> texts = PersonCard.renderPropertyTexts(List.of(a, b, c));
        assertEquals(List.of("Alpha, ", "Beta, ", "Gamma"), texts);
    }
}
