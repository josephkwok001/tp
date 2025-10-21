package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.property.Address;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyName;



/**
 * Jackson-friendly version of {@link Property}.
 */
class JsonAdaptedProperty {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Property's %s field is missing!";

    private final String address;
    private final Integer price;
    private final String propertyName;

    /**
     * Constructs a {@code JsonAdaptedProperty} with the given property details.
     */
    @JsonCreator
    public JsonAdaptedProperty(@JsonProperty("address") String address, @JsonProperty("price") Integer price,
            @JsonProperty("propertyName") String propertyName) {
        this.address = address;
        this.price = price;
        this.propertyName = propertyName;

    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedProperty(Property source) {
        this.address = source.getAddress().toString();
        this.price = source.getPrice().getIntegerPrice();
        this.propertyName = source.getPropertyName().toString();
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's
     * {@code Property} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in
     *                               the adapted property.
     */
    public Property toModelType() throws IllegalValueException {
        if (propertyName == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    PropertyName.class.getSimpleName()));
        }
        if (!PropertyName.isValidName(propertyName)) {
            throw new IllegalValueException(PropertyName.MESSAGE_CONSTRAINTS);
        }
        final PropertyName modelName = new PropertyName(propertyName);

        if (price == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Price.class.getSimpleName()));
        }
        if (!Price.isValidPrice(price)) {
            throw new IllegalValueException(Price.MESSAGE_CONSTRAINTS);
        }
        final Price modelPrice = new Price(price);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        return new Property(modelAddress, modelPrice, modelName);
    }

    /**
     * Returns a compact JSON string representing this adapted Property, or "{}" if
     * serialization fails. Intended for diagnostics in load reports.
     */
    public String toRawJsonString() {
        try {
            return seedu.address.commons.util.JsonUtil.toJsonString(this);
        } catch (Exception e) {
            return "{}";
        }
    }

    // --- Add these getters so the loader can prefill the dialog with originals ---
    public String getName() {
        return propertyName;
    }

    public String getAddress() {
        return address;
    }

    public Integer getPrice() {
        return price;
    }

    /**
     * Returns the set of field keys that are invalid, based on the same validation
     * rules used by toModelType(). Keys are one or more of:
     * "propertyName", "address", "price"
     *
     * This lets the load-report and the fix wizard know exactly which inputs to
     * require from the user, while pre-filling the valid ones as read-only.
     */
    public java.util.Set<String> invalidFieldKeys() {
        java.util.Set<String> invalids = new java.util.HashSet<>();

        if (address == null || !Address.isValidAddress(address)) {
            invalids.add("address");
        }
        if (price == null || !Price.isValidPrice(price)) {
            invalids.add("price");
        }
        if (propertyName == null || !PropertyName.isValidName(propertyName)) {
            invalids.add("propertyName");
        }
        return invalids;
    }
}
