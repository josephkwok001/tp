package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;

/**
 * Covers LoadReport accessors and fieldInvalid.
 */
public class LoadReportAccessorsTest {

    @Test
    void accessors_and_fieldInvalid() {
        LoadReport.ModelData md = new LoadReport.ModelData(new AddressBook());
        LoadReport.InvalidPersonEntry inv = new LoadReport.InvalidPersonEntry(
                3, "r", "n", "p", "e", "a", "l", Set.of("name", "email"));
        LoadReport lr = new LoadReport(md, List.of(inv));
        assertEquals(md, lr.getModelData());
        assertEquals(1, lr.getInvalids().size());
        LoadReport.InvalidPersonEntry got = lr.getInvalids().get(0);
        assertEquals(3, got.index());
        assertEquals("n", got.name());
        assertTrue(got.fieldInvalid("name"));
        assertTrue(got.fieldInvalid("email"));
    }
}
