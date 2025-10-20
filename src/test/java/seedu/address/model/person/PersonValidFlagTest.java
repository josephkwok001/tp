package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.Tag;

/**
 * Tests Person.isFullyValid with valid inputs.
 */
public class PersonValidFlagTest {

    private static Person mk(String n, String p, String e, String a, String l) {
        return new Person(new Name(n), new Phone(p), new Email(e), new Address(a),
                java.util.Set.of(new Tag("t")));
    }

    @Test
    void isFullyValid_trueWhenAllPass() {
        assertTrue(mk("Alice Bob", "999", "a@b.com", "Somewhere", "HDB").isFullyValid());
    }
}
