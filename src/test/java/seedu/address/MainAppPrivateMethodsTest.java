package seedu.address;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.storage.LoadReport;
import seedu.address.storage.Storage;

/**
 * Tests private helpers in MainApp via reflection so we do not modify production code.
 * We avoid JavaFX startup by not calling start(), only pure helpers.
 */
class MainAppPrivateMethodsTest {

    /**
     * Storage stub that returns a configurable LoadReport and no-op methods elsewhere.
     * This keeps the test focused on MainApp logic, not I/O.
     */
    private static final class StorageStub implements Storage {
        private final LoadReport report;

        StorageStub(LoadReport report) {
            this.report = report;
        }

        @Override
        public LoadReport overwriteRawEntryAtIndex(int index,
                                                   seedu.address.model.person.Person person) {
            return report;
        }

        @Override
        public Optional<seedu.address.model.UserPrefs> readUserPrefs() {
            return Optional.empty();
        }

        @Override
        public void saveUserPrefs(ReadOnlyUserPrefs prefs) { }

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
        public void saveAddressBook(ReadOnlyAddressBook ab) { }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook ab, Path path) { }

        @Override
        public LoadReport readAddressBookWithReport() {
            return report;
        }

        @Override
        public LoadReport readAddressBookWithReport(Path path) {
            return report;
        }
    }

    /**
     * Builds a report with the given number of invalid entries.
     */
    private static LoadReport reportWithInvalidCount(int n) {
        var ab = new seedu.address.model.AddressBook();
        var modelData = new LoadReport.ModelData(ab);
        var invalids = new java.util.ArrayList<LoadReport.InvalidPersonEntry>();
        for (int i = 0; i < n; i++) {
            invalids.add(new LoadReport.InvalidPersonEntry(
                    i + 1, "invalid", "", "", "", "", "",
                    Collections.singleton("name")
            ));
        }
        return new LoadReport(modelData, invalids);
    }

    /**
     * Verifies countInvalidEntries() returns the value provided by Storage.
     */
    @Test
    void countInvalidEntries_reflectiveCall_matchesStorage() throws Exception {
        var app = new MainApp();
        var storage = new StorageStub(reportWithInvalidCount(3));

        // Inject storage field reflectively.
        var field = MainApp.class.getDeclaredField("storage");
        field.setAccessible(true);
        field.set(app, storage);

        // Call private int countInvalidEntries().
        Method m = MainApp.class.getDeclaredMethod("countInvalidEntries");
        m.setAccessible(true);
        Object out = m.invoke(app);

        assertEquals(3, ((Integer) out).intValue());
    }

    /**
     * Verifies hasInvalidEntries() returns false when Storage reports none.
     */
    @Test
    void hasInvalidEntries_reflectiveCall_falseWhenNone() throws Exception {
        var app = new MainApp();
        var storage = new StorageStub(reportWithInvalidCount(0));

        var field = MainApp.class.getDeclaredField("storage");
        field.setAccessible(true);
        field.set(app, storage);

        Method m = MainApp.class.getDeclaredMethod("hasInvalidEntries");
        m.setAccessible(true);
        Object out = m.invoke(app);

        assertFalse(((Boolean) out).booleanValue());
    }
}
