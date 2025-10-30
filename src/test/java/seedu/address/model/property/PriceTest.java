package seedu.address.model.property;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class PriceTest {

    @Test
    public void constructor_negativeValue_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Price(-1));
    }

    @Test
    public void equals_sameValue_returnsTrue() {
        Price p1 = new Price(500000);
        Price p2 = new Price(500000);
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void equals_differentValue_returnsFalse() {
        Price p1 = new Price(500000);
        Price p2 = new Price(600000);
        assertThrows(AssertionError.class, () -> assertEquals(p1, p2));
    }

    @Test
    public void isSamePrice() {
        Price p1 = new Price(750000);
        assertEquals(p1, p1);
    }
}
