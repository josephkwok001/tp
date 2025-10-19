package seedu.address;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.storage.LoadReport;

/**
 * Tests MainApp.buildInvalidSummary via reflection with a synthetic LoadReport.
 */
public class MainAppSummaryTest {

    private static LoadReport emptyReport() {
        return new LoadReport(new LoadReport.ModelData(new seedu.address.model.AddressBook()), List.of());
    }

    private static LoadReport oneInvalid() {
        LoadReport.ModelData modelData = new LoadReport.ModelData(new seedu.address.model.AddressBook());
        LoadReport.InvalidPersonEntry inv = new LoadReport.InvalidPersonEntry(
                1,
                "Invalid field(s): name, phone",
                "123",
                "xx",
                "bad",
                " ",
                "",
                Set.of("name", "phone")
        );
        return new LoadReport(modelData, List.of(inv));
    }

    private static LoadReport syntheticReport() {
        LoadReport.ModelData modelData = new LoadReport.ModelData(new seedu.address.model.AddressBook());
        LoadReport.InvalidPersonEntry inv = new LoadReport.InvalidPersonEntry(
                0,
                "Invalid field(s): name, phone, email, address, listing",
                "12345",
                "abc",
                "bad",
                " ",
                "",
                Set.of("name", "phone", "email", "address", "listing")
        );
        return new LoadReport(modelData, List.of(inv));
    }

    @Test
    void buildInvalidSummary_formatsByIndexAndListsReasons() throws Exception {
        MainApp app = new MainApp();
        Method m = MainApp.class.getDeclaredMethod("buildInvalidSummary", LoadReport.class);
        m.setAccessible(true);
        String summary = (String) m.invoke(app, syntheticReport());

        assertTrue(summary.contains("Invalid persons (ignored): 1"));
        assertTrue(summary.contains("Person #1"));
        assertTrue(summary.contains("name:"));
        assertTrue(summary.contains("phone:"));
        assertTrue(summary.contains("email:"));
        assertTrue(summary.contains("address:"));
        assertTrue(summary.contains("listing:"));
    }

    @Test
    void buildInvalidSummary_none_showsZero() throws Exception {
        MainApp app = new MainApp();
        Method m = MainApp.class.getDeclaredMethod("buildInvalidSummary", LoadReport.class);
        m.setAccessible(true);
        String s = (String) m.invoke(app, emptyReport());
        assertTrue(s.contains("Invalid persons (ignored): 0"));
        assertTrue(s.contains("Valid persons (loaded): 0"));
        assertTrue(s.contains("Invalid records: 0"));
    }

    @Test
    void buildInvalidSummary_one_showsIndexAndFields() throws Exception {
        MainApp app = new MainApp();
        Method m = MainApp.class.getDeclaredMethod("buildInvalidSummary", LoadReport.class);
        m.setAccessible(true);
        String s = (String) m.invoke(app, oneInvalid());
        assertTrue(s.contains("Invalid persons (ignored): 1"));
        assertTrue(s.contains("Person #2"));
        assertTrue(s.contains("name:"));
        assertTrue(s.contains("phone:"));
    }
}
