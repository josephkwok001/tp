package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Listing;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Tests LoadReport.fromAddressBook for both all-valid and invalid branches.
 */
public class LoadReportFromAddressBookTest {

    private static Person valid(String n, String p, String e, String a, String l) {
        return new Person(new Name(n), new Phone(p),
                new Email(e), new Address(a), new Listing(l), Set.of(new Tag("x")));
    }

    private static class FakeInvalidPerson extends Person {
        FakeInvalidPerson(String n, String p, String e, String a, String l) {
            super(new Name(n), new Phone(p), new Email(e), new Address(a), new Listing(l), Set.of());
        }
        @Override
        public boolean isFullyValid() {
            return false;
        }
    }

    @Test
    void fromAddressBook_allInvalid_indexesReported() {
        AddressBook src = new AddressBook();
        src.addPerson(new FakeInvalidPerson("A A", "91234567", "a@a.com", "x", "HDB"));
        src.addPerson(new FakeInvalidPerson("B B", "92345678", "b@b.com", "y", "Condo"));

        LoadReport rep = LoadReport.fromAddressBook(src);

        assertEquals(0, rep.getModelData().getAddressBook().getPersonList().size());
        assertEquals(2, rep.getInvalids().size());
        assertEquals(0, rep.getInvalids().get(0).index());
        assertEquals(1, rep.getInvalids().get(1).index());
    }

    @Test
    void fromAddressBook_mixed_reportsInvalidsAndKeepsValids() {
        AddressBook src = new AddressBook();
        src.addPerson(valid("Good One", "91234567", "g1@a.com", "a", "HDB"));
        src.addPerson(new FakeInvalidPerson("Bad One", "92345678", "b@b.com", "b", "Condo"));
        src.addPerson(valid("Good Two", "93456789", "g2@a.com", "c", "Landed Property"));

        LoadReport rep = LoadReport.fromAddressBook(src);

        assertEquals(2, rep.getModelData().getAddressBook().getPersonList().size());
        assertEquals(1, rep.getInvalids().size());
        assertEquals(1, rep.getInvalids().get(0).index());
    }
}
