package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_PROPERTY_1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_PROPERTY_2;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_PROPERTY_1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_PROPERTY_2;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRICE_PROPERTY_1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRICE_PROPERTY_2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.property.Property;

/**
 * A utility class containing a list of {@code Property} objects to be used in tests.
 */
public class TypicalProperties {

    public static final Property PROPERTY_A = new PropertyBuilder().withName("The Pinnacle at Duxton")
            .withAddress("1G Cantonment Road, #48-10, S080001")
            .withPrice(1450000).build();

    public static final Property PROPERTY_B = new PropertyBuilder().withName("SkyVille at Dawson")
            .withAddress("86 Dawson Road, #35-05, S141086")
            .withPrice(1100000).build();

    public static final Property PROPERTY_C = new PropertyBuilder().withName("The Interlace")
            .withAddress("180 Depot Road, #03-22, S109684")
            .withPrice(2200000).build();

    public static final Property PROPERTY_D = new PropertyBuilder().withName("Reflections at Keppel Bay")
            .withAddress("8 Keppel Bay View, #12-88, S098417")
            .withPrice(3500000).build();

    public static final Property PROPERTY_E = new PropertyBuilder().withName("Marina Bay Residences")
            .withAddress("18 Marina Boulevard, #25-01, S018980")
            .withPrice(4800000).build();

    public static final Property PROPERTY_F = new PropertyBuilder().withName("DLeedon")
            .withAddress("1 Leedon Heights, #15-15, S267939")
            .withPrice(2850000).build();

    public static final Property PROPERTY_G = new PropertyBuilder().withName("The Sail at Marina Bay")
            .withAddress("6 Marina Boulevard, #50-12, S018985")
            .withPrice(3200000).build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Property PROPERTY_1 = new PropertyBuilder().withName(VALID_NAME_PROPERTY_1)
            .withAddress(VALID_ADDRESS_PROPERTY_1)
            .withPrice(VALID_PRICE_PROPERTY_1)
            .build();
    public static final Property PROPERTY_2 = new PropertyBuilder().withName(VALID_NAME_PROPERTY_2)
            .withAddress(VALID_ADDRESS_PROPERTY_2)
            .withPrice(VALID_PRICE_PROPERTY_2)
            .build();



    private TypicalProperties() {} // prevents instantiation

    /**
     * Returns an {@code EstateSearch} with all the typical properties.
     */
    public static AddressBook getTypicalPropertiesAddressBook() {
        AddressBook ab = new AddressBook();
        for (Property property : getTypicalProperties()) {
            ab.addProperty(property);
        }
        return ab;
    }

    /**
     * Returns a list of typical properties.
     */
    public static List<Property> getTypicalProperties() {
        return new ArrayList<>(Arrays.asList(PROPERTY_A, PROPERTY_B, PROPERTY_C,
                PROPERTY_D, PROPERTY_E, PROPERTY_F, PROPERTY_G));
    }
}
