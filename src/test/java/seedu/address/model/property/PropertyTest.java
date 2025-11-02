package seedu.address.model.property;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PropertyTest {

    @Test
    public void constructor_nullFields_throwsNullPointerException() {
        Address address = new Address("123 Example St");
        Price price = new Price(100000);
        PropertyName name = new PropertyName("Name");

        assertThrows(NullPointerException.class, () -> new Property(null, price, name));
        assertThrows(NullPointerException.class, () -> new Property(address, null, name));
        assertThrows(NullPointerException.class, () -> new Property(address, price, null));
    }

    @Test
    public void isSameProperty_sameNameDifferentOtherFields_returnsTrue() {
        PropertyName name = new PropertyName("CommonName");
        Property p1 = new Property(new Address("A1"), new Price(100), name);
        Property p2 = new Property(new Address("A2"), new Price(200), new PropertyName("CommonName"));
        assertTrue(p1.isSameProperty(p2));
    }

    @Test
    public void isSameProperty_differentName_returnsFalse() {
        Property p1 = new Property(new Address("A1"), new Price(100), new PropertyName("N1"));
        Property p2 = new Property(new Address("A2"), new Price(100), new PropertyName("N2"));
        assertFalse(p1.isSameProperty(p2));
    }

    @Test
    public void equals_sameFields_returnsTrue() {
        Address addr = new Address("1 Example Rd");
        Price pr = new Price(12345);
        PropertyName nm = new PropertyName("PName");
        Property p1 = new Property(addr, pr, nm);
        Property p2 = new Property(new Address("1 Example Rd"), new Price(12345), new PropertyName("PName"));
        assertEquals(p1, p2);
    }

    @Test
    public void toString_containsFields() {
        Property p = new Property(new Address("Addr"), new Price(10), new PropertyName("PN"));
        String s = p.toString();
        assertTrue(s.contains("Addr"));
        assertTrue(s.contains("10"));
        assertTrue(s.contains("PN"));
    }
}
