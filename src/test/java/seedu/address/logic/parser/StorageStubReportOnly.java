package seedu.address.logic.parser;

import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.storage.LoadReport;
import seedu.address.storage.Storage;

/**
 * Minimal storage stub to satisfy parser tests.
 */
final class StorageStubReportOnly implements Storage {

    @Override
    public LoadReport overwriteRawEntryAtIndex(int index, seedu.address.model.person.Person person) {
        return dummy();
    }

    @Override
    public Optional<seedu.address.model.UserPrefs> readUserPrefs() {
        return Optional.empty();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs prefs) {
    }

    @Override
    public Path getUserPrefsFilePath() {
        return Path.of("dummy.json");
    }

    @Override
    public Path getAddressBookFilePath() {
        return Path.of("dummy.json");
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook() throws DataLoadingException {
        return Optional.empty();
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook(Path path) throws DataLoadingException {
        return Optional.empty();
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook ab) {
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook ab, Path path) {
    }

    @Override
    public LoadReport readAddressBookWithReport() {
        return dummy();
    }

    @Override
    public LoadReport readAddressBookWithReport(Path path) {
        return dummy();
    }

    private static LoadReport dummy() {
        return new LoadReport(new LoadReport.ModelData(new seedu.address.model.AddressBook()), java.util.List.of());
    }
}
