package seedu.address.testutil;

import seedu.address.model.property.Address;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyName;

/**
 * A utility class to help with building Property objects.
 */
public class PropertyBuilder {

    public static final String DEFAULT_NAME = "Sunshine Gardens";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final int DEFAULT_PRICE = 8000000;

    private PropertyName name;
    private Address address;
    private Price price;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PropertyBuilder() {
        name = new PropertyName(DEFAULT_NAME);
        address = new Address(DEFAULT_ADDRESS);
        price = new Price(DEFAULT_PRICE);
    }

    /**
     * Initializes the PersonBuilder with the data of {@code propertyToCopy}.
     */
    public PropertyBuilder(Property propertyToCopy) {
        name = propertyToCopy.getPropertyName();
        address = propertyToCopy.getAddress();
        price = propertyToCopy.getPrice();
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PropertyBuilder withName(String name) {
        this.name = new PropertyName(name);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public PropertyBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Price} of the {@code Person} that we are building.
     */
    public PropertyBuilder withPrice(int price) {
        this.price = new Price(price);
        return this;
    }

    public Property build() {
        return new Property(address, price, name);
    }
}
