package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FixInvalidCommand;
import seedu.address.logic.commands.ListInvalidCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.storage.Storage;

/**
 * Tests AddressBookParser for feature commands and storage guards.
 */
public class AddressBookParserFeatureTest {

    @Test
    void parse_export_keptFromMaster() throws Exception {
        EstateSearchParser p = new EstateSearchParser();
        assertThrows(ParseException.class, () -> p.parseCommand("export out/data.json"));
    }

    @Test
    void parse_fixInvalid_guarded() {
        EstateSearchParser p = new EstateSearchParser(null);
        assertThrows(ParseException.class, () ->
                p.parseCommand("fix-invalid i/1 n/Alice p/999 e/a@b.com a/addr l/HDB"));
    }

    @Test
    void parse_listInvalid_guarded() {
        EstateSearchParser p = new EstateSearchParser(null);
        assertThrows(ParseException.class, () -> p.parseCommand("list-invalid"));
    }

    @Test
    void parse_fixInvalid_okWithStorage() throws Exception {
        Storage fake = new StorageStubReportOnly();
        EstateSearchParser p = new EstateSearchParser(fake);
        assertTrue(p.parseCommand("fix-invalid i/1 n/Alice p/999 e/a@b.com a/addr l/HDB") instanceof FixInvalidCommand);
    }

    @Test
    void parse_listInvalid_okWithStorage() throws Exception {
        Storage fake = new StorageStubReportOnly();
        EstateSearchParser p = new EstateSearchParser(fake);
        assertTrue(p.parseCommand("list-invalid") instanceof ListInvalidCommand);
    }

    @Test
    void parseFixInvalid_requiresStorage_guarded() {
        EstateSearchParser p = new EstateSearchParser();
        assertThrows(ParseException.class, () -> p.parseCommand("fix-invalid i/1"));
    }

    @Test
    void parseListInvalid_requiresStorage_guarded() {
        EstateSearchParser p = new EstateSearchParser();
        assertThrows(ParseException.class, () -> p.parseCommand("list-invalid"));
    }
}
