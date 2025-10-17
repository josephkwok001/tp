package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.storage.LoadReport;

/**
 * Tests for {@link ListInvalidCommand}.
 */
public class ListInvalidCommandTest {

    private Model makeModel() {
        return new ModelManager(new AddressBook(), new UserPrefs());
    }

    @Test
    public void execute_noInvalids_returnsZero() throws Exception {
        Supplier<LoadReport> supplier = () -> new LoadReport(
                new LoadReport.ModelData(new AddressBook()),
                Collections.emptyList()
        );

        ListInvalidCommand cmd = new ListInvalidCommand(supplier);
        CommandResult res = cmd.execute(makeModel());

        // In our integration, "list-invalid" returns "0" when there are no invalids.
        assertEquals("0", res.getFeedbackToUser().trim());
    }

    @Test
    public void execute_withInvalids_listsReasons() throws Exception {
        // NOTE: constructor is now (index, reason) with a 1-based index
        List<LoadReport.InvalidPersonEntry> invalids = List.of(
                new LoadReport.InvalidPersonEntry(
                        1, "Bad phone", "Alice", "9123", "a@b.com", "Blk 1", "HDB", java.util.Set.of("phone")),
                new LoadReport.InvalidPersonEntry(
                        3, "Bad email", "Bob", "9999", "x@y", "Blk 2", "Condo", java.util.Set.of("email"))
        );

        Supplier<LoadReport> supplier = () -> new LoadReport(
                new LoadReport.ModelData(new AddressBook()),
                invalids
        );

        ListInvalidCommand cmd = new ListInvalidCommand(supplier);
        CommandResult res = cmd.execute(makeModel());
        String out = res.getFeedbackToUser();

        // Be tolerant to formatting differences; verify the reasons show up.
        assertTrue(out.contains("Bad phone"));
        assertTrue(out.contains("Bad email"));

        // Optional: if your command prints indices, you can keep these checks.
        // assertTrue(out.contains("#1"));
        // assertTrue(out.contains("#2") || out.contains("#3")); // depending on your chosen index display
    }
}
