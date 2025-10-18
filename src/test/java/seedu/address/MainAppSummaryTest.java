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
}
