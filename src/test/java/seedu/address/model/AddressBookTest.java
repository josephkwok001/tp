package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.property.Address;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyName;
import seedu.address.model.property.exceptions.DuplicatePropertyException;
import seedu.address.model.property.exceptions.PropertyNotFoundException;
import seedu.address.testutil.PersonBuilder;


public class AddressBookTest {

    private final AddressBook addressBook = new AddressBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), addressBook.getPersonList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Person> newPersons = Arrays.asList(ALICE, editedAlice);
        AddressBookStub newData = new AddressBookStub(newPersons);

        assertThrows(DuplicatePersonException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        assertTrue(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(addressBook.hasPerson(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getPersonList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = AddressBook.class.getCanonicalName() + "{persons=" + addressBook.getPersonList() + "}";
        assertEquals(expected, addressBook.toString());
    }

    /**
     * Ensures property list is empty on construction.
     */
    @Test
    public void constructor_propertyListEmpty() {
        assertEquals(Collections.emptyList(), addressBook.getPropertyList());
    }

    /**
     * Ensures getPropertyList() exposes an unmodifiable view.
     */
    @Test
    public void getPropertyList_modify_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getPropertyList().remove(0));
    }

    /**
     * Adding a duplicate person should throw DuplicatePersonException.
     */
    @Test
    public void addPerson_duplicate_throwsDuplicatePersonException() {
        addressBook.addPerson(ALICE);
        assertThrows(DuplicatePersonException.class, () -> addressBook.addPerson(new PersonBuilder(ALICE).build()));
    }

    @Test
    public void setProperty_nullEditedProperty_throwsNullPointerException() {
        AddressBook ab = new AddressBook();
        Property target = new Property(new Address("A"), new Price(100), new PropertyName("N"));
        // editedProperty null -> requireNonNull should throw
        assertThrows(NullPointerException.class, () -> ab.setProperty(target, null));
    }

    @Test
    public void setProperty_success_replacesProperty() {
        AddressBook ab = new AddressBook();
        Property original = new Property(new Address("A1"), new Price(100), new PropertyName("NameA"));
        ab.addProperty(original);

        Property edited = new Property(new Address("A2"), new Price(200), new PropertyName("NameAEdited"));
        ab.setProperty(original, edited);

        assertEquals(1, ab.getPropertyList().size());
        assertEquals(edited, ab.getPropertyList().get(0));
    }

    @Test
    public void setProperty_targetNotFound_throwsPropertyNotFoundException() {
        AddressBook ab = new AddressBook();
        Property target = new Property(new Address("A"), new Price(100), new PropertyName("N"));
        Property edited = new Property(new Address("B"), new Price(200), new PropertyName("N2"));
        assertThrows(PropertyNotFoundException.class, () -> ab.setProperty(target, edited));
    }

    @Test
    public void setProperty_replacingWithDuplicate_throwsDuplicatePropertyException() {
        AddressBook ab = new AddressBook();
        Property p1 = new Property(new Address("A1"), new Price(100), new PropertyName("NameA"));
        Property p2 = new Property(new Address("A2"), new Price(200), new PropertyName("NameB"));
        ab.addProperty(p1);
        ab.addProperty(p2);

        // attempt to edit p1 so its identity matches p2 (e.g. same name), expecting duplicate exception
        Property edited = new Property(new Address("A3"), new Price(300), new PropertyName("NameB"));
        assertThrows(DuplicatePropertyException.class, () -> ab.setProperty(p1, edited));
    }

    /**
     * A stub ReadOnlyAddressBook whose persons list can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();
        private final ObservableList<Property> properties = FXCollections.observableArrayList();

        AddressBookStub(Collection<Person> persons) {
            this.persons.setAll(persons);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }

        @Override
        public ObservableList<Property> getPropertyList() {
            return properties;
        }
    }
}
