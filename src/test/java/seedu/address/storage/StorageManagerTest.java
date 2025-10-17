package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;

/**
 * Integration tests for {@link StorageManager}.
 */
public class StorageManagerTest {

    private static final Path TEST_DATA_FOLDER =
            Paths.get("src", "test", "data", "JsonAddressBookStorageTest");

    @TempDir
    public Path testFolder;

    private StorageManager storageManager;

    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    @BeforeEach
    public void setUp() {
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(getTempFilePath("prefs"));
        storageManager = new StorageManager(addressBookStorage, userPrefsStorage);
    }

    @Test
    public void prefsReadSave() throws Exception {
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(new GuiSettings(300, 600, 4, 6));
        storageManager.saveUserPrefs(original);

        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void addressBookReadSave() throws Exception {
        AddressBook original = seedu.address.testutil.TypicalPersons.getTypicalAddressBook();
        storageManager.saveAddressBook(original);

        ReadOnlyAddressBook retrieved = storageManager.readAddressBook().get();
        assertEquals(original, new AddressBook(retrieved));
    }

    @Test
    public void getAddressBookFilePath() {
        assertNotNull(storageManager.getAddressBookFilePath());
    }

    /**
     * File with only invalid entries should produce an empty model and some invalids
     * (unless the implementation repairs them all, in which case the accounting check still passes).
     */
    @Test
    public void readAddressBookWithReport_invalidOnly_reportsInvalidsOrRepairs_allAccounted() throws Exception {
        Path invalidOnly = TEST_DATA_FOLDER.resolve("invalidPersonAddressBook.json");

        StorageManager mgr = new StorageManager(
                new JsonAddressBookStorage(invalidOnly),
                new JsonUserPrefsStorage(getTempFilePath("prefs2"))
        );

        LoadReport report = mgr.readAddressBookWithReport(invalidOnly);

        AddressBook loaded = report.getModelData().getAddressBook();
        assertNotNull(loaded, "Loaded AddressBook should not be null.");

        int validCount = loaded.getPersonList().size();
        int invalidCount = report.getInvalids().size();
        int totalInJson = countPersonsArray(invalidOnly);

        assertEquals(totalInJson, validCount + invalidCount,
                "Valid + invalid should equal number of raw JSON entries.");
    }

    /**
     * Mixed file: ensure the model has some valid entries and that
     * valid + invalid equals the raw JSON entry count, regardless of
     * whether the implementation quarantines or repairs.
     */
    @Test
    public void readAddressBookWithReport_mixed_keepsValids_allAccounted() throws Exception {
        Path mixed = TEST_DATA_FOLDER.resolve("invalidAndValidPersonAddressBook.json");

        StorageManager mgr = new StorageManager(
                new JsonAddressBookStorage(mixed),
                new JsonUserPrefsStorage(getTempFilePath("prefs3"))
        );

        LoadReport report = mgr.readAddressBookWithReport(mixed);
        AddressBook loaded = report.getModelData().getAddressBook();

        assertNotNull(loaded, "Loaded AddressBook should not be null.");
        assertFalse(loaded.getPersonList().isEmpty(),
                "Loaded AddressBook should contain at least one valid person.");

        int validCount = loaded.getPersonList().size();
        int invalidCount = report.getInvalids().size();
        int totalInJson = countPersonsArray(mixed);

        assertEquals(totalInJson, validCount + invalidCount,
                "Valid + invalid should equal number of raw JSON entries.");
    }

    @Test
    public void readAddressBookWithReport_noArg_usesConfiguredPath() throws Exception {
        // Arrange: stub AddressBookStorage that records the path it was called with
        class RecordingAddressBookStorage implements AddressBookStorage {
            final Path configuredPath;
            Path lastPath; // recorded when readAddressBookWithReport(Path) is called

            RecordingAddressBookStorage(Path configuredPath) {
                this.configuredPath = configuredPath;
            }

            @Override
            public Path getAddressBookFilePath() {
                return configuredPath;
            }

            @Override
            public Optional<ReadOnlyAddressBook> readAddressBook() {
                return Optional.empty();
            }

            @Override
            public Optional<ReadOnlyAddressBook> readAddressBook(Path file) {
                return Optional.empty();
            }

            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook, Path file) {
                // no-op for this test
            }

            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook) {
                // no-op for this test
            }

            @Override
            public LoadReport readAddressBookWithReport(Path file) {
                // record the path to prove delegation used getAddressBookFilePath()
                this.lastPath = file;
                return new LoadReport(
                        new LoadReport.ModelData(new seedu.address.model.AddressBook()),
                        java.util.Collections.emptyList()
                );
            }

            @Override
            public LoadReport overwriteRawEntryAtIndex(
                    int index, seedu.address.model.person.Person person) {
                throw new UnsupportedOperationException("not used");
            }
        }

        Path configured = getTempFilePath("delegation-ab.json");
        RecordingAddressBookStorage recordingAb = new RecordingAddressBookStorage(configured);

        // Any prefs storage will do; it won't be touched by this call
        JsonUserPrefsStorage prefs = new JsonUserPrefsStorage(getTempFilePath("delegation-prefs.json"));

        StorageManager mgr = new StorageManager(recordingAb, prefs);

        // Act: call the no-arg method we want to cover
        LoadReport report = mgr.readAddressBookWithReport();

        // Assert: report exists and, critically, the storage was called with its configured path
        assertNotNull(report.getModelData().getAddressBook(), "Report should contain model data.");
        assertEquals(configured, recordingAb.lastPath,
                "No-arg readAddressBookWithReport() must delegate using getAddressBookFilePath().");
    }

    /** Counts how many person objects exist in the raw JSON "persons" array. */
    private static int countPersonsArray(Path jsonPath) throws Exception {
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        JsonNode root = mapper.readTree(jsonPath.toFile());
        JsonNode persons = (root == null) ? null : root.get("persons");
        return (persons != null && persons.isArray()) ? persons.size() : 0;
    }
}
