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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Manual test class to systematically test each command based on the User Guide.
 * This test documents any bugs or unexpected behavior.
 */
public class ManualCommandTest {
    private Logic logic;
    private Model model;
    private List<String> bugReport;
    private PrintWriter writer;

    @BeforeEach
    public void setUp() {
        try {
            writer = new PrintWriter(new FileWriter("bug_report.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        model = new ModelManager(new AddressBook(), new UserPrefs());
        Path tempPath = Paths.get("data", "temp_test.json");
        Storage storage = new StorageManager(
                new JsonAddressBookStorage(tempPath),
                new JsonUserPrefsStorage(Paths.get("preferences_test.json"))
        );
        logic = new LogicManager(model, storage);
        bugReport = new ArrayList<>();
    }

    private void testCommand(String commandName, String command, String expectedSuccess) {
        try {
            String msg = "\n=== Testing: " + commandName + " ===";
            System.out.println(msg);
            writer.println(msg);
            msg = "Command: " + command;
            System.out.println(msg);
            writer.println(msg);
            CommandResult result = logic.execute(command);
            msg = "Result: " + result.getFeedbackToUser();
            System.out.println(msg);
            writer.println(msg);

            if (!result.getFeedbackToUser().contains(expectedSuccess)) {
                String bug = String.format("[%s] Expected message containing '%s', but got: %s",
                    commandName, expectedSuccess, result.getFeedbackToUser());
                msg = "BUG FOUND: " + bug;
                System.out.println(msg);
                writer.println(msg);
                bugReport.add(bug);
            }
        } catch (CommandException | ParseException e) {
            String bug = String.format("[%s] Command failed with exception: %s", commandName, e.getMessage());
            String msg = "BUG FOUND: " + bug;
            System.out.println(msg);
            writer.println(msg);
            bugReport.add(bug);
        }
    }

    private void testCommandShouldFail(String commandName, String command, String expectedError) {
        try {
            String msg = "\n=== Testing (should fail): " + commandName + " ===";
            System.out.println(msg);
            writer.println(msg);
            msg = "Command: " + command;
            System.out.println(msg);
            writer.println(msg);
            CommandResult result = logic.execute(command);
            String bug = String.format("[%s] Expected command to fail, but succeeded with: %s",
                commandName, result.getFeedbackToUser());
            msg = "BUG FOUND: " + bug;
            System.out.println(msg);
            writer.println(msg);
            bugReport.add(bug);
        } catch (CommandException | ParseException e) {
            String msg = "Failed as expected: " + e.getMessage();
            System.out.println(msg);
            writer.println(msg);
            if (expectedError != null && !e.getMessage().contains(expectedError)) {
                String bug = String.format("[%s] Expected error message containing '%s', but got: %s",
                    commandName, expectedError, e.getMessage());
                msg = "BUG FOUND: " + bug;
                System.out.println(msg);
                writer.println(msg);
                bugReport.add(bug);
            }
        }
    }

    @Test
    public void testAllCommandsSystematically() {
        System.out.println("===== STARTING SYSTEMATIC COMMAND TESTING =====\n");

        // 1. TEST GENERAL UTILITIES
        System.out.println("\n===== TESTING GENERAL UTILITIES =====");

        // Test help command
        testCommand("help", "help", "Opened help window");

        // Test clear command - start fresh
        testCommand("clear", "clear", "Address book has been cleared");

        // 2. TEST MANAGING CLIENTS
        System.out.println("\n===== TESTING MANAGING CLIENTS =====");

        // Test add command - basic
        testCommand("add - basic",
            "add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01",
            "New person added");

        // Test add command - with tags
        testCommand("add - with tags",
            "add n/Betsy Crowe t/client e/betsycrowe@example.com a/Changi p/1234567 t/buyer",
            "New person added");

        // Test add command - duplicate detection
        testCommandShouldFail("add - duplicate",
            "add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01",
            "already exists");

        // Test add command - invalid phone (too short)
        testCommandShouldFail("add - invalid phone",
            "add n/Test p/12 e/test@example.com a/Test address",
            "Phone");

        // Test add command - invalid email
        testCommandShouldFail("add - invalid email",
            "add n/Test p/12345678 e/invalidemail a/Test address",
            "Email");

        // Test add command - missing required field
        testCommandShouldFail("add - missing phone",
            "add n/Test e/test@example.com a/Test address",
            null);

        // Test list command
        testCommand("list", "list", "Listed all persons");

        // Test find command - by name
        testCommand("find - by name", "find n/John", "person");

        // Test find command - by tag
        testCommand("find - by tag", "find t/buyer", "person");

        // Test edit command
        testCommand("edit", "edit 1 p/91234567 e/johndoe@example.com", "Edited Person");

        // Test edit command - invalid index
        testCommandShouldFail("edit - invalid index",
            "edit 999 p/91234567",
            "index");

        // Test delete command - by index
        testCommand("delete - by index", "delete 2", "Deleted Person");

        // Test delete command - by email
        testCommand("delete - by email", "delete e/johndoe@example.com", "Deleted Person");

        // 3. TEST MANAGING PROPERTIES
        System.out.println("\n===== TESTING MANAGING PROPERTIES =====");

        // Test addp command
        testCommand("addp - basic",
            "addp n/Sunshine Condo a/123, Sunshine Rd, 123456 pr/800000",
            "New property added");

        testCommand("addp - another property",
            "addp n/Ocean View HDB a/456, Ocean Ave, 654321 pr/1000000",
            "New property added");

        // Test addp - duplicate detection
        testCommandShouldFail("addp - duplicate",
            "addp n/Sunshine Condo a/123, Sunshine Rd, 123456 pr/800000",
            "already exists");

        // Test addp - invalid price (decimal)
        testCommandShouldFail("addp - decimal price",
            "addp n/Test Property a/Test Address pr/800000.50",
            "Price");

        // Test addp - negative price
        testCommandShouldFail("addp - negative price",
            "addp n/Test Property a/Test Address pr/-100000",
            "Price");

        // Test addp - price exceeding max (2147483647)
        testCommandShouldFail("addp - price exceeds max",
            "addp n/Test Property a/Test Address pr/9999999999",
            "Price");

        // Test listp command
        testCommand("listp", "listp", "Listed all properties");

        // Test findp command
        testCommand("findp", "findp n/Sunshine", "properties listed");

        // Test editp command
        testCommand("editp", "editp 1 n/Sunshine Condo pr/1200000 a/123 Testing Rd", "Edited Property");

        // Test editp - invalid index
        testCommandShouldFail("editp - invalid index",
            "editp 999 pr/100000",
            "index");

        // Test deletep command
        testCommand("deletep", "deletep 2", "Deleted Property");

        // 4. TEST CLIENT-PROPERTY RELATIONSHIPS
        System.out.println("\n===== TESTING CLIENT-PROPERTY RELATIONSHIPS =====");

        // First, ensure we have clients and properties
        testCommand("add - client for relationship",
            "add n/Alice Smith p/87654321 e/alice@example.com a/Alice street",
            "New person added");

        testCommand("addp - property for relationship",
            "addp n/Hillside Villa a/789, Hill Road pr/500000",
            "New property added");

        // Test setop command
        testCommand("setop", "setop 1 n/Hillside Villa", "Set owned property");

        // Test setop - property not found
        testCommandShouldFail("setop - property not found",
            "setop 1 n/Nonexistent Property",
            "Property not found");

        // Test setop - duplicate owned property
        testCommandShouldFail("setop - duplicate",
            "setop 1 n/Hillside Villa",
            "already owns");

        // Test setip command
        testCommand("setip", "setip 1 n/Sunshine Condo", "Set interested property");

        // Test setip - property not found
        testCommandShouldFail("setip - property not found",
            "setip 1 n/Nonexistent Property",
            "Property not found");

        // Test setip - duplicate interested property
        testCommandShouldFail("setip - duplicate",
            "setip 1 n/Sunshine Condo",
            "already marked as interested");

        // Test deleteop command
        testCommand("deleteop", "deleteop 1 n/Hillside Villa", "Deleted owned property");

        // Test deleteop - property not owned
        testCommandShouldFail("deleteop - not owned",
            "deleteop 1 n/Hillside Villa",
            null);

        // Test deleteip command
        testCommand("deleteip", "deleteip 1 n/Sunshine Condo", "Deleted interested property");

        // Test deleteip - property not interested
        testCommandShouldFail("deleteip - not interested",
            "deleteip 1 n/Sunshine Condo",
            null);

        // 5. TEST EXPORT COMMAND
        System.out.println("\n===== TESTING EXPORT COMMAND =====");

        testCommand("export - basic", "export test_export", "exported to CSV");

        // Test export - invalid filename (with spaces)
        testCommandShouldFail("export - filename with spaces",
            "export my file",
            null);

        // Test export - empty filename
        testCommandShouldFail("export - empty filename",
            "export   ",
            null);

        // 6. TEST EDGE CASES
        System.out.println("\n===== TESTING EDGE CASES =====");

        // Test command with extra spaces
        testCommand("command - extra spaces", "list   ", "Listed all persons");

        // Test command case sensitivity
        testCommand("command - case sensitivity", "LIST", "Unknown command");

        // Test parameter with leading/trailing spaces
        testCommand("parameter - spaces stripped",
            "add n/  Spaced Name  p/98765432 e/spaced@example.com a/Test",
            "New person added");

        // Test very long name (51 characters - should fail based on UserGuide)
        testCommandShouldFail("add - name too long",
            "add n/ThisIsAVeryLongNameThatExceedsFiftyCharactersInLength12 p/98765432 e/longname@example.com a/Test",
            "Name");

        // Test phone with exactly 3 digits (minimum)
        testCommand("add - phone minimum length",
            "add n/Min Phone p/123 e/minphone@example.com a/Test",
            "New person added");

        // Test phone with exactly 20 digits (maximum)
        testCommand("add - phone maximum length",
            "add n/Max Phone p/12345678901234567890 e/maxphone@example.com a/Test",
            "New person added");

        // Test phone with 21 digits (should fail)
        testCommandShouldFail("add - phone too long",
            "add n/Too Long Phone p/123456789012345678901 e/toolong@example.com a/Test",
            "Phone");

        // Test tag with 31 characters (should fail based on UserGuide)
        testCommandShouldFail("add - tag too long",
            "add n/Tag Test p/98765432 e/tag@example.com a/Test t/ThisIsAVeryLongTagThatExceeds30",
            "Tag");

        // PRINT SUMMARY
        String summary = "\n\n===== TEST SUMMARY =====";
        System.out.println(summary);
        writer.println(summary);
        if (bugReport.isEmpty()) {
            String msg = "✓ All tests passed! No bugs found.";
            System.out.println(msg);
            writer.println(msg);
        } else {
            String msg = "✗ Found " + bugReport.size() + " bug(s):";
            System.out.println(msg);
            writer.println(msg);
            for (int i = 0; i < bugReport.size(); i++) {
                msg = (i + 1) + ". " + bugReport.get(i);
                System.out.println(msg);
                writer.println(msg);
            }
        }
        writer.close();
    }
}
