package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.property.Property;
import seedu.address.model.property.UniquePropertyList;
import seedu.address.model.tag.Tag;

/**
 * Represents a person in the address book.
 * <p>
 * The person is immutable from the outside: clients obtain read-only views of tags and owned properties.
 * Owned properties are stored in a {@link UniquePropertyList} and exposed as an unmodifiable {@link ObservableList}.
 * Interested properties are kept as an internal list and exposed as an unmodifiable {@link List}.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final UniquePropertyList ownedProperties;
    private final Set<Tag> tags = new HashSet<>();
    private final List<Property> interestedProperties = new ArrayList<>();

    /**
     * Constructs a {@code Person} with all fields and initial lists.
     *
     * @param name            the person's name
     * @param phone           the person's phone
     * @param email           the person's email
     * @param address         the person's address
     * @param tags            the set of tags
     * @param ownedProps      initial owned properties; duplicates (by identity) are rejected
     *                        by {@link UniquePropertyList#setProperties(List)}
     * @param interestedProps initial interested properties list; may be empty or {@code null}
     * @throws NullPointerException if any required field is {@code null}
     * @throws seedu.address.model.property.exceptions.DuplicatePropertyException if {@code ownedProps}
     *         contains duplicate properties by identity
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags,
                  List<Property> ownedProps, List<Property> interestedProps) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);

        this.ownedProperties = new UniquePropertyList();
        if (ownedProps != null && !ownedProps.isEmpty()) {
            this.ownedProperties.setProperties(ownedProps);
        }
        if (interestedProps != null && !interestedProps.isEmpty()) {
            this.interestedProperties.addAll(interestedProps);
        }
    }

    /**
     * Constructs a {@code Person} with owned properties and empty interested properties.
     *
     * @param name       the person's name
     * @param phone      the person's phone
     * @param email      the person's email
     * @param address    the person's address
     * @param tags       the set of tags
     * @param ownedProps initial owned properties
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags,
                  List<Property> ownedProps) {
        this(name, phone, email, address, tags, ownedProps, List.of());
    }

    /**
     * Constructs a {@code Person} with empty owned and interested properties.
     *
     * @param name    the person's name
     * @param phone   the person's phone
     * @param email   the person's email
     * @param address the person's address
     * @param tags    the set of tags
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        this(name, phone, email, address, tags, List.of(), List.of());
    }

    /**
     * Returns the person's name.
     *
     * @return the {@link Name}
     */
    public Name getName() {
        return name;
    }

    /**
     * Returns the person's phone.
     *
     * @return the {@link Phone}
     */
    public Phone getPhone() {
        return phone;
    }

    /**
     * Returns the person's email.
     *
     * @return the {@link Email}
     */
    public Email getEmail() {
        return email;
    }

    /**
     * Returns the person's address.
     *
     * @return the {@link Address}
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Returns an unmodifiable view of the person's owned properties.
     *
     * @return an unmodifiable {@link ObservableList} of {@link Property}
     */
    public ObservableList<Property> getOwnedProperties() {
        return ownedProperties.asUnmodifiableObservableList();
    }

    /**
     * Returns an unmodifiable view of the person's tags.
     *
     * @return an unmodifiable {@link Set} of {@link Tag}
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns an unmodifiable view of the person's interested properties.
     *
     * @return an unmodifiable {@link List} of {@link Property}
     */
    public List<Property> getInterestedProperties() {
        return Collections.unmodifiableList(interestedProperties);
    }

    /**
     * Checks if another person has the same identity as this person.
     * Identity is defined by the {@link Name}.
     *
     * @param otherPerson the other person to compare
     * @return {@code true} if both refer to the same person by name; {@code false} otherwise
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }
        return otherPerson != null && otherPerson.getName().equals(getName());
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * Two persons are considered equal if all identity and data fields match.
     *
     * @param other the reference object with which to compare
     * @return {@code true} if this object is the same as the {@code other} argument; {@code false} otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Person)) {
            return false;
        }
        Person o = (Person) other;
        return name.equals(o.name)
                && phone.equals(o.phone)
                && email.equals(o.email)
                && address.equals(o.address)
                && tags.equals(o.tags)
                && ownedProperties.equals(o.ownedProperties)
                && interestedProperties.equals(o.interestedProperties);
    }

    /**
     * Returns a hash code value for the person, consistent with {@link #equals(Object)}.
     *
     * @return the hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, phone, email, address, tags, ownedProperties, interestedProperties);
    }

    /**
     * Returns a string representation of the person and its fields.
     *
     * @return a string describing this person
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("tags", tags)
                .add("ownedProperties", ownedProperties)
                .add("interested", interestedProperties)
                .toString();
    }

    /**
     * Returns whether all fields of this person satisfy their validation rules.
     *
     * @return {@code true} if name, phone, email, and address are all valid; {@code false} otherwise
     */
    public boolean isFullyValid() {
        return Name.isValidName(name.fullName)
                && Phone.isValidPhone(phone.value)
                && Email.isValidEmail(email.value)
                && Address.isValidAddress(address.value);
    }
}
