package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyAddressBook;

/**
 * Ensures readAddressBookWithReport does not mutate the raw JSON file on disk.
 */
public class JsonAddressBookStorageReportTest {

    @Test
    void readWithReport_keepsJsonIntact_noDeletion() throws Exception {
        Path temp = Files.createTempFile("ab", ".json");
        String json = "{ \"persons\":[\n"
                + "  { \"name\":\"12345\",\"phone\":\"abc\",\"email\":\"bad\",\"address\":\" \",\"listing\":\"\" },\n"
                + "  { \"name\":\"Roy Balakrishnan\",\"phone\":\"92624417\",\"email\":\"royb@example.com\","
                + "\"address\":\"Blk 45\",\"listing\":\"Landed Property\" }\n"
                + "] }\n";
        Files.write(temp, json.getBytes(StandardCharsets.UTF_8));

        JsonAddressBookStorage storage = new JsonAddressBookStorage(temp);
        LoadReport rep = storage.readAddressBookWithReport(temp);
        ReadOnlyAddressBook valid = rep.getModelData().getAddressBook();

        assertEquals(1, valid.getPersonList().size());

        String raw = new String(Files.readAllBytes(temp), StandardCharsets.UTF_8);
        int countPersons = raw.split("\"name\"").length - 1;
        assertEquals(2, countPersons);
    }
}
