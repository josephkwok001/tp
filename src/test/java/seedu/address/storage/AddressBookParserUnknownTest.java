package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Covers unknown command branch.
 */
public class AddressBookParserUnknownTest {
    @Test
    void parse_unknown_throws() {
        AddressBookParser p = new AddressBookParser();
        assertThrows(ParseException.class, () -> p.parseCommand("not-a-cmd"));
    }
}
