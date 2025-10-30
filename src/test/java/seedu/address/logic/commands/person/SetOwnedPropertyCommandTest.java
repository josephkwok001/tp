package seedu.address.logic.commands.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.tag.Tag;

/**
 * Unit tests for {@link SetOwnedPropertyCommand}.
 */
public class SetOwnedPropertyCommandTest {

    /**
     * Successfully adds an owned property and verifies unified success format:
     * "Set owned property for {person}: {property}".
     */
    @Test
    public void execute_success_addsPropertyToPerson() throws Exception {
        Person alex = new Person(
                new Name("Alex Yeoh"),
                new Phone("87438807"),
                new Email("alexyeoh@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"),
                Set.of(new Tag("friends"))
        );

        Property cityLoft = new Property(
                new seedu.address.model.property.Address("45 Orchard Rd"),
                new Price(1200000),
                new seedu.address.model.property.PropertyName("City Loft"));

        AddressBook ab = new AddressBook();
        ab.addPerson(alex);
        ab.addProperty(cityLoft);

        ModelStub model = new ModelStub(FXCollections.observableArrayList(alex), ab);

        SetOwnedPropertyCommand cmd = new SetOwnedPropertyCommand(Index.fromOneBased(1), "City Loft");
        CommandResult result = cmd.execute(model);

        assertEquals(
                String.format(SetOwnedPropertyCommand.MESSAGE_SUCCESS, "Alex Yeoh", "City Loft"),
                result.getFeedbackToUser()
        );

        List<Property> expectedOwned = new ArrayList<>();
        expectedOwned.add(cityLoft);
        Person expected = new Person(
                alex.getName(),
                alex.getPhone(),
                alex.getEmail(),
                alex.getAddress(),
                alex.getTags(),
                expectedOwned,
                List.of()
        );

        assertEquals(expected, model.persons.get(0));
    }

    /**
     * Fails when the property name is not found in the address book.
     */
    @Test
    public void execute_propertyNotFound_throwsCommandException() {
        Person alex = new Person(
                new Name("Alex Yeoh"),
                new Phone("87438807"),
                new Email("alexyeoh@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"),
                Set.of(),
                List.of(),
                List.of()
        );

        AddressBook ab = new AddressBook();
        ab.addPerson(alex);

        ModelStub model = new ModelStub(FXCollections.observableArrayList(alex), ab);

        SetOwnedPropertyCommand cmd = new SetOwnedPropertyCommand(Index.fromOneBased(1), "No Such Property");
        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertEquals(String.format(SetOwnedPropertyCommand.MESSAGE_PROP_NOT_FOUND, "No Such Property"),
                ex.getMessage());
    }

    /**
     * Fails when the person index is invalid.
     */
    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Person alex = new Person(
                new Name("Alex Yeoh"),
                new Phone("87438807"),
                new Email("alexyeoh@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"),
                Set.of(),
                List.of(),
                List.of()
        );

        Property sunny = new Property(
                new seedu.address.model.property.Address("123 Example St"),
                new Price(500000),
                new seedu.address.model.property.PropertyName("Sunny Villa"));

        AddressBook ab = new AddressBook();
        ab.addPerson(alex);
        ab.addProperty(sunny);

        ModelStub model = new ModelStub(FXCollections.observableArrayList(alex), ab);

        SetOwnedPropertyCommand cmd = new SetOwnedPropertyCommand(Index.fromOneBased(2), "Sunny Villa");
        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertEquals(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, ex.getMessage());
    }

    /**
     * Fails with duplicate when the person already owns the property.
     */
    @Test
    public void execute_duplicateOwned_throwsCommandException() throws Exception {
        Person alex = new Person(
                new Name("Alex Yeoh"),
                new Phone("87438807"),
                new Email("alexyeoh@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"),
                Set.of(),
                List.of(),
                List.of()
        );

        Property sunny = new Property(
                new seedu.address.model.property.Address("123 Example St"),
                new Price(500000),
                new seedu.address.model.property.PropertyName("Sunny Villa"));

        AddressBook ab = new AddressBook();
        ab.addPerson(alex);
        ab.addProperty(sunny);

        ModelStub model = new ModelStub(FXCollections.observableArrayList(alex), ab);

        new SetOwnedPropertyCommand(Index.fromOneBased(1), "Sunny Villa").execute(model);

        CommandException ex = assertThrows(CommandException.class, () ->
                new SetOwnedPropertyCommand(Index.fromOneBased(1),
                        "Sunny Villa").execute(model));
        assertEquals(String.format(SetOwnedPropertyCommand.MESSAGE_DUPLICATE_PROP, "Sunny Villa"), ex.getMessage());
    }

    /**
     * Fails with interest conflict when the person is already interested in the same property.
     */
    @Test
    public void execute_interestConflict_throwsCommandException() {
        Property sunny = new Property(
                new seedu.address.model.property.Address("123 Example St"),
                new Price(500000),
                new seedu.address.model.property.PropertyName("Sunny Villa"));

        Person alex = new Person(
                new Name("Alex Yeoh"),
                new Phone("87438807"),
                new Email("alexyeoh@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"),
                Set.of(),
                List.of(),
                List.of(sunny)
        );

        AddressBook ab = new AddressBook();
        ab.addPerson(alex);
        ab.addProperty(sunny);

        ModelStub model = new ModelStub(FXCollections.observableArrayList(alex), ab);

        CommandException ex = assertThrows(CommandException.class, () ->
                new SetOwnedPropertyCommand(Index.fromOneBased(1),
                        "Sunny Villa").execute(model));
        assertEquals(
                String.format(SetOwnedPropertyCommand.MESSAGE_INTEREST_CONFLICT, "Sunny Villa"),
                ex.getMessage()
        );
    }

    private static class ModelStub implements Model {
        final ObservableList<Person> persons;
        final AddressBook ab;

        ModelStub(ObservableList<Person> persons, AddressBook ab) {
            this.persons = persons;
            this.ab = ab;
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return persons;
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            int idx = persons.indexOf(target);
            persons.set(idx, editedPerson);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return ab;
        }

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) { }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            return new UserPrefs();
        }

        @Override
        public GuiSettings getGuiSettings() {
            return new GuiSettings();
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) { }

        @Override
        public Path getAddressBookFilePath() {
            return null;
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) { }

        @Override
        public void setAddressBook(ReadOnlyAddressBook addressBook) { }

        @Override
        public boolean hasPerson(Person person) {
            return false;
        }

        @Override
        public void deletePerson(Person target) { }

        @Override
        public void addPerson(Person person) { }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) { }

        @Override
        public void setProperty(Property target, Property editedProperty) { }

        @Override
        public boolean hasProperty(Property property) {
            return false;
        }

        @Override
        public void deleteProperty(Property target) { }

        @Override
        public void addProperty(Property property) { }

        @Override
        public ObservableList<Property> getFilteredPropertyList() {
            return FXCollections.observableArrayList();
        }

        @Override
        public void updateFilteredPropertyList(Predicate<Property> predicate) { }

        @Override
        public void removePropertyFromAllPersons(Property propertyToDelete) { }

        @Override
        public void updatePropertyInAllPersons(Property oldProperty, Property newProperty) { }
    }
}
