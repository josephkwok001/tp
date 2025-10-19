package seedu.address;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Method;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Listing;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.storage.LoadReport;

/**
 * Tests MainApp.toValidOnly via reflection.
 */
public class MainAppToValidOnlyTest {

    private static Person p(String n, String ph, String em, String ad, String li) {
        return new Person(new Name(n), new Phone(ph), new Email(em), new Address(ad), new Listing(li), Set.of());
    }

    private static LoadReport reportWithPersonsAndInvalidIdx(int... invalidIdx) {
        AddressBook ab = new AddressBook();
        ab.addPerson(p("A", "91234567", "a@a.com", "a", "HDB"));
        ab.addPerson(p("B", "92345678", "b@b.com", "b", "Condo"));
        ab.addPerson(p("C", "93456789", "c@c.com", "c", "Landed Property"));
        LoadReport.ModelData md = new LoadReport.ModelData(ab);
        java.util.List<LoadReport.InvalidPersonEntry> invs = new java.util.ArrayList<>();
        for (int idx : invalidIdx) {
            invs.add(new LoadReport.InvalidPersonEntry(
                    idx, "invalid", "", "", "", "", "", java.util.Set.of("name")
            ));
        }
        return new LoadReport(md, invs);
    }

    @Test
    void toValidOnly_filtersOutInvalidIndices() throws Exception {
        MainApp app = new MainApp();
        Method m = MainApp.class.getDeclaredMethod("toValidOnly", LoadReport.class);
        m.setAccessible(true);
        LoadReport rpt = reportWithPersonsAndInvalidIdx(1);
        AddressBook filtered = (AddressBook) m.invoke(app, rpt);
        assertEquals(2, filtered.getPersonList().size());
        assertEquals("A", filtered.getPersonList().get(0).getName().fullName);
        assertEquals("C", filtered.getPersonList().get(1).getName().fullName);
    }

    @Test
    void toValidOnly_noInvalid_keepsAll() throws Exception {
        MainApp app = new MainApp();
        Method m = MainApp.class.getDeclaredMethod("toValidOnly", LoadReport.class);
        m.setAccessible(true);
        LoadReport rpt = reportWithPersonsAndInvalidIdx();
        AddressBook filtered = (AddressBook) m.invoke(app, rpt);
        assertEquals(3, filtered.getPersonList().size());
    }
}
