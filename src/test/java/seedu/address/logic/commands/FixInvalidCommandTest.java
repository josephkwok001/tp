package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.property.Property;
import seedu.address.storage.LoadReport;
import seedu.address.storage.Storage;

/**
 * Tests for {@link FixInvalidCommand}.
 * This test file is checkstyle-compliant and uses English inline comments.
 */
public class FixInvalidCommandTest {

    /* ===========================
     * Minimal Model stub for tests
     * =========================== */
    private static class ModelStub implements Model {
        final List<Person> persons = new ArrayList<>();
        final List<Property> properties = new ArrayList<>();

        private ReadOnlyAddressBook lastSetAddressBook;
        ReadOnlyAddressBook getLastSetAddressBook() {
            return lastSetAddressBook;
        }

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {}

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            return null;
        }

        @Override
        public GuiSettings getGuiSettings() {
            return null;
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {}

        @Override
        public Path getAddressBookFilePath() {
            return null;
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {}

        @Override
        public void setAddressBook(ReadOnlyAddressBook addressBook) {
            // FixInvalidCommand calls this; capture for potential assertions.
            this.lastSetAddressBook = addressBook;
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return null;
        }

        @Override
        public boolean hasPerson(Person person) {
            return persons.stream().anyMatch(person::isSamePerson);
        }

        @Override
        public boolean hasProperty(Property property) {
            return false;
        }

        @Override
        public void deletePerson(Person target) {}

        @Override
        public void deleteProperty(Property target) {}

        @Override
        public void addPerson(Person person) {
            persons.add(person);
        }

        @Override
        public void addProperty(Property property) {

        }

        @Override
        public void setPerson(Person target, Person editedPerson) {}

        @Override
        public void setProperty(Property target, Property editedProperty) {}

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return null;
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {}

        @Override
        public ObservableList<Property> getFilteredPropertyList() {
            return null;
        }

        @Override
        public void updateFilteredPropertyList(Predicate<Property> predicate) {}

    }

    /* ===========================
     * Storage test doubles
     * =========================== */

    /** Storage stub that simulates a successful overwrite and returns an empty report (no invalids). */
    private static class StorageSuccess implements Storage {
        @Override
        public LoadReport overwriteRawEntryAtIndex(int index, Person corrected) {
            // Return a report with an empty (fresh) AddressBook and no invalids.
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
        public void saveUserPrefs(ReadOnlyUserPrefs prefs) {}

        @Override
        public Path getUserPrefsFilePath() {
            return Path.of("dummy.json");
        }

        @Override
        public Path getAddressBookFilePath() {
            return Path.of("dummy.json");
        }

        @Override
        public java.util.Optional<ReadOnlyAddressBook> readAddressBook() {
            return java.util.Optional.empty();
        }

        @Override
        public java.util.Optional<ReadOnlyAddressBook> readAddressBook(Path path) {
            return java.util.Optional.empty();
        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook ab) {}

        @Override
        public void saveAddressBook(ReadOnlyAddressBook ab, Path path) {}

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
    }

    /** Storage stub that always throws IOException from overwriteRawEntryAtIndex. */
    private static class StorageThrowingIo implements Storage {
        @Override
        public LoadReport overwriteRawEntryAtIndex(int index, Person corrected) throws java.io.IOException {
            throw new java.io.IOException("Disk full");
        }

        @Override
        public java.util.Optional<seedu.address.model.UserPrefs> readUserPrefs() {
            return java.util.Optional.empty();
        }

        @Override
        public void saveUserPrefs(ReadOnlyUserPrefs prefs) {}

        @Override
        public Path getUserPrefsFilePath() {
            return Path.of("dummy.json");
        }

        @Override
        public Path getAddressBookFilePath() {
            return Path.of("dummy.json");
        }

        @Override
        public java.util.Optional<ReadOnlyAddressBook> readAddressBook() {
            return java.util.Optional.empty();
        }

        @Override
        public java.util.Optional<ReadOnlyAddressBook> readAddressBook(Path path) {
            return java.util.Optional.empty();
        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook ab) {}

        @Override
        public void saveAddressBook(ReadOnlyAddressBook ab, Path path) {}

        @Override
        public LoadReport readAddressBookWithReport() {
            return null;
        }

        @Override
        public LoadReport readAddressBookWithReport(Path path) {
            return null;
        }
    }

    /** Storage stub that always throws DataLoadingException from overwriteRawEntryAtIndex. */
    private static class StorageThrowingDataLoading implements Storage {
        @Override
        public LoadReport overwriteRawEntryAtIndex(int index, Person corrected) throws DataLoadingException {
            // In this codebase DataLoadingException wraps a cause; pass an IOException as the cause.
            throw new DataLoadingException(new java.io.IOException("Corrupt file"));
        }

        @Override
        public java.util.Optional<seedu.address.model.UserPrefs> readUserPrefs() {
            return java.util.Optional.empty();
        }

        @Override
        public void saveUserPrefs(ReadOnlyUserPrefs prefs) {}

        @Override
        public Path getUserPrefsFilePath() {
            return Path.of("dummy.json");
        }

        @Override
        public Path getAddressBookFilePath() {
            return Path.of("dummy.json");
        }

        @Override
        public java.util.Optional<ReadOnlyAddressBook> readAddressBook() {
            return java.util.Optional.empty();
        }

        @Override
        public java.util.Optional<ReadOnlyAddressBook> readAddressBook(Path path) {
            return java.util.Optional.empty();
        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook ab) {}

        @Override
        public void saveAddressBook(ReadOnlyAddressBook ab, Path path) {}

        @Override
        public LoadReport readAddressBookWithReport() {
            return null;
        }

        @Override
        public LoadReport readAddressBookWithReport(Path path) {
            return null;
        }
    }

    /* ===========================
     * Helpers
     * =========================== */

    private static Person alice() {
        return new Person(
                new Name("Alice"),
                new Phone("91234567"),
                new Email("a@b.com"),
                new Address("Blk 1"),
                Set.of()
        );
    }

    /* ===========================
     * Tests
     * =========================== */

    /**
     * Valid overwrite: command executes and returns the expected success message.
     */
    @Test
    public void execute_success_returnsMessage() throws Exception {
        ModelStub model = new ModelStub();
        Storage storage = new StorageSuccess();

        FixInvalidCommand cmd = new FixInvalidCommand(0, alice(), storage);
        CommandResult result = cmd.execute(model);

        assertEquals("Fixed invalid entry at index 0.", result.getFeedbackToUser());
    }

    /**
     * Storage throws IOException; Logic should wrap it into CommandException.
     */
    @Test
    public void execute_storageIoWrappedAsCommandException() {
        ModelStub model = new ModelStub();
        Storage storage = new StorageThrowingIo();

        FixInvalidCommand cmd = new FixInvalidCommand(3, alice(), storage);
        assertThrows(CommandException.class, () -> cmd.execute(model));
    }

    /**
     * Storage throws DataLoadingException; Logic should wrap it into CommandException.
     */
    @Test
    public void execute_storageDataLoadingWrappedAsCommandException() {
        ModelStub model = new ModelStub();
        Storage storage = new StorageThrowingDataLoading();

        FixInvalidCommand cmd = new FixInvalidCommand(2, alice(), storage);
        assertThrows(CommandException.class, () -> cmd.execute(model));
    }

    /**
     * Covers equals(): reflexive, null, type, different index, different person, same fields.
     */
    @Test
    public void equals_variousCases_covered() {
        Storage s = noopStorage();

        Person alice = new Person(
                new Name("Alice"),
                new Phone("91234567"),
                new Email("a@b.com"),
                new Address("Blk 1"),
                Set.of());

        Person bob = new Person(
                new Name("Bob"),
                new Phone("98765432"),
                new Email("b@c.com"),
                new Address("Blk 2"),
                Set.of());

        FixInvalidCommand c1 = new FixInvalidCommand(0, alice, s);
        FixInvalidCommand c2 = new FixInvalidCommand(0, alice, s);
        FixInvalidCommand diffIndex = new FixInvalidCommand(1, alice, s);
        FixInvalidCommand diffPerson = new FixInvalidCommand(0, bob, s);

        // same object
        assertTrue(c1.equals(c1));

        // same values
        assertTrue(c1.equals(c2));

        // null
        assertFalse(c1.equals(null));

        // different type
        assertFalse(c1.equals("not-a-command"));

        // different index
        assertFalse(c1.equals(diffIndex));

        // different person
        assertFalse(c1.equals(diffPerson));
    }

    /**
     * Minimal Storage stub for equality tests; never hits disk.
     */
    private static Storage noopStorage() {
        return new Storage() {
            @Override
            public LoadReport overwriteRawEntryAtIndex(int index, Person corrected) {
                return new LoadReport(
                        new LoadReport.ModelData(new seedu.address.model.AddressBook()),
                        java.util.Collections.emptyList());
            }
            @Override
            public java.util.Optional<seedu.address.model.UserPrefs> readUserPrefs() {
                return java.util.Optional.empty();
            }
            @Override
            public void saveUserPrefs(seedu.address.model.ReadOnlyUserPrefs prefs) { }
            @Override
            public java.nio.file.Path getUserPrefsFilePath() {
                return java.nio.file.Path.of("dummy.json");
            }
            @Override
            public java.nio.file.Path getAddressBookFilePath() {
                return java.nio.file.Path.of("dummy.json");
            }
            @Override
            public java.util.Optional<seedu.address.model.ReadOnlyAddressBook> readAddressBook() {
                return java.util.Optional.empty();
            }
            @Override
            public java.util.Optional<seedu.address.model.ReadOnlyAddressBook> readAddressBook(
                    java.nio.file.Path path) {
                return java.util.Optional.empty();
            }
            @Override
            public void saveAddressBook(seedu.address.model.ReadOnlyAddressBook ab) { }
            @Override
            public void saveAddressBook(seedu.address.model.ReadOnlyAddressBook ab,
                                        java.nio.file.Path path) { }
            @Override
            public LoadReport readAddressBookWithReport() {
                return new LoadReport(
                        new LoadReport.ModelData(new seedu.address.model.AddressBook()),
                        java.util.Collections.emptyList());
            }
            @Override
            public LoadReport readAddressBookWithReport(java.nio.file.Path path) {
                return new LoadReport(
                        new LoadReport.ModelData(new seedu.address.model.AddressBook()),
                        java.util.Collections.emptyList());
            }
        };
    }
}
