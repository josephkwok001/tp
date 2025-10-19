package seedu.address;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.nio.file.Files;
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

public class MainAppInitModelManagerTest {

    private static Person p(String n, String ph, String em, String ad, String li) {
        return new Person(new Name(n), new Phone(ph), new Email(em), new Address(ad), new Listing(li), Set.of());
    }

    private static class StorageWithReport implements Storage {
        private final LoadReport report;
        private final Path path;

        StorageWithReport(LoadReport r, Path path) {
            this.report = r;
            this.path = path;
        }

        @Override public LoadReport overwriteRawEntryAtIndex(int i, Person person) {
            return report; }
        @Override public Optional<UserPrefs> readUserPrefs() {
            return Optional.empty(); }
        @Override public void saveUserPrefs(ReadOnlyUserPrefs prefs) {
        }
        @Override public Path getUserPrefsFilePath() {
            return Path.of("x"); }
        @Override public Path getAddressBookFilePath() {
            return path; }
        @Override public Optional<ReadOnlyAddressBook> readAddressBook() throws DataLoadingException {
            return Optional.empty(); }
        @Override public Optional<ReadOnlyAddressBook> readAddressBook(Path path) throws DataLoadingException {
            return Optional.empty(); }
        @Override public void saveAddressBook(ReadOnlyAddressBook ab) { }
        @Override public void saveAddressBook(ReadOnlyAddressBook ab, Path path) { }
        @Override public LoadReport readAddressBookWithReport() {
            return report; }
        @Override public LoadReport readAddressBookWithReport(Path path) {
            return report; }
    }

    private static class StorageThrowsAtPath implements Storage {
        private final Path path;
        StorageThrowsAtPath(Path path) {
            this.path = path; }

        @Override public LoadReport overwriteRawEntryAtIndex(int i, Person person) {
            return null; }
        @Override public Optional<UserPrefs> readUserPrefs() {
            return Optional.empty(); }
        @Override public void saveUserPrefs(ReadOnlyUserPrefs prefs) { }
        @Override public Path getUserPrefsFilePath() {
            return Path.of("x"); }
        @Override public Path getAddressBookFilePath() {
            return path; }
        @Override public Optional<ReadOnlyAddressBook> readAddressBook() throws DataLoadingException {
            return Optional.empty(); }
        @Override public Optional<ReadOnlyAddressBook> readAddressBook(Path path) throws DataLoadingException {
            return Optional.empty(); }
        @Override public void saveAddressBook(ReadOnlyAddressBook ab) { }
        @Override public void saveAddressBook(ReadOnlyAddressBook ab, Path path) { }
        @Override public LoadReport readAddressBookWithReport() throws DataLoadingException {
            throw new DataLoadingException(new RuntimeException("fail"));
        }
        @Override public LoadReport readAddressBookWithReport(Path path) throws DataLoadingException {
            throw new DataLoadingException(new RuntimeException("fail"));
        }
    }

    @Test
    void initModelManager_usesReportSnapshotWhenAvailable() throws Exception {
        AddressBook ab = new AddressBook();
        ab.addPerson(p("A", "91234567", "a@a.com", "a", "HDB"));
        LoadReport rpt = new LoadReport(new LoadReport.ModelData(ab), java.util.List.of());

        Path existing = Files.createTempFile("ab-exists-", ".json");
        existing.toFile().deleteOnExit();
        Storage storage = new StorageWithReport(rpt, existing);
        UserPrefs prefs = new UserPrefs();

        MainApp app = new MainApp();
        Method m = MainApp.class.getDeclaredMethod("initModelManager",
                Storage.class, ReadOnlyUserPrefs.class);
        m.setAccessible(true);
        Object out = m.invoke(app, storage, prefs);
        ModelManager mm = (ModelManager) out;

        assertEquals(1, mm.getAddressBook().getPersonList().size());
    }

    @Test
    void initModelManager_whenFileMissing_createsSample() throws Exception {
        Path missingPath = Path.of("target", "tmp-nonexistent-" + System.nanoTime() + ".json");
        Files.createDirectories(missingPath.getParent());
        if (Files.exists(missingPath)) {
            Files.delete(missingPath);
        }

        Storage storage = new StorageThrowsAtPath(missingPath);
        UserPrefs prefs = new UserPrefs();

        MainApp app = new MainApp();
        Method m = MainApp.class.getDeclaredMethod("initModelManager",
                Storage.class, ReadOnlyUserPrefs.class);
        m.setAccessible(true);
        Object out = m.invoke(app, storage, prefs);
        ModelManager mm = (ModelManager) out;

        assertTrue(mm.getAddressBook().getPersonList().size() > 0);
    }

    @Test
    void initModelManager_onErrorWithExistingPath_startsEmpty() throws Exception {
        Path existing = Files.createTempFile("ab-existing-", ".json");
        existing.toFile().deleteOnExit();

        Storage storage = new StorageThrowsAtPath(existing);
        UserPrefs prefs = new UserPrefs();

        MainApp app = new MainApp();
        Method m = MainApp.class.getDeclaredMethod("initModelManager",
                Storage.class, ReadOnlyUserPrefs.class);
        m.setAccessible(true);
        Object out = m.invoke(app, storage, prefs);
        ModelManager mm = (ModelManager) out;

        assertEquals(0, mm.getAddressBook().getPersonList().size());
    }
}
