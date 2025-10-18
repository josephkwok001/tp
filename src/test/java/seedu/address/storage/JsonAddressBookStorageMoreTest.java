package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyAddressBook;

/**
 * Exercises additional readAddressBookWithReport scenarios.
 */
public class JsonAddressBookStorageMoreTest {

    @Test
    void read_validAll_loadedNoInvalids() throws Exception {
        Path temp = Files.createTempFile("ab_ok", ".json");
        String json = "{ \"persons\":["
                + "{ \"name\":\"Alice Bob\",\"phone\":\"91234567\",\"email\":\"alice@example.com\","
                + "\"address\":\"1 Main\",\"listing\":\"HDB\",\"tags\":[\"t\"] }"
                + "] }";
        Files.write(temp, json.getBytes(StandardCharsets.UTF_8));
        JsonAddressBookStorage storage = new JsonAddressBookStorage(temp);
        LoadReport rp = storage.readAddressBookWithReport(temp);
        ReadOnlyAddressBook ab = rp.getModelData().getAddressBook();
        assertEquals(1, ab.getPersonList().size());
        assertEquals(0, rp.getInvalids().size());
    }

    @Test
    void read_missingField_quarantinedButFileUntouched() throws Exception {
        Path temp = Files.createTempFile("ab_missing", ".json");
        String json = "{ \"persons\":["
                + "{ \"phone\":\"999\",\"email\":\"a@b.com\",\"address\":\"x\",\"listing\":\"HDB\" },"
                + "{ \"name\":\"Roy Balakrishnan\",\"phone\":\"92624417\",\"email\":\"royb@example.com\","
                + "\"address\":\"Blk 45\",\"listing\":\"Landed Property\" }"
                + "] }";
        Files.write(temp, json.getBytes(StandardCharsets.UTF_8));
        JsonAddressBookStorage storage = new JsonAddressBookStorage(temp);
        LoadReport rp = storage.readAddressBookWithReport(temp);
        assertEquals(1, rp.getModelData().getAddressBook().getPersonList().size());
        String raw = new String(Files.readAllBytes(temp), StandardCharsets.UTF_8);
        int count = raw.split("\"phone\"").length - 1;
        assertEquals(2, count);
    }
}
