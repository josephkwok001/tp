package seedu.address;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.core.Config;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.storage.LoadReport;
import seedu.address.storage.Storage;

/**
 * Tests helper methods in MainApp that do not depend on JavaFX.
 */
class MainAppPrivateMethodsTest {

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

    @TempDir
    public Path tmp;

    /**
     * Verifies initConfig creates a file when missing and returns a Config.
     */
    @Test
    void initConfig_createsConfigFileWhenMissing() {
        MainApp app = new MainApp();
        Path cfg = tmp.resolve("config.json");
        Config out = app.initConfig(cfg);
        assertNotNull(out);
        assertTrue(Files.exists(cfg));
    }

    /**
     * Verifies initModelManager returns a non-null model when called reflectively.
     */
    @Test
    void initModelManager_reflectiveCall_returnsModel() throws Exception {
        MainApp app = new MainApp();
        StorageStub storage = new StorageStub(new LoadReport(
                new LoadReport.ModelData(new seedu.address.model.AddressBook()),
                java.util.Collections.emptyList()));
        seedu.address.model.UserPrefs prefs = new seedu.address.model.UserPrefs();

        Method m = MainApp.class.getDeclaredMethod("initModelManager",
                seedu.address.storage.Storage.class,
                seedu.address.model.ReadOnlyUserPrefs.class);
        m.setAccessible(true);
        Object model = m.invoke(app, storage, prefs);
        assertNotNull(model);
    }
}
