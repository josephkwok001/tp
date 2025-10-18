package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
 * Tests LoadReport.fromAddressBook with all-valid persons.
 */
public class LoadReportFromAddressBookTest {

    private static Person validPerson(String n, String p, String e, String a, String l) {
        return new Person(
                new Name(n),
                new Phone(p),
                new Email(e),
                new Address(a),
                new Listing(l),
                java.util.Set.of(new Tag("friends")));
    }

    @Test
    void fromAddressBook_allValid_noInvalidsReported() {
        AddressBook src = new AddressBook();
        src.addPerson(validPerson("Alice Bob", "91234567", "alice@example.com", "1 Main St", "HDB"));
        src.addPerson(validPerson("Roy Balakrishnan", "92624417", "royb@example.com", "Blk 45", "Landed Property"));

        LoadReport rep = LoadReport.fromAddressBook(src);

        assertEquals(2, rep.getModelData().getAddressBook().getPersonList().size());
        assertEquals(0, rep.getInvalids().size());
    }
}
