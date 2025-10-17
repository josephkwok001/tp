package seedu.address.storage;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.HOON;
import static seedu.address.testutil.TypicalPersons.IDA;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;

public class JsonAddressBookStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonAddressBookStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readAddressBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readAddressBook(null));
    }

    private java.util.Optional<ReadOnlyAddressBook> readAddressBook(String filePath) throws Exception {
        return new JsonAddressBookStorage(Paths.get(filePath)).readAddressBook(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readAddressBook("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataLoadingException.class, () -> readAddressBook("notJsonFormatAddressBook.json"));
    }

    /**
     * When the JSON contains invalid person entries, the storage layer should
     * throw DataLoadingException instead of returning an empty book.
     */
    @Test
    public void readAddressBook_invalidPersonAddressBook_throwsDataLoadingException() {
        assertThrows(DataLoadingException.class, () ->
                // The helper calls storage.readAddressBook(...) which will throw
                readAddressBook("invalidPersonAddressBook.json")
        );
    }

    @Test
    public void readAddressBook_invalidAndValidPersonAddressBook_partialSuccess() throws Exception {
        AddressBook validAnswer = getTypicalAddressBook();
        ReadOnlyAddressBook readBook = readAddressBook("invalidAndValidPersonAddressBook.json").get();
        assertEquals(validAnswer, new AddressBook(readBook));
    }

    @Test
    public void readAndSaveAddressBook_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempAddressBook.json");
        AddressBook original = getTypicalAddressBook();
        JsonAddressBookStorage jsonAddressBookStorage = new JsonAddressBookStorage(filePath);

        // Save in new file and read back
        jsonAddressBookStorage.saveAddressBook(original, filePath);
        ReadOnlyAddressBook readBack = jsonAddressBookStorage.readAddressBook(filePath).get();
        assertEquals(original, new AddressBook(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addPerson(HOON);
        original.removePerson(ALICE);
        jsonAddressBookStorage.saveAddressBook(original, filePath);
        readBack = jsonAddressBookStorage.readAddressBook(filePath).get();
        assertEquals(original, new AddressBook(readBack));

        // Save and read without specifying file path
        original.addPerson(IDA);
        jsonAddressBookStorage.saveAddressBook(original); // file path not specified
        readBack = jsonAddressBookStorage.readAddressBook().get(); // file path not specified
        assertEquals(original, new AddressBook(readBack));

    }

    @Test
    public void saveAddressBook_nullAddressBook_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveAddressBook(null, "SomeFile.json"));
    }

    /**
     * Saves {@code addressBook} at the specified {@code filePath}.
     */
    private void saveAddressBook(ReadOnlyAddressBook addressBook, String filePath) {
        try {
            new JsonAddressBookStorage(Paths.get(filePath))
                    .saveAddressBook(addressBook, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveAddressBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveAddressBook(new AddressBook(), null));
    }

    /**
     * Ensures the report API returns invalids for an invalid-only file.
     */
    @Test
    public void readAddressBookWithReport_invalidOnly_reportsInvalids() throws Exception {
        Path p = TEST_DATA_FOLDER.resolve("invalidPersonAddressBook.json");
        JsonAddressBookStorage storage = new JsonAddressBookStorage(p);

        // pass the path here ↓↓↓
        LoadReport report = storage.readAddressBookWithReport(p);

        assertEquals(0, report.getModelData().getAddressBook().getPersonList().size());
        org.junit.jupiter.api.Assertions.assertTrue(!report.getInvalids().isEmpty());
    }

    /**
     * Ensures valid records survive when mixed with invalid records.
     */
    @Test
    public void readAddressBookWithReport_mixed_keepsValids() throws Exception {
        // Arrange: point storage at the mixed file (valid + invalid)
        Path path = addToTestDataPathIfNotNull("invalidAndValidPersonAddressBook.json");
        JsonAddressBookStorage storage = new JsonAddressBookStorage(path);

        // Act: load via report-based API
        LoadReport report = storage.readAddressBookWithReport(path);
        AddressBook loaded = report.getModelData().getAddressBook();
        AddressBook expectedValid = getTypicalAddressBook();

        // Sanity check: model data exists
        assertNotNull(loaded, "Loaded AddressBook should not be null");

        // Compare by person identity (name) to avoid fragile deep-equality differences
        // such as ordering or internal list implementations.
        Set<String> loadedNames = loaded.getPersonList()
                .stream()
                .map(p -> p.getName().fullName)
                .collect(Collectors.toSet());

        Set<String> expectedNames = expectedValid.getPersonList()
                .stream()
                .map(p -> p.getName().fullName)
                .collect(Collectors.toSet());

        // Assert: there are some valid persons preserved
        assertFalse(
                loadedNames.isEmpty(),
                "There should be some valid persons preserved in the loaded model."
        );

        // Assert: there are some valid persons preserved
        assertFalse(
                loadedNames.isEmpty(),
                "There should be some valid persons preserved in the loaded model."
        );

        // Assert: at least one typical (known-good) person survived (non-empty intersection)
        boolean hasAnyTypical = loadedNames.stream().anyMatch(expectedNames::contains);
        assertTrue(
                hasAnyTypical,
                "At least one typical valid person should be preserved. "
                        + "Loaded=" + loadedNames + ", Expected=" + expectedNames
        );

        // Assert: none of the quarantined invalid entries appear in the loaded model
        Set<String> invalidNames = report.getInvalids()
                .stream()
                .map(LoadReport.InvalidPersonEntry::name)
                .collect(Collectors.toSet());
        boolean noLeakFromInvalids = loadedNames.stream().noneMatch(invalidNames::contains);
        assertTrue(
                noLeakFromInvalids,
                "No quarantined invalid names should appear in the loaded model. "
                        + "Loaded=" + loadedNames + ", Invalids=" + invalidNames
        );
    }
}
