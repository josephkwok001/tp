package seedu.address;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.AddressBook;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Listing;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.storage.LoadReport;
import seedu.address.storage.Storage;

/**
 * Tests MainApp.initModelManager via reflection.
 */
public class MainAppInitModelManagerTest {

    private static Person p(String n, String ph, String em, String ad, String li) {
        return new Person(new Name(n), new Phone(ph), new Email(em), new Address(ad), new Listing(li), Set.of());
    }

    private static class StorageWithReport implements Storage {
        private final LoadReport report;

        StorageWithReport(LoadReport r) {
            this.report = r;
        }

        @Override
        public LoadReport overwriteRawEntryAtIndex(int i, Person person) {
            return report;
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
            return Path.of("x");
        }

        @Override
        public Path getAddressBookFilePath() {
            return Path.of("x");
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
            return report;
        }

        @Override
        public LoadReport readAddressBookWithReport(Path path) {
            return report;
        }
    }

    private static class StorageThrows implements Storage {

        @Override
        public LoadReport overwriteRawEntryAtIndex(int i, Person person) {
            return null;
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
            return Path.of("x");
        }

        @Override
        public Path getAddressBookFilePath() {
            return Path.of("x");
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
        public LoadReport readAddressBookWithReport() throws DataLoadingException {
            throw new DataLoadingException(new RuntimeException("fail"));
        }

        @Override
        public LoadReport readAddressBookWithReport(Path path) throws DataLoadingException {
            throw new DataLoadingException(new RuntimeException("fail"));
        }
    }

    @Test
    void initModelManager_usesReportSnapshotWhenAvailable() throws Exception {
        AddressBook ab = new AddressBook();
        ab.addPerson(p("A", "91234567", "a@a.com", "a", "HDB"));
        LoadReport rpt = new LoadReport(new LoadReport.ModelData(ab), java.util.List.of());
        Storage storage = new StorageWithReport(rpt);
        UserPrefs prefs = new UserPrefs();

        MainApp app = new MainApp();
        Method m = MainApp.class.getDeclaredMethod("initModelManager",
                Storage.class, seedu.address.model.ReadOnlyUserPrefs.class);
        m.setAccessible(true);
        Object out = m.invoke(app, storage, prefs);
        ModelManager mm = (ModelManager) out;
        assertEquals(1, mm.getAddressBook().getPersonList().size());
    }

    @Test
    void initModelManager_onErrorStartsEmpty() throws Exception {
        Storage storage = new StorageThrows();
        UserPrefs prefs = new UserPrefs();
        MainApp app = new MainApp();
        Method m = MainApp.class.getDeclaredMethod("initModelManager",
                Storage.class, seedu.address.model.ReadOnlyUserPrefs.class);
        m.setAccessible(true);
        Object out = m.invoke(app, storage, prefs);
        ModelManager mm = (ModelManager) out;
        assertEquals(0, mm.getAddressBook().getPersonList().size());
    }
}
