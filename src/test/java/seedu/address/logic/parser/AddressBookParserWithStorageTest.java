package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.logic.commands.ListInvalidCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.storage.LoadReport;
import seedu.address.storage.Storage;

class AddressBookParserWithStorageTest {

    static class StorageStub implements Storage {
        private final LoadReport report =
                new LoadReport(new LoadReport.ModelData(new AddressBook()), List.of());

        @Override
        public Optional<UserPrefs> readUserPrefs() throws DataLoadingException {
            return Optional.empty();
        }

        @Override
        public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) {}

        @Override
        public Path getUserPrefsFilePath() {
            return Path.of("prefs.json");
        }

        @Override
        public Path getAddressBookFilePath() {
            return Path.of("x.json");
        }

        @Override
        public Optional<ReadOnlyAddressBook> readAddressBook() throws DataLoadingException {
            return Optional.empty();
        }

        @Override
        public Optional<ReadOnlyAddressBook> readAddressBook(Path p) throws DataLoadingException {
            return Optional.empty();
        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook addressBook) {}

        @Override
        public void saveAddressBook(ReadOnlyAddressBook addressBook, Path p) {}

        @Override
        public LoadReport readAddressBookWithReport() throws DataLoadingException {
            return report;
        }

        @Override
        public LoadReport readAddressBookWithReport(Path p) throws DataLoadingException {
            return report;
        }

        @Override
        public LoadReport overwriteRawEntryAtIndex(
                int index,
                seedu.address.model.person.Person person)
                throws DataLoadingException, IOException {
            // Return an empty report; tests don’t depend on its contents here
            return new LoadReport(
                    new LoadReport.ModelData(new AddressBook()),
                    List.of()
            );
        }
    }

    @Test
    void listInvalid_withStorage_routes() throws Exception {
        AddressBookParser parser = new AddressBookParser(new StorageStub());
        assertTrue(parser.parseCommand("list-invalid") instanceof ListInvalidCommand);
    }

    @Test
    void listInvalid_withoutStorage_throws() {
        AddressBookParser parser = new AddressBookParser();
        assertThrows(ParseException.class, () -> parser.parseCommand("list-invalid"));
    }
}
