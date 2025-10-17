package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FixInvalidCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.storage.LoadReport;
import seedu.address.storage.Storage;

/**
 * Unit tests for FixInvalidCommandParser.
 */
class FixInvalidCommandParserTest {

    private static final Storage DUMMY_STORAGE = new Storage() {
        @Override
        public LoadReport overwriteRawEntryAtIndex(int i,
                                                   seedu.address.model.person.Person p) {
            return null;
        }

        @Override
        public Optional<seedu.address.model.UserPrefs> readUserPrefs() {
            return Optional.empty();
        }

        @Override
        public void saveUserPrefs(seedu.address.model.ReadOnlyUserPrefs prefs) {}

        @Override
        public Path getUserPrefsFilePath() {
            return Path.of("dummy.json");
        }

        @Override
        public Path getAddressBookFilePath() {
            return Path.of("dummy.json");
        }

        @Override
        public Optional<ReadOnlyAddressBook> readAddressBook() {
            return Optional.empty();
        }

        @Override
        public Optional<ReadOnlyAddressBook> readAddressBook(Path path) {
            return Optional.empty();
        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook ab) {}

        @Override
        public void saveAddressBook(ReadOnlyAddressBook ab, Path path) {}

        @Override
        public LoadReport readAddressBookWithReport() {
            return null;
        }

        @Override
        public LoadReport readAddressBookWithReport(Path path) {
            return null;
        }
    };

    private final FixInvalidCommandParser parser = new FixInvalidCommandParser(DUMMY_STORAGE);

    @Test
    void parse_allPrefixesPresent_success() throws Exception {
        assertTrue(parser.parse(" i/1 n/Alice p/91234567 e/a@b.com a/Blk 1 l/HDB")
                instanceof FixInvalidCommand);
    }

    @Test
    void parse_missingPrefixes_failure() {
        assertThrows(ParseException.class, () ->
                parser.parse(" n/Alice p/91234567 e/a@b.com a/Blk 1"));
        assertThrows(ParseException.class, () -> parser.parse(""));
    }
}
