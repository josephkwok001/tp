package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.OwnedProperties;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.tag.Tag;

/**
 * Tests for {@link SetOwnedPropertyCommand}.
 */
public class SetOwnedPropertyCommandTest {

    @Test
    public void execute_success_addsPropertyToPerson() throws Exception {
        Person alex = new Person(
                new Name("Alex Yeoh"),
                new Phone("87438807"),
                new Email("alexyeoh@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"),
                Set.of(new Tag("friends")),
                new OwnedProperties(java.util.Collections.emptyList())
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
                result.getFeedbackToUser());

        OwnedProperties expectedOwned = alex.getOwnedProperties().withAdded(cityLoft);
        Person expected = new Person(
                alex.getName(),
                alex.getPhone(),
                alex.getEmail(),
                alex.getAddress(),
                alex.getTags(),
                expectedOwned
        );

        assertEquals(expected, model.persons.get(0));
    }

    @Test
    public void execute_propertyNotFound_throwsCommandException() {
        Person alex = new Person(
                new Name("Alex Yeoh"),
                new Phone("87438807"),
                new Email("alexyeoh@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"),
                Set.of(),
                new OwnedProperties(java.util.Collections.emptyList())
        );

        AddressBook ab = new AddressBook();
        ab.addPerson(alex);

        ModelStub model = new ModelStub(FXCollections.observableArrayList(alex), ab);

        SetOwnedPropertyCommand cmd = new SetOwnedPropertyCommand(Index.fromOneBased(1), "No Such Property");
        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertEquals(String.format(SetOwnedPropertyCommand.MESSAGE_PROP_NOT_FOUND, "No Such Property"),
                ex.getMessage());
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Person alex = new Person(
                new Name("Alex Yeoh"),
                new Phone("87438807"),
                new Email("alexyeoh@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"),
                Set.of(),
                new OwnedProperties(java.util.Collections.emptyList())
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
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new UnsupportedOperationException();
        }

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
            throw new UnsupportedOperationException();
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook addressBook) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasPerson(Person person) {
            return false;
        }

        @Override
        public void deletePerson(Person target) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addPerson(Person person) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) { }

        @Override
        public void setProperty(Property target, Property editedProperty) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasProperty(Property property) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteProperty(Property target) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addProperty(Property property) {
            throw new UnsupportedOperationException();
        }

    }
}
