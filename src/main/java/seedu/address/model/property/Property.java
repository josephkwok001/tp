package seedu.address.model.property;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.List;
import java.util.Objects;

/**
 * Represents a Property in the address book.
 * Guarantees: details are present and not null, field values are validated,
 * immutable.
 */
public class Property {

    private final Address address;
    private final Price price;
    private final PropertyName propertyName;

    /**
     * Every field must be present and not null.
     */
    public Property(Address address, Price price, PropertyName propertyName) {
        requireAllNonNull(address, price, propertyName);
        this.address = address;
        this.price = price;
        this.propertyName = propertyName;
    }

    public Address getAddress() {
        return address;
    }

    public Price getPrice() {
        return price;
    }

    public PropertyName getPropertyName() {
        return propertyName;
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSameProperty(Property otherProperty) {
        if (otherProperty == this) {
            return true;
        }

        return otherProperty != null
                && otherProperty.getPropertyName().equals(getPropertyName());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Property)) {
            return false;
        }

        Property otherProperty = (Property) other;
        return address.equals(otherProperty.address)
                && price.equals(otherProperty.price)
                && propertyName.equals(otherProperty.propertyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, price, propertyName);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(" Address: ")
                .append(getAddress())
                .append(" Price: $")
                .append(getPrice())
                .append(" Property Name: ")
                .append(getPropertyName());
        return builder.toString();
    }
}
