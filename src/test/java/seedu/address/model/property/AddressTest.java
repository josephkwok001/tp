package seedu.address.model.property;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class AddressTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Address(null));
    }

    @Test
    public void equals_sameValue_returnsTrue() {
        Address a1 = new Address("123 Example St");
        Address a2 = new Address("123 Example St");
        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }
}
