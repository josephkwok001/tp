package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;

/**
 * Unit tests for {@link LoadReport}, {@link LoadReport.ModelData},
 * and {@link LoadReport.InvalidPersonEntry}.
 */
public class LoadReportTest {

    @Test
    public void constructor_nullModelData_throwsNullPointerException() {
        List<LoadReport.InvalidPersonEntry> empty = Collections.emptyList();
        assertThrows(NullPointerException.class, () -> new LoadReport(null, empty));
    }

    @Test
    public void modelData_nullAddressBook_throwsNpe() {
        LoadReport.ModelData data = new LoadReport.ModelData(new AddressBook());
        assertThrows(NullPointerException.class, () -> new LoadReport(data, null));
    }

    @Test
    public void modelData_nullAb_throwsNpe() {
        assertThrows(NullPointerException.class, () -> new LoadReport.ModelData(null));
    }

    @Test
    public void getModelData_returnsSameInstance() {
        AddressBook ab = new AddressBook();
        LoadReport.ModelData data = new LoadReport.ModelData(ab);
        LoadReport report = new LoadReport(data, Collections.emptyList());

        assertNotNull(report.getModelData());
        assertEquals(ab, report.getModelData().getAddressBook());
    }

    @Test
    public void getInvalids_isUnmodifiable() {
        LoadReport.ModelData data = new LoadReport.ModelData(new AddressBook());

        List<LoadReport.InvalidPersonEntry> backing = new ArrayList<>();
        LoadReport report = new LoadReport(data, backing);

        List<LoadReport.InvalidPersonEntry> exposed = report.getInvalids();
        assertNotNull(exposed);

        assertThrows(UnsupportedOperationException.class, () ->
                exposed.add(dummyInvalid(0, "x", Collections.emptyList())));
    }

    @Test
    public void invalidPersonEntry_accessorsAndFieldInvalid_work() {
        HashSet<String> invalid = new HashSet<>(Arrays.asList("name", "email"));
        LoadReport.InvalidPersonEntry entry = new LoadReport.InvalidPersonEntry(
                3,
                "Bad name and email",
                "A!ice", // original invalid values from JSON
                "91234567",
                "not-an-email",
                "Blk 1",
                invalid
        );

        assertEquals(3, entry.index());
        assertEquals("Bad name and email", entry.reason());
        assertEquals("A!ice", entry.name());
        assertEquals("91234567", entry.phone());
        assertEquals("not-an-email", entry.email());
        assertEquals("Blk 1", entry.address());

        // fieldInvalid should reflect the provided set
        assertTrue(entry.fieldInvalid("name"));
        assertTrue(entry.fieldInvalid("email"));
        assertFalse(entry.fieldInvalid("phone"));
        assertFalse(entry.fieldInvalid("address"));
        assertFalse(entry.fieldInvalid("nonexistent"));
    }

    @Test
    public void invalidPersonEntry_noInvalidFields_allFalse() {
        LoadReport.InvalidPersonEntry entry = new LoadReport.InvalidPersonEntry(
                0,
                "Some reason",
                "Bob",
                "91234567",
                "b@c.com",
                "Blk 2",
                new HashSet<>()
        );

        assertFalse(entry.fieldInvalid("name"));
        assertFalse(entry.fieldInvalid("phone"));
        assertFalse(entry.fieldInvalid("email"));
        assertFalse(entry.fieldInvalid("address"));
    }

    private static LoadReport.InvalidPersonEntry dummyInvalid(int idx,
                                                              String reason,
                                                              List<String> invalidKeys) {
        return new LoadReport.InvalidPersonEntry(
                idx,
                reason,
                "N",
                "P",
                "E",
                "A",
                new HashSet<>(invalidKeys)
        );
    }
}
