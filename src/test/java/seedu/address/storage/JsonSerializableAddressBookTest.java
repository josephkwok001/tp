package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;
import seedu.address.testutil.TypicalPersons;

/**
 * Tests for {@link JsonSerializableAddressBook}.
 * This suite exercises legacy conversion, report-based conversion, dynamic in-memory mutation via replaceAt,
 * duplicate handling for persons and properties, unknown ownedProperties reporting, empty input handling,
 * and bounds checking.
 */
public class JsonSerializableAddressBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data",
            "JsonSerializableAddressBookTest");
    private static final Path TYPICAL_PERSONS_FILE = TEST_DATA_FOLDER.resolve("typicalPersonsAddressBook.json");
    private static final Path INVALID_PERSON_FILE = TEST_DATA_FOLDER.resolve("invalidPersonAddressBook.json");
    private static final Path DUPLICATE_PERSON_FILE = TEST_DATA_FOLDER.resolve("duplicatePersonAddressBook.json");

    /**
     * Ensures that converting a well-formed JSON produces a model equal to the typical address book.
     */
    @Test
    public void toModelType_typicalPersonsFile_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(
                TYPICAL_PERSONS_FILE, JsonSerializableAddressBook.class).get();
        LoadReport report = dataFromFile.toModelTypeWithReport();

        AddressBook loaded = report.getModelData().getAddressBook();
        AddressBook expected = TypicalPersons.getTypicalAddressBook();

        java.util.Set<String> loadedNames = loaded.getPersonList().stream()
                .map(p -> p.getName().fullName).collect(java.util.stream.Collectors.toSet());
        java.util.Set<String> expectedNames = expected.getPersonList().stream()
                .map(p -> p.getName().fullName).collect(java.util.stream.Collectors.toSet());

        assertEquals(expectedNames, loadedNames);
    }

    /**
     * Ensures that the report-based API returns the expected model and no invalid entries for a clean file.
     */
    @Test
    public void toModelTypeWithReport_typicalPersonsFile_successAndNoInvalids() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(
                TYPICAL_PERSONS_FILE, JsonSerializableAddressBook.class).get();
        LoadReport report = dataFromFile.toModelTypeWithReport();

        AddressBook loaded = report.getModelData().getAddressBook();
        AddressBook expected = TypicalPersons.getTypicalAddressBook();

        java.util.Set<String> loadedNames = loaded.getPersonList().stream()
                .map(p -> p.getName().fullName).collect(java.util.stream.Collectors.toSet());
        java.util.Set<String> expectedNames = expected.getPersonList().stream()
                .map(p -> p.getName().fullName).collect(java.util.stream.Collectors.toSet());

        assertEquals(expectedNames, loadedNames);
    }


    /**
     * Ensures that legacy conversion throws an {@link IllegalValueException} when the JSON contains invalid persons.
     */
    @Test
    public void toModelType_invalidPersonFile_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(
                INVALID_PERSON_FILE, JsonSerializableAddressBook.class
        ).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    /**
     * Ensures that the report-based API collects invalid entries instead of throwing for an invalid file.
     */
    @Test
    public void toModelTypeWithReport_invalidPersonFile_collectsInvalids() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(
                INVALID_PERSON_FILE, JsonSerializableAddressBook.class
        ).get();
        LoadReport report = dataFromFile.toModelTypeWithReport();
        assertTrue(!report.getInvalids().isEmpty());
        assertTrue(report.getModelData().getAddressBook() != null);
    }

    /**
     * Ensures that duplicate persons in the JSON cause legacy conversion to throw with the expected message.
     */
    @Test
    public void toModelType_duplicatePersons_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_PERSON_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_DUPLICATE_PERSON,
                dataFromFile::toModelType);
    }

    /**
     * Ensures that swapping two entries via replaceAt changes the resulting model without introducing duplicates.
     */
    @Test
    public void toModelType_afterReplaceAt_changesModelContent() throws Exception {
        JsonSerializableAddressBook data = JsonUtil.readJsonFile(
                TYPICAL_PERSONS_FILE, JsonSerializableAddressBook.class).get();

        AddressBook baseline = data.toModelTypeWithReport().getModelData().getAddressBook();

        JsonAdaptedPerson alice = new JsonAdaptedPerson(TypicalPersons.ALICE);
        JsonAdaptedPerson benson = new JsonAdaptedPerson(TypicalPersons.BENSON);

        data.replaceAt(0, benson);
        data.replaceAt(1, alice);

        AddressBook mutated = data.toModelTypeWithReport().getModelData().getAddressBook();

        String baselineOrder = baseline.getPersonList().stream()
                .map(p -> p.getName().fullName).collect(java.util.stream.Collectors.joining("|"));
        String mutatedOrder = mutated.getPersonList().stream()
                .map(p -> p.getName().fullName).collect(java.util.stream.Collectors.joining("|"));

        assertNotEquals(baselineOrder, mutatedOrder);
    }

    /**
     * Ensures that the report-based API collects a duplicate person entry when duplicates are created via replaceAt.
     */
    @Test
    public void toModelTypeWithReport_afterCreatingDuplicateViaReplaceAt_collectsInvalidsWithDuplicateReason()
            throws Exception {
        JsonSerializableAddressBook data = JsonUtil.readJsonFile(
                TYPICAL_PERSONS_FILE, JsonSerializableAddressBook.class).get();

        LoadReport baseline = data.toModelTypeWithReport();
        seedu.address.model.AddressBook baseAb = baseline.getModelData().getAddressBook();
        org.junit.jupiter.api.Assertions.assertTrue(baseAb.getPersonList().size() >= 2,
                "Typical persons file should contain at least 2 persons.");

        Person first = baseAb.getPersonList().get(0);
        JsonAdaptedPerson duplicateOfFirst = new JsonAdaptedPerson(first);

        data.replaceAt(1, duplicateOfFirst);

        LoadReport report = data.toModelTypeWithReport();

        java.util.List<LoadReport.InvalidPersonEntry> invalids = report.getInvalids();
        org.junit.jupiter.api.Assertions.assertTrue(!invalids.isEmpty(),
                "Expected at least one invalid entry after injecting a duplicate, but none found.");

        boolean hasDuplicateReason = invalids.stream()
                .map(LoadReport.InvalidPersonEntry::reason)
                .filter(java.util.Objects::nonNull)
                .anyMatch(r -> r.equals(JsonSerializableAddressBook.MESSAGE_DUPLICATE_PERSON)
                        || r.toLowerCase().contains("duplicate person"));
        org.junit.jupiter.api.Assertions.assertTrue(
                hasDuplicateReason,
                "Expected a duplicate-person reason in invalids, but got: "
                        + invalids.stream().map(LoadReport.InvalidPersonEntry::reason).toList()
        );
    }

    /**
     * Ensures that providing null lists through the JSON-creator constructor yields an empty model and no invalids.
     */
    @Test
    public void toModelTypeWithReport_nullLists_yieldsEmptyModelNoInvalids() throws Exception {
        JsonSerializableAddressBook empty = new JsonSerializableAddressBook(null, null);
        LoadReport report = empty.toModelTypeWithReport();
        assertTrue(report.getModelData().getAddressBook().getPersonList().isEmpty());
        assertTrue(report.getModelData().getAddressBook().getPropertyList().isEmpty());
        assertTrue(report.getInvalids().isEmpty());
    }

    /**
     * Ensures that replaceAt throws {@link IndexOutOfBoundsException} for invalid indices.
     */
    @Test
    public void replaceAt_outOfBounds_throwsIndexOutOfBoundsException() throws Exception {
        JsonSerializableAddressBook data = JsonUtil.readJsonFile(TYPICAL_PERSONS_FILE,
                JsonSerializableAddressBook.class).get();
        JsonAdaptedPerson replacement = new JsonAdaptedPerson(TypicalPersons.ALICE);
        assertThrows(IndexOutOfBoundsException.class, () -> data.replaceAt(-1, replacement));
        assertThrows(IndexOutOfBoundsException.class, () -> data.replaceAt(9_999, replacement));
    }

    /**
     * Ensures that unknown owned properties are reported as invalids.
     * Accepts either the early resolving error or the later consolidated message.
     */
    @Test
    public void toModelTypeWithReport_unknownOwnedProperties_collectsInvalids() throws Exception {
        java.util.List<JsonAdaptedProperty> props = java.util.Collections.emptyList();

        String name = "Zed Zero";
        String phone = "12345678";
        String email = "zed@example.com";
        String address = "NUS";
        java.util.List<JsonAdaptedTag> tags = java.util.Collections.emptyList();
        java.util.List<String> ownedProps = java.util.Arrays.asList("Unknown One", "Unknown Two");

        JsonAdaptedPerson zed = new JsonAdaptedPerson(
                name, phone, email, address, tags, ownedProps, java.util.Collections.emptyList()
        );

        JsonSerializableAddressBook data = new JsonSerializableAddressBook(
                java.util.Arrays.asList(zed), props
        );

        LoadReport report = data.toModelTypeWithReport();

        assertTrue(!report.getInvalids().isEmpty());
        boolean hasExpectedReason = report.getInvalids().stream()
                .anyMatch(inv -> {
                    String r = inv.reason();
                    return r != null && (r.contains("Unknown properties")
                            || r.contains("Property not found during load"));
                });
        assertTrue(hasExpectedReason);
    }
}
