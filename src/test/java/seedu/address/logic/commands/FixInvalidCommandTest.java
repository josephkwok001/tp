package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Listing;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.storage.LoadReport;
import seedu.address.storage.Storage;

/**
 * Tests for FixInvalidCommand.
 * This version is fully Checkstyle-compliant and contains English inline comments.
 */
public class FixInvalidCommandTest {

    /**
     * A minimal Model stub that only supports the operations needed by FixInvalidCommand.
     */
    static class ModelStub implements Model {
        final List<Person> persons = new ArrayList<>();

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) { }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            return null;
        }

        @Override
        public GuiSettings getGuiSettings() {
            return null;
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
        public ReadOnlyAddressBook getAddressBook() {
            return null;
        }

        @Override
        public boolean hasPerson(Person person) {
            return persons.stream().anyMatch(person::isSamePerson);
        }

        @Override
        public void deletePerson(Person target) { }

        @Override
        public void addPerson(Person person) {
            persons.add(person);
        }

        @Override
        public void setPerson(Person target, Person editedPerson) { }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return null;
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) { }
    }

    /**
     * Tests that a valid FixInvalidCommand executes successfully and returns the correct message.
     */
    @Test
    public void execute_addsPerson_whenValid() throws Exception {
        ModelStub model = new ModelStub();

        // Create a valid Person to fix
        Person p = new Person(
                new Name("Alice"),
                new Phone("91234567"),
                new Email("a@b.com"),
                new Address("Blk 1"),
                new Listing("HDB"),
                new HashSet<>()
        );

        // Storage stub that simulates a successful overwrite with an empty invalid list
        Storage okStorage = new Storage() {
            @Override
            public LoadReport overwriteRawEntryAtIndex(int index, Person corrected) {
                // Return a dummy LoadReport indicating success
                return new LoadReport(
                        new LoadReport.ModelData(new seedu.address.model.AddressBook()),
                        java.util.Collections.emptyList()
                );
            }

            @Override
            public java.util.Optional<seedu.address.model.UserPrefs> readUserPrefs() {
                return java.util.Optional.empty();
            }

            @Override
            public void saveUserPrefs(seedu.address.model.ReadOnlyUserPrefs prefs) { }

            @Override
            public Path getUserPrefsFilePath() {
                return Path.of("dummy.json");
            }

            @Override
            public Path getAddressBookFilePath() {
                return Path.of("dummy.json");
            }

            @Override
            public java.util.Optional<seedu.address.model.ReadOnlyAddressBook> readAddressBook() {
                return java.util.Optional.empty();
            }

            @Override
            public java.util.Optional<seedu.address.model.ReadOnlyAddressBook> readAddressBook(Path path) {
                return java.util.Optional.empty();
            }

            @Override
            public void saveAddressBook(seedu.address.model.ReadOnlyAddressBook ab) { }

            @Override
            public void saveAddressBook(seedu.address.model.ReadOnlyAddressBook ab, Path path) { }

            @Override
            public LoadReport readAddressBookWithReport() {
                return new LoadReport(
                        new LoadReport.ModelData(new seedu.address.model.AddressBook()),
                        java.util.Collections.emptyList()
                );
            }

            @Override
            public LoadReport readAddressBookWithReport(Path path) {
                return new LoadReport(
                        new LoadReport.ModelData(new seedu.address.model.AddressBook()),
                        java.util.Collections.emptyList()
                );
            }
        };

        // Run the command
        FixInvalidCommand cmd = new FixInvalidCommand(0, p, okStorage);
        CommandResult res = cmd.execute(model);

        // Verify output message only (the model is not updated directly anymore)
        assertEquals("Fixed invalid entry at index 0.", res.getFeedbackToUser());
    }

    /**
     * Tests that FixInvalidCommand throws CommandException when the storage fails to overwrite.
     */
    @Test
    public void execute_duplicate_throwsCommandException() {
        ModelStub model = new ModelStub();

        // Add one existing person to simulate a duplicate entry
        model.persons.add(new Person(
                new Name("Alice"),
                new Phone("91234567"),
                new Email("a@b.com"),
                new Address("Blk 1"),
                new Listing("HDB"),
                new HashSet<Tag>()
        ));

        // Create another identical Person (duplicate)
        Person p = new Person(
                new Name("Alice"),
                new Phone("91234567"),
                new Email("a@b.com"),
                new Address("Blk 1"),
                new Listing("HDB"),
                new HashSet<>()
        );

        // Storage stub that simulates a failure (throws IOException)
        Storage failingStorage = new Storage() {
            @Override
            public LoadReport overwriteRawEntryAtIndex(int index, Person corrected)
                    throws java.io.IOException {
                throw new java.io.IOException("Simulated write failure");
            }

            @Override
            public java.util.Optional<seedu.address.model.UserPrefs> readUserPrefs() {
                return java.util.Optional.empty();
            }

            @Override
            public void saveUserPrefs(seedu.address.model.ReadOnlyUserPrefs prefs) { }

            @Override
            public Path getUserPrefsFilePath() {
                return Path.of("dummy.json");
            }

            @Override
            public Path getAddressBookFilePath() {
                return Path.of("dummy.json");
            }

            @Override
            public java.util.Optional<seedu.address.model.ReadOnlyAddressBook> readAddressBook() {
                return java.util.Optional.empty();
            }

            @Override
            public java.util.Optional<seedu.address.model.ReadOnlyAddressBook> readAddressBook(Path path) {
                return java.util.Optional.empty();
            }

            @Override
            public void saveAddressBook(seedu.address.model.ReadOnlyAddressBook ab) { }

            @Override
            public void saveAddressBook(seedu.address.model.ReadOnlyAddressBook ab, Path path) { }

            @Override
            public LoadReport readAddressBookWithReport() {
                return null;
            }

            @Override
            public LoadReport readAddressBookWithReport(Path path) {
                return null;
            }
        };

        // Expect CommandException because overwriteRawEntryAtIndex fails
        FixInvalidCommand cmd = new FixInvalidCommand(0, p, failingStorage);
        assertThrows(CommandException.class, () -> cmd.execute(model));
    }
}
