package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTagName));
    }

    @Test
    public void isValidTagName_null_throwsNullPointerException() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));
    }

    @Test
    public void isValidTagName_invalidTagName_returnsFalse() {
        // null tag name
        assertFalse(Tag.isValidTagName("S P A 1 E"));
    }

    @Test
    public void isValidTagName_tooLongTagName_returnsFalse() {
        // null tag name
        assertFalse(Tag.isValidTagName("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
    }

    @Test
    public void isValidTagName_valid_returnsTrue() {
        // null tag name
        assertTrue(Tag.isValidTagName("4roomFlat"));
    }

}
