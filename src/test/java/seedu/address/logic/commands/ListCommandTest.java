package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ListCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        String expectedMessage = withCountSuffix(expectedModel);
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, CommandResult.ViewType.PERSONS);
        assertCommandSuccess(new ListCommand(), model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        String expectedMessage = withCountSuffix(expectedModel);
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, CommandResult.ViewType.PERSONS);
        assertCommandSuccess(new ListCommand(), model, expectedCommandResult, expectedModel);
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        ListCommand cmd = new ListCommand();
        assertTrue(cmd.equals(cmd));
    }

    @Test
    public void equals_differentInstanceSameType_returnsTrue() {
        assertTrue(new ListCommand().equals(new ListCommand()));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        assertFalse(new ListCommand().equals(new Object()));
    }

    @Test
    public void hashCode_sameType_returnsSameValue() {
        assertEquals(new ListCommand().hashCode(), new ListCommand().hashCode());
    }

    /** get “Listed all persons (N person[s])” */
    private static String withCountSuffix(Model m) {
        int count = m.getFilteredPersonList().size();
        String people = (count == 1) ? "person" : "persons";
        return String.format("%s (%d %s)", ListCommand.MESSAGE_SUCCESS, count, people);
    }
}
