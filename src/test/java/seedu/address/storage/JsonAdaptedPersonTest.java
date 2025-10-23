package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyName;
import seedu.address.model.util.SampleDataUtil;

/**
 * Tests for {@link JsonAdaptedPerson} covering validation, property resolution,
 * equality, defensive cases, and raw JSON serialization.
 */
public class JsonAdaptedPersonTest {

    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();

    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    private static final List<String> VALID_INTERESTED_PROP_NAMES = SampleDataUtil.getSampleProperties()
            .stream()
            .map(p -> p.getPropertyName().toString())
            .collect(Collectors.toList());

    /**
     * Ensures a valid person is reconstructed faithfully.
     */
    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    /**
     * Covers resolution of owned and interested property names with a populated AddressBook.
     */
    @Test
    public void toModelType_withAddressBook_resolvesProperties() throws Exception {
        Property owned = new Property(new seedu.address.model.property.Address("1 NUS Rd"),
                new Price(1_000_000), new PropertyName("Campus Home"));
        Property interested = new Property(new seedu.address.model.property.Address("2 Clementi Ave"),
                new Price(2_000_000), new PropertyName("Sunset Loft"));
        AddressBook ab = new AddressBook();
        ab.addProperty(owned);
        ab.addProperty(interested);

        List<String> ownedList = List.of("Campus Home");
        List<String> interestedList = List.of("Sunset Loft");

        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, ownedList, interestedList);

        Person model = person.toModelType(ab);
        assertTrue(model.getOwnedProperties().stream()
                .anyMatch(p -> p.getPropertyName().toString().equals("Campus Home")));
        assertTrue(model.getInterestedProperties().stream()
                .anyMatch(p -> p.getPropertyName().toString().equals("Sunset Loft")));
    }

    /**
     * Verifies that missing property names in the AddressBook raise an exception.
     */
    @Test
    public void toModelType_withUnknownProperty_throwsIllegalValueException() {
        AddressBook ab = new AddressBook();
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, List.of("Ghost Mansion"), List.of());
        assertThrows(IllegalValueException.class, () -> person.toModelType(ab));
    }

    /**
     * Ensures equality and hashCode produce consistent results for equivalent data.
     */
    @Test
    public void equals_andHashCode_behaveCorrectly() throws Exception {
        JsonAdaptedPerson a = new JsonAdaptedPerson(BENSON);
        JsonAdaptedPerson b = new JsonAdaptedPerson(BENSON);

        assertTrue(a.equals(a));

        assertEquals(a.toRawJsonString(), b.toRawJsonString());

        assertEquals(a.toModelType(), b.toModelType());

        assertEquals(a.toRawJsonString().hashCode(), b.toRawJsonString().hashCode());
    }

    /**
     * Confirms null and empty constructor lists yield empty collections after model conversion.
     */
    @Test
    public void constructor_nullLists_yieldsEmptyCollections() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                null, null, null);

        Person model = person.toModelType();
        org.junit.jupiter.api.Assertions.assertTrue(model.getOwnedProperties().isEmpty());
        org.junit.jupiter.api.Assertions.assertTrue(model.getInterestedProperties().isEmpty());
    }


    /**
     * Validates that invalid field combinations trigger composite exception behavior.
     */
    @Test
    public void toModelType_invalidComposite_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                INVALID_NAME, INVALID_PHONE, INVALID_EMAIL, INVALID_ADDRESS,
                null, null, null);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    /**
     * Verifies raw JSON serialization returns a compact JSON object containing expected keys.
     */
    @Test
    public void toRawJsonString_containsExpectedKeys() {
        JsonAdaptedPerson adapted = new JsonAdaptedPerson(
                VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS,
                Collections.emptyList(), VALID_INTERESTED_PROP_NAMES);
        String json = adapted.toRawJsonString();
        assertTrue(json.contains("\"name\""));
        assertTrue(json.contains("\"phone\""));
        assertTrue(json.contains("\"ownedProperties\""));
    }

    /**
     * Confirms invalid fields are correctly reported by invalidFieldKeys().
     */
    @Test
    public void invalidFieldKeys_allInvalidFieldsReported() {
        JsonAdaptedPerson adapted = new JsonAdaptedPerson(
                INVALID_NAME, INVALID_PHONE, INVALID_EMAIL, INVALID_ADDRESS,
                null, null, null);
        Set<String> keys = adapted.invalidFieldKeys();
        assertTrue(keys.contains("name"));
        assertTrue(keys.contains("phone"));
        assertTrue(keys.contains("email"));
        assertTrue(keys.contains("address"));
    }
}
