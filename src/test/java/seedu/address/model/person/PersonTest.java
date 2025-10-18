package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.Tag;

public class PersonTest {

    private static Person person(String n, String p, String e, String a, String l, Set<Tag> tags) {
        return new Person(new Name(n), new Phone(p), new Email(e), new Address(a), new Listing(l), tags);
    }

    @Test
    public void constructor_nullName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new Person(null, new Phone("91234567"), new Email("a@b.com"),
                        new Address("addr"), new Listing("HDB"), Set.of()));
    }

    @Test
    public void getters_roundTrip() {
        Person p = person("Alice Bob", "91234567", "alice@example.com", "1 Main St", "HDB", Set.of(new Tag("x")));
        assertEquals("Alice Bob", p.getName().fullName);
        assertEquals("91234567", p.getPhone().value);
        assertEquals("alice@example.com", p.getEmail().value);
        assertEquals("1 Main St", p.getAddress().value);
        assertEquals("HDB", p.getListing().value);
        assertNotNull(p.getTags());
    }

    @Test
    public void getTags_isUnmodifiable() {
        Person p = person("Alice Bob", "91234567", "alice@example.com", "1 Main St", "HDB", Set.of(new Tag("x")));
        assertThrows(UnsupportedOperationException.class, () -> p.getTags().add(new Tag("y")));
    }

    @Test
    public void isSamePerson_sameInstance_true() {
        Person p = person("Alice Bob", "91234567", "alice@example.com", "1 Main St", "HDB", Set.of());
        org.junit.jupiter.api.Assertions.assertTrue(p.isSamePerson(p));
    }

    @Test
    public void isSamePerson_sameName_true() {
        Person a = person("Alice Bob", "91234567", "alice@example.com", "1 Main St", "HDB", Set.of());
        Person b = person("Alice Bob", "99999999", "alice2@example.com", "Other", "Condo", Set.of(new Tag("z")));
        org.junit.jupiter.api.Assertions.assertTrue(a.isSamePerson(b));
    }

    @Test
    public void isSamePerson_differentName_false() {
        Person a = person("Alice Bob", "91234567", "alice@example.com", "1 Main St", "HDB", Set.of());
        Person b = person("Charlie Tan", "91234567", "alice@example.com", "1 Main St", "HDB", Set.of());
        org.junit.jupiter.api.Assertions.assertFalse(a.isSamePerson(b));
    }

    @Test
    public void equalsAndHashCodeContract() {
        Person a = person("Alice Bob", "91234567", "alice@example.com", "1 Main St", "HDB", Set.of(new Tag("x")));
        Person b = person("Alice Bob", "91234567", "alice@example.com", "1 Main St", "HDB", Set.of(new Tag("x")));
        Person c = person("Alice Bob", "93334444", "alice@example.com", "1 Main St", "HDB", Set.of(new Tag("x")));

        org.junit.jupiter.api.Assertions.assertTrue(a.equals(a));
        org.junit.jupiter.api.Assertions.assertFalse(a.equals(null));
        org.junit.jupiter.api.Assertions.assertFalse(a.equals("not a person"));

        org.junit.jupiter.api.Assertions.assertTrue(a.equals(b));
        org.junit.jupiter.api.Assertions.assertEquals(a.hashCode(), b.hashCode());

        org.junit.jupiter.api.Assertions.assertFalse(a.equals(c));
    }

    @Test
    public void toString_containsCoreFields() {
        Person p = person("Alice Bob", "91234567", "alice@example.com", "1 Main St", "HDB", Set.of(new Tag("x")));
        String s = p.toString();
        org.junit.jupiter.api.Assertions.assertTrue(s.contains("Alice Bob"));
        org.junit.jupiter.api.Assertions.assertTrue(s.contains("91234567"));
        org.junit.jupiter.api.Assertions.assertTrue(s.contains("alice@example.com"));
        org.junit.jupiter.api.Assertions.assertTrue(s.contains("1 Main St"));
        org.junit.jupiter.api.Assertions.assertTrue(s.contains("HDB"));
    }

    @Test
    public void isFullyValid_validFields_true() {
        Person p = person("Alice Bob", "91234567", "alice@example.com", "1 Main St", "HDB", Set.of());
        org.junit.jupiter.api.Assertions.assertTrue(p.isFullyValid());
    }
}
