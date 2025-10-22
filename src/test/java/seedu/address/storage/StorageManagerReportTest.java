package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyAddressBook;

/**
 * Ensures StorageManager delegates readAddressBookWithReport to underlying storage.
 */
public class StorageManagerReportTest {

    @Test
    void delegate_readReport() throws Exception {
        Path ab = Files.createTempFile("sm_ab", ".json");
        Path prefs = Files.createTempFile("sm_prefs", ".json");
        String json = "{ \"persons\":["
                + "{ \"name\":\"Alice Bob\",\"phone\":\"91234567\",\"email\":\"alice@example.com\","
                + "\"address\":\"1 Main\" },"
                + "{ \"name\":\"12345\",\"phone\":\"xx\",\"email\":\"bad\",\"address\":\" \" }"
                + "] }";
        Files.write(ab, json.getBytes(StandardCharsets.UTF_8));

        JsonAddressBookStorage abStorage = new JsonAddressBookStorage(ab);
        JsonUserPrefsStorage prefsStorage = new JsonUserPrefsStorage(prefs);
        StorageManager sm = new StorageManager(abStorage, prefsStorage);

        LoadReport rp = sm.readAddressBookWithReport();
        ReadOnlyAddressBook model = rp.getModelData().getAddressBook();
        assertEquals(1, model.getPersonList().size());
        assertEquals(1, rp.getInvalids().size());
    }
}
