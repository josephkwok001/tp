package seedu.address;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.AddressBook;
import seedu.address.storage.*;

import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Test to investigate the delete by email bug.
 */
public class SpecificBugTest {
    private Logic logic;
    private Model model;
    private PrintWriter writer;

    @BeforeEach
    public void setUp() {
        try {
            writer = new PrintWriter(new FileWriter("specific_bug_test.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        model = new ModelManager(new AddressBook(), new UserPrefs());
        Path tempPath = Paths.get("data", "specific_test.json");
        Storage storage = new StorageManager(
                new JsonAddressBookStorage(tempPath),
                new JsonUserPrefsStorage(Paths.get("preferences_specific_test.json"))
        );
        logic = new LogicManager(model, storage);
    }

    private void executeAndLog(String command) {
        try {
            String msg = "\n>>> " + command;
            System.out.println(msg);
            writer.println(msg);

            // Log list size before command
            msg = "List size before: " + model.getFilteredPersonList().size();
            System.out.println(msg);
            writer.println(msg);

            CommandResult result = logic.execute(command);
            msg = "Result: " + result.getFeedbackToUser();
            System.out.println(msg);
            writer.println(msg);

            // Log list size and contents after command
            msg = "List size after: " + model.getFilteredPersonList().size();
            System.out.println(msg);
            writer.println(msg);

            msg = "Current list contents:";
            System.out.println(msg);
            writer.println(msg);
            model.getFilteredPersonList().forEach(person -> {
                String info = "  - " + person.getName() + " (email: " + person.getEmail() + ")";
                System.out.println(info);
                writer.println(info);
            });

        } catch (CommandException | ParseException e) {
            String msg = "ERROR: " + e.getMessage();
            System.out.println(msg);
            writer.println(msg);
        }
    }

    @Test
    public void testDeleteByEmailBug() {
        System.out.println("===== INVESTIGATING DELETE BY EMAIL BUG =====\n");
        writer.println("===== INVESTIGATING DELETE BY EMAIL BUG =====\n");

        // Setup: Add two clients
        executeAndLog("add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01");
        executeAndLog("add n/Betsy Crowe t/client e/betsycrowe@example.com a/Changi p/1234567 t/buyer");

        // Show full list
        executeAndLog("list");

        // Filter by name
        executeAndLog("find n/John");

        // Filter by tag (should show only Betsy)
        executeAndLog("find t/buyer");

        // Edit person 1 in filtered list (should be Betsy)
        executeAndLog("edit 1 p/91234567 e/johndoe@example.com");

        // Check current state
        executeAndLog("list");

        // Delete person 2 by index
        executeAndLog("delete 2");

        // Check current state again
        executeAndLog("list");

        // Try to delete by email
        executeAndLog("delete e/johndoe@example.com");

        writer.close();
        System.out.println("\n===== TEST COMPLETE - Check specific_bug_test.txt for details =====");
    }
}
