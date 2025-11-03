package seedu.address.logic.commands.property;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalProperties.PROPERTY_A;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.property.Property;
import seedu.address.testutil.PropertyBuilder;

public class AddPropertyCommandTest {

    @Test
    public void constructor_nullProperty_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddPropertyCommand(null));
    }

    @Test
    public void execute_propertyAcceptedByModel_addSuccessful() throws Exception {
        AddPropertyCommandTest.ModelStubAcceptingPropertyAdded modelStub = new AddPropertyCommandTest
            .ModelStubAcceptingPropertyAdded();
        Property validProperty = new PropertyBuilder().build();

        CommandResult commandResult = new AddPropertyCommand(validProperty).execute(modelStub);

        assertEquals(String.format(AddPropertyCommand.MESSAGE_SUCCESS, Messages.formatProperty(validProperty)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validProperty), modelStub.propertiesAdded);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Property validProperty = new PropertyBuilder().build();
        AddPropertyCommand addPropertyCommand = new AddPropertyCommand(validProperty);
        AddPropertyCommandTest.ModelStub modelStub = new AddPropertyCommandTest.ModelStubWithProperty(validProperty);

        assertThrows(CommandException.class, AddPropertyCommand.MESSAGE_DUPLICATE_PROPERTY, () -> addPropertyCommand
                .execute(modelStub));
    }

    @Test
    public void equals() {
        Property duxton = new PropertyBuilder().withName("The Pinnacle at Duxton").build();
        Property dawson = new PropertyBuilder().withName("SkyVille at Dawson").build();
        AddPropertyCommand addDuxtonCommand = new AddPropertyCommand(duxton);
        AddPropertyCommand addDawsonCommand = new AddPropertyCommand(dawson);

        // same object -> returns true
        assertTrue(addDuxtonCommand.equals(addDuxtonCommand));

        // same values -> returns true
        AddPropertyCommand addDuxtonCommandCopy = new AddPropertyCommand(duxton);
        assertTrue(addDuxtonCommand.equals(addDuxtonCommandCopy));

        // different types -> returns false
        assertFalse(addDuxtonCommandCopy.equals(1));

        // null -> returns false
        assertFalse(addDawsonCommand.equals(null));

        // different person -> returns false
        assertFalse(addDuxtonCommand.equals(addDawsonCommand));
    }

    @Test
    public void toStringMethod() {
        AddPropertyCommand addPropertyCommand = new AddPropertyCommand(PROPERTY_A);
        String expected = AddPropertyCommand.class.getCanonicalName() + "{toAddProperty=" + PROPERTY_A + "}";
        assertEquals(expected, addPropertyCommand.toString());
    }

    /**
     * Adds a property when an existing property has a similar name differing only by case/spacing.
     * Expects success message followed by a warning about the similar name.
     */
    @Test
    public void execute_propertyWithSimilarName_warnsButAdds() throws Exception {
        Property existing = new PropertyBuilder()
                .withName("Hillside Villa")
                .withAddress("311, Clementi Ave 2, #02-25")
                .withPrice(1000000)
                .build();

        Property toAdd = new PropertyBuilder()
                .withName("hillside   villa")
                .withAddress("10, Dover Road, #01-01")
                .withPrice(999999)
                .build();

        AddressBook seeded = new AddressBook();
        seeded.addProperty(existing);
        ModelStubAcceptingPropertyAddedWithSeed modelStub =
                new ModelStubAcceptingPropertyAddedWithSeed(seeded);

        CommandResult commandResult = new AddPropertyCommand(toAdd).execute(modelStub);

        String expected = String.format(AddPropertyCommand.MESSAGE_SUCCESS, Messages.formatProperty(toAdd))
                + String.format(
                "\nWarning: A similar property name already exists: \"%s\" (differs only by spacing/case).",
                existing.getPropertyName().toString());

        assertEquals(expected, commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(toAdd), modelStub.propertiesAdded);
    }

    /**
     * A Model stub that is pre-seeded with properties in its AddressBook and accepts new properties.
     * Duplicate detection is based on isSameProperty and the seeded data is visible through getAddressBook().
     */
    private class ModelStubAcceptingPropertyAddedWithSeed extends AddPropertyCommandTest.ModelStub {
        final ArrayList<Property> propertiesAdded = new ArrayList<>();
        final AddressBook backing;

        ModelStubAcceptingPropertyAddedWithSeed(ReadOnlyAddressBook seed) {
            this.backing = new AddressBook(seed);
        }

        @Override
        public boolean hasProperty(Property property) {
            requireNonNull(property);
            return propertiesAdded.stream().anyMatch(property::isSameProperty)
                    || backing.getPropertyList().stream().anyMatch(property::isSameProperty);
        }

        @Override
        public void addProperty(Property property) {
            requireNonNull(property);
            propertiesAdded.add(property);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return backing;
        }
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addProperty(Property property) {

        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasProperty(Property property) {
            return false;
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteProperty(Property target) {

        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setProperty(Property target, Property editedProperty) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Property> getFilteredPropertyList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPropertyList(Predicate<Property> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void removePropertyFromAllPersons(Property propertyToDelete) {
        }

        @Override
        public void updatePropertyInAllPersons(Property oldProperty, Property newProperty) {
        }
    }

    /**
     * A Model stub that contains a single person.
     */
    private class ModelStubWithProperty extends AddPropertyCommandTest.ModelStub {
        private final Property property;

        ModelStubWithProperty(Property property) {
            requireNonNull(property);
            this.property = property;
        }

        @Override
        public boolean hasProperty(Property property) {
            requireNonNull(property);
            return this.property.isSameProperty(property);
        }
    }

    /**
     * A Model stub that always accept the property being added.
     */
    private class ModelStubAcceptingPropertyAdded extends AddPropertyCommandTest.ModelStub {
        final ArrayList<Property> propertiesAdded = new ArrayList<>();

        @Override
        public boolean hasProperty(Property property) {
            requireNonNull(property);
            return propertiesAdded.stream().anyMatch(property::isSameProperty);
        }

        @Override
        public void addProperty(Property property) {
            requireNonNull(property);
            propertiesAdded.add(property);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }
}
