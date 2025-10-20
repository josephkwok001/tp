package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.property.Property;
import seedu.address.model.property.UniquePropertyList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;
    private final UniquePropertyList properties;
    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        properties = new UniquePropertyList();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    /**
     * Returns true if the address book contains an equivalent property as the given argument.
     *
     * @param property the property to check for presence in this address book
     * @return true if an equivalent property exists in the address book, false otherwise
     * @throws NullPointerException if {@code property} is null
     */
    public boolean hasProperty(Property property) {
        requireNonNull(property);
        return properties.contains(property);
    }

    /**
     * Adds the given property to the address book.
     * The property must not already exist in the address book.
     *
     * @param property the property to add
     * @throws NullPointerException if {@code property} is null
     * @throws DuplicatePropertyException if the property already exists in the address book
     */
    public void addProperty(Property property) {
        properties.add(property);
    }

    /**
     * Replaces the given target property in the address book with {@code editedProperty}.
     * {@code target} must exist in the address book.
     * The identity of {@code editedProperty} must not be the same as another existing property
     * in the address book.
     *
     * @param target the property to be replaced
     * @param editedProperty the property to replace with
     * @throws NullPointerException if {@code target} or {@code editedProperty} is null
     * @throws PropertyNotFoundException if the {@code target} does not exist in the address book
     * @throws DuplicatePropertyException if the replacement would result in a duplicate property
     */
    public void setProperty(Property target, Property editedProperty) {
        properties.setProperty(target, editedProperty);
    }

    /**
     * Removes the equivalent property from the address book.
     * The property must exist in the address book.
     *
     * @param toRemove the property to remove
     * @throws NullPointerException if {@code toRemove} is null
     * @throws PropertyNotFoundException if the property does not exist in the address book
     */
    public void removeProperty(Property toRemove) {
        properties.remove(toRemove);
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .toString();
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Property> getPropertyList() {
        return properties.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBook)) {
            return false;
        }

        AddressBook otherAddressBook = (AddressBook) other;
        return persons.equals(otherAddressBook.persons) && properties.equals(otherAddressBook.properties);
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }
}
