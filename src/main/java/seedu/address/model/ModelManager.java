package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;
import seedu.address.model.property.Property;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final FilteredList<Property> filteredProperties;


    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        filteredProperties = new FilteredList<>(this.addressBook.getPropertyList());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public boolean hasProperty(Property property) {
        requireNonNull(property);
        return addressBook.hasProperty(property);
    }

    @Override
    public void deletePerson(Person target) {
        addressBook.removePerson(target);
    }

    @Override
    public void deleteProperty(Property property) {
        addressBook.removeProperty(property);
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void removePropertyFromAllPersons(Property propertyToDelete) {
        requireNonNull(propertyToDelete);

        List<Person> allPersons = addressBook.getPersonList();
        for (Person person : allPersons) {
            boolean changed = false;

            if (person.getOwnedProperties().contains(propertyToDelete)) {
                person.removeOwnedProperty(propertyToDelete);
                changed = true;
            }

            if (person.getInterestedProperties().contains(propertyToDelete)) {
                person.removeInterestedProperty(propertyToDelete);
                changed = true;
            }

            if (changed) {
                addressBook.setPerson(person, person);
            }
        }
    }

    @Override
    public void addProperty(Property property) {
        addressBook.addProperty(property);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        addressBook.setPerson(target, editedPerson);
    }

    @Override
    public void setProperty(Property target, Property editedProperty) {
        requireAllNonNull(target, editedProperty);

        addressBook.setProperty(target, editedProperty);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public ObservableList<Property> getFilteredPropertyList() {
        return filteredProperties;
    }

    @Override
    public void updateFilteredPropertyList(Predicate<Property> predicate) {
        requireNonNull(predicate);
        filteredProperties.setPredicate(predicate);
    }

    @Override
    public void updatePropertyInAllPersons(Property oldProperty, Property newProperty) {
        requireAllNonNull(oldProperty, newProperty);

        List<Person> allPersons = addressBook.getPersonList();
        for (Person person : allPersons) {
            boolean hasOwnedProperty = person.getOwnedProperties().contains(oldProperty);
            boolean hasInterestedProperty = person.getInterestedProperties().contains(oldProperty);

            if (hasOwnedProperty || hasInterestedProperty) {
                List<Property> updatedOwnedProperties =
                        new java.util.ArrayList<>(person.getOwnedProperties());
                List<Property> updatedInterestedProperties =
                        new java.util.ArrayList<>(person.getInterestedProperties());

                if (hasOwnedProperty) {
                    int index = updatedOwnedProperties.indexOf(oldProperty);
                    updatedOwnedProperties.set(index, newProperty);
                }

                if (hasInterestedProperty) {
                    int index = updatedInterestedProperties.indexOf(oldProperty);
                    updatedInterestedProperties.set(index, newProperty);
                }

                Person updatedPerson = new Person(
                        person.getName(),
                        person.getPhone(),
                        person.getEmail(),
                        person.getAddress(),
                        person.getTags(),
                        updatedOwnedProperties,
                        updatedInterestedProperties
                );

                addressBook.setPerson(person, updatedPerson);
            }
        }
    }


    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons);
    }

}
