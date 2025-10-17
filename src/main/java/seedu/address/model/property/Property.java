package seedu.address.model.property;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.List;
import java.util.Objects;

// import seedu.address.model.person.Person;

/**
 * Represents a Property in the address book.
 * Guarantees: details are present and not null, field values are validated,
 * immutable.
 */
public class Property {

    // private final Person owner;
    private final String owner;
    private final Address address;
    private final Price price;
    // private final List<Person> interestedPersons;
    private final String propertyName;

    /**
     * Every field must be present and not null.
     */
    public Property(String owner, Address address, Price price, String propertyName) {
        // , List<Person> interestedPersons) {
        requireAllNonNull(owner, address, price); // , interestedPersons);
        this.owner = owner;
        this.address = address;
        this.price = price;
        this.propertyName = propertyName;
        // this.interestedPersons = new ArrayList<>(interestedPersons);
    }

    public String getOwner() {
        return owner;
    }

    public Address getAddress() {
        return address;
    }

    public Price getPrice() {
        return price;
    }

    public String getPropertyName() {
        return propertyName;
    }

    // /**
    // * Returns an unmodifiable view of the interested persons list.
    // */
    // public List<Person> getInterestedPersons() {
    // return Collections.unmodifiableList(interestedPersons);
    // }

    // /**
    // * Returns true if the person is interested in this property.
    // */
    // public boolean hasInterestedPerson(Person person) {
    // requireAllNonNull(person);
    // return interestedPersons.stream().anyMatch(p -> p.equals(person));
    // }

    // /**
    // * Adds an interested person to this property.
    // * The person must not be the owner of the property.
    // */
    // public Property addInterestedPerson(Person person) {
    // requireAllNonNull(person);
    // if (person.equals(owner)) {
    // throw new IllegalArgumentException("Owner cannot be added as an interested
    // person");
    // }
    // List<Person> updatedList = new ArrayList<>(interestedPersons);
    // updatedList.add(person);
    // return new Property(owner, address, price, updatedList);
    // }

    // /**
    // * Removes an interested person from this property.
    // */
    // public Property removeInterestedPerson(Person person) {
    // requireAllNonNull(person);
    // List<Person> updatedList = new ArrayList<>(interestedPersons);
    // updatedList.remove(person);
    // return new Property(owner, address, price, updatedList);
    // }

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
        return owner.equals(otherProperty.owner)
                && address.equals(otherProperty.address)
                && price.equals(otherProperty.price)
                && propertyName.equals(otherProperty.propertyName);
        // && interestedPersons.equals(otherProperty.interestedPersons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, address, price); // , interestedPersons);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Property owned by: ")
                .append(getOwner())
                .append(" Address: ")
                .append(getAddress())
                .append(" Price: $")
                .append(getPrice())
                .append(" Property Name: ")
                .append(getPropertyName());
        // .append(" Interested Persons: ")
        // .append(getInterestedPersons().size());
        return builder.toString();
    }
}
