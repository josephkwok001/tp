package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.storage.JsonAdaptedProperty.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyName;
/**
 * Tests for {@link JsonAdaptedProperty} covering validation, property resolution,
 * equality, defensive cases, and raw JSON serialization.
 */
public class JsonAdaptedPropertyTest {

    private static final String VALID_ADDRESS = "123, Main Street, #01-01";
    private static final Integer VALID_PRICE = 500000;
    private static final String VALID_PROPERTY_NAME = "Hillside Villa";

    /**
     * Ensures a valid property is reconstructed faithfully:
     * core fields equal to expected values.
     */
    @Test
    public void toModelType_validPropertyDetails_returnsProperty() throws Exception {
        JsonAdaptedProperty adapted = new JsonAdaptedProperty(VALID_ADDRESS, VALID_PRICE, VALID_PROPERTY_NAME);
        Property property = adapted.toModelType();
        assertEquals(new PropertyName(VALID_PROPERTY_NAME), property.getPropertyName());
        assertEquals(new seedu.address.model.property.Address(VALID_ADDRESS), property.getAddress());
        assertEquals(new Price(VALID_PRICE), property.getPrice());
    }

    @Test
    public void equals_andHashCode_behaveCorrectly() {
        JsonAdaptedProperty adapted1 = new JsonAdaptedProperty(VALID_ADDRESS, VALID_PRICE, VALID_PROPERTY_NAME);
        JsonAdaptedProperty adapted2 = new JsonAdaptedProperty(VALID_ADDRESS, VALID_PRICE, VALID_PROPERTY_NAME);
        JsonAdaptedProperty adapted3 = new JsonAdaptedProperty("Different Address", VALID_PRICE, VALID_PROPERTY_NAME);

        // Test equality
        assertEquals(adapted1, adapted2);
        assertNotEquals(adapted1, adapted3);

        // Test hashCode
        assertEquals(adapted1.hashCode(), adapted2.hashCode());
        assertTrue(adapted1.hashCode() != adapted3.hashCode());
    }

    @Test
    public void toModelType_missingPropertyName_throwsIllegalValueException() {
        JsonAdaptedProperty adapted = new JsonAdaptedProperty(VALID_ADDRESS, VALID_PRICE, null);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT,
                PropertyName.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, adapted::toModelType);
    }

    @Test
    public void toModelType_invalidPropertyName_throwsIllegalValueException() {
        String invalidPropertyName = "Hillside@Villa";
        JsonAdaptedProperty adapted = new JsonAdaptedProperty(VALID_ADDRESS, VALID_PRICE, invalidPropertyName);
        String expectedMessage = PropertyName.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, adapted::toModelType);
    }

    @Test
    public void toModelType_missingPrice_throwsIllegalValueException() {
        JsonAdaptedProperty adapted = new JsonAdaptedProperty(VALID_ADDRESS, null, VALID_PROPERTY_NAME);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT,
                Price.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, adapted::toModelType);
    }

    @Test
    public void toModelType_invalidPrice_throwsIllegalValueException() {
        Integer invalidPrice = -1000;
        JsonAdaptedProperty adapted = new JsonAdaptedProperty(VALID_ADDRESS, invalidPrice, VALID_PROPERTY_NAME);
        String expectedMessage = Price.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, adapted::toModelType);
    }

    @Test
    public void toModelType_missingAddress_throwsIllegalValueException() {
        JsonAdaptedProperty adapted = new JsonAdaptedProperty(null, VALID_PRICE, VALID_PROPERTY_NAME);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT,
                Address.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, adapted::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        String invalidAddress = "";
        JsonAdaptedProperty adapted = new JsonAdaptedProperty(invalidAddress, VALID_PRICE, VALID_PROPERTY_NAME);
        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, adapted::toModelType);
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        JsonAdaptedProperty adapted = new JsonAdaptedProperty(VALID_ADDRESS, VALID_PRICE, VALID_PROPERTY_NAME);
        assertEquals(adapted, adapted);
    }

    @Test
    public void equals_differentType_returnsFalse() {
        JsonAdaptedProperty adapted = new JsonAdaptedProperty(VALID_ADDRESS, VALID_PRICE, VALID_PROPERTY_NAME);
        assertNotEquals("Some String", adapted);
    }
}
