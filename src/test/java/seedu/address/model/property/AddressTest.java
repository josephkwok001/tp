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

    /**
     * Returns empty string when input is null.
     */
    @Test
    public void canonicalLoose_null_returnsEmpty() {
        assertEquals("", Address.canonicalLoose(null));
    }

    /**
     * Normalizes case, removes punctuation, collapses spaces, and trims ends.
     */
    @Test
    public void canonicalLoose_mixedCasePunctWhitespace_normalizes() {
        String in = "  #12-34,  Clementi  Ave 2!!  ";
        String out = Address.canonicalLoose(in);
        assertEquals("1234 clementi ave 2", out);
    }

    /**
     * Preserves letters and numbers while removing non-alnum separators.
     */
    @Test
    public void canonicalLoose_lettersNumbersPreserved_separatorsRemoved() {
        String in = "Blk 10A@Jurong-West";
        String out = Address.canonicalLoose(in);
        assertEquals("blk 10ajurongwest", out);
    }

}
