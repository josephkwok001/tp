package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;

/**
 * Edge-case tests for {@link JsonAddressBookStorage#readAddressBookWithReport(Path)}.
 * This suite asserts the current behavior: malformed or empty inputs throw {@link DataLoadingException}.
 */
public class JsonAddressBookStorageWithReportEdgeCasesTest {

    @TempDir
    public Path tempDir;

    /**
     * An empty file should result in a DataLoadingException.
     */
    @Test
    public void readWithReport_emptyFile_throwsDataLoadingException() throws IOException {
        Path file = tempDir.resolve("empty.json");
        Files.createFile(file);

        JsonAddressBookStorage storage = new JsonAddressBookStorage(file);
        assertThrows(DataLoadingException.class, () -> storage.readAddressBookWithReport(file));
    }

    /**
     * A JSON root that is not an object (e.g., an array) should result in a DataLoadingException.
     */
    @Test
    public void readWithReport_rootIsNotObject_throwsDataLoadingException() throws IOException {
        Path file = tempDir.resolve("array.json");
        Files.writeString(file, "[]");

        JsonAddressBookStorage storage = new JsonAddressBookStorage(file);
        assertThrows(DataLoadingException.class, () -> storage.readAddressBookWithReport(file));
    }
}
