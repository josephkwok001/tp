package seedu.address.model.property;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class PropertyNameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PropertyName(null));
    }

    @Test
    public void equals_sameValue_returnsTrue() {
        PropertyName n1 = new PropertyName("Sunny Villa");
        PropertyName n2 = new PropertyName("Sunny Villa");
        assertEquals(n1, n2);
        assertEquals(n1.hashCode(), n2.hashCode());
    }
}
