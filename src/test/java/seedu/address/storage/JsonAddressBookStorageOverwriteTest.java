package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.model.person.Person;
import seedu.address.testutil.TypicalPersons;

/**
 * Tests for overwriteRawEntryAtIndex covering bounds, structure, and successful replacement.
 */
public class JsonAddressBookStorageOverwriteTest {

    @TempDir
    public Path tempDir;

    private Path write(String fileName, String content) throws Exception {
        Path p = tempDir.resolve(fileName);
        Files.write(p, content.getBytes(StandardCharsets.UTF_8));
        return p;
    }

    @Test
    void overwriteOutOfBoundsThrowsIoException() throws Exception {
        Path p = write("ab.json",
                "{ \"persons\": [ {\"name\":\"Roy Balakrishnan\",\"phone\":\"92624417\","
                        + "\"email\":\"royb@example.com\",\"address\":\"Blk 45\"} ] }");
        JsonAddressBookStorage storage = new JsonAddressBookStorage(p);
        Person alice = TypicalPersons.ALICE;
        assertThrows(java.io.IOException.class, () -> storage.overwriteRawEntryAtIndex(5, alice));
    }

    @Test
    void overwriteOutOfBoundsOnEmptyFileThrowsIoException() throws Exception {
        Path p = write("empty.json", "");
        JsonAddressBookStorage storage = new JsonAddressBookStorage(p);
        Person bob = TypicalPersons.BOB;
        assertThrows(java.io.IOException.class, () -> storage.overwriteRawEntryAtIndex(0, bob));
    }

    @Test
    void overwriteWhenPersonsIsNotArrayThrowsIoException() throws Exception {
        Path p = write("weird.json", "{ \"persons\": {\"not\":\"array\"} }");
        JsonAddressBookStorage storage = new JsonAddressBookStorage(p);
        Person carl = TypicalPersons.CARL;
        assertThrows(java.io.IOException.class, () -> storage.overwriteRawEntryAtIndex(0, carl));
    }

    @Test
    void overwriteSuccessPreservesOrderAndReturnsFreshReport() throws Exception {
        Path p = write("three.json",
                "{ \"persons\": [\n"
                        + "  {\"name\":\"Alice Pauline\",\"phone\":\"94351253\",\"email\":\"alice@example.com\","
                        + "\"address\":\"123, Jurong West Ave 6, #08-111\"},\n"
                        + "  {\"name\":\"Benson Meier\",\"phone\":\"98765432\",\"email\":\"johnd@example.com\","
                        + "\"address\":\"311, Clementi Ave 2, #02-25\"},\n"
                        + "  {\"name\":\"Carl Kurz\",\"phone\":\"95352563\",\"email\":\"heinz@example.com\","
                        + "\"address\":\"wall street\"}\n"
                        + "] }");

        JsonAddressBookStorage storage = new JsonAddressBookStorage(p);
        Person replacement = TypicalPersons.IDA;

        LoadReport rep = storage.overwriteRawEntryAtIndex(1, replacement);

        String raw = Files.readString(p, StandardCharsets.UTF_8);
        int countNames = raw.split("\\{\\s*\"name\"").length - 1;
        assertEquals(3, countNames);

        var names = rep.getModelData().getAddressBook().getPersonList()
                .stream().map(pp -> pp.getName().fullName).collect(Collectors.toList());
        assertEquals(3, names.size());
        assertEquals("Alice Pauline", names.get(0));
        assertEquals(replacement.getName().fullName, names.get(1));
        assertEquals("Carl Kurz", names.get(2));
    }
}
