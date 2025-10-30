package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.property.Property;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";
    public static final String PROPERTY_NOT_FOUND_MESSAGE_FORMAT = "Property not found during load: %s";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    /** Property names for owned properties. */
    private final List<String> ownedProperties = new ArrayList<>();
    /** Property names for interested properties. */
    private final List<String> interestedProperties = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given JSON fields.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name,
                             @JsonProperty("phone") String phone,
                             @JsonProperty("email") String email,
                             @JsonProperty("address") String address,
                             @JsonProperty("tags") List<JsonAdaptedTag> tags,
                             @JsonProperty("ownedProperties") List<String> ownedProperties,
                             @JsonProperty("interestedProperties") List<String> interestedProperties) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        if (ownedProperties != null) {
            this.ownedProperties.addAll(ownedProperties);
        }
        if (interestedProperties != null) {
            this.interestedProperties.addAll(interestedProperties);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        this.name = source.getName().fullName;
        this.phone = source.getPhone().value;
        this.email = source.getEmail().value;
        this.address = source.getAddress().value;

        this.tags.addAll(source.getTags().stream().map(JsonAdaptedTag::new).toList());

        // Serialize owned property names
        this.ownedProperties.addAll(
                source.getOwnedProperties().stream()
                        .map(p -> p.getPropertyName().toString())
                        .toList());

        // Serialize interested property names
        this.interestedProperties.addAll(
                source.getInterestedProperties().stream()
                        .map(p -> p.getPropertyName().toString())
                        .toList());
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        final Set<Tag> modelTags = new HashSet<>(personTags);

        return new Person(modelName, modelPhone, modelEmail, modelAddress, modelTags, List.of(), List.of());
    }

    /**
     * Converts this adapted person into a model {@code Person}, resolving property names
     * against the provided {@code addressBook}.
     */
    public Person toModelType(ReadOnlyAddressBook addressBook) throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        final Set<Tag> modelTags = new HashSet<>(personTags);

        // Resolve owned property names
        final List<Property> resolvedOwned = new ArrayList<>();
        if (addressBook != null && !ownedProperties.isEmpty()) {
            for (String propName : ownedProperties) {
                Property p = addressBook.getPropertyList().stream()
                        .filter(pp -> pp.getPropertyName().toString().equals(propName))
                        .findFirst()
                        .orElseThrow(() ->
                                new IllegalValueException(String.format(PROPERTY_NOT_FOUND_MESSAGE_FORMAT, propName)));
                resolvedOwned.add(p);
            }
        }

        // Resolve interested property names
        final List<Property> resolvedInterested = new ArrayList<>();
        if (addressBook != null && !interestedProperties.isEmpty()) {
            for (String propName : interestedProperties) {
                Property p = addressBook.getPropertyList().stream()
                        .filter(pp -> pp.getPropertyName().toString().equals(propName))
                        .findFirst()
                        .orElseThrow(() ->
                                new IllegalValueException(String.format(PROPERTY_NOT_FOUND_MESSAGE_FORMAT, propName)));
                resolvedInterested.add(p);
            }
        }

        return new Person(modelName, modelPhone, modelEmail, modelAddress, modelTags,
                resolvedOwned, resolvedInterested);
    }

    /**
     * Returns a compact JSON string representing this adapted person, or "{}" if
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
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public List<String> getOwnedProperties() {
        return java.util.Collections.unmodifiableList(ownedProperties);
    }

    /**
     * Returns an unmodifiable view of interested property names.
     */
    public List<String> getInterestedProperties() {
        return java.util.Collections.unmodifiableList(interestedProperties);
    }


    /**
     * Returns the set of field keys that are invalid, based on the same validation
     * rules used by toModelType(). Keys are one or more of:
     *   "name", "phone", "email", "address"
     *
     * This lets the load-report and the fix wizard know exactly which inputs to
     * require from the user, while pre-filling the valid ones as read-only.
     */
    public Set<String> invalidFieldKeys() {
        Set<String> invalids = new HashSet<>();

        if (name == null || !Name.isValidName(name)) {
            invalids.add("name");
        }
        if (phone == null || !Phone.isValidPhone(phone)) {
            invalids.add("phone");
        }
        if (email == null || !Email.isValidEmail(email)) {
            invalids.add("email");
        }
        if (address == null || !Address.isValidAddress(address)) {
            invalids.add("address");
        }
        return invalids;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JsonAdaptedPerson)) {
            return false;
        }
        JsonAdaptedPerson that = (JsonAdaptedPerson) o;

        return Objects.equals(name, that.name)
                && Objects.equals(phone, that.phone)
                && Objects.equals(email, that.email)
                && Objects.equals(address, that.address)
                && Objects.equals(tags, that.tags)
                && Objects.equals(ownedProperties, that.ownedProperties)
                && Objects.equals(interestedProperties, that.interestedProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                name, phone, email, address,
                tags, ownedProperties, interestedProperties
        );
    }
}
