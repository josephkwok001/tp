package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import static seedu.address.logic.commands.CommandTestUtil.showPropertyAtIndex;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PROPERTIES;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PROPERTY;
import static seedu.address.testutil.TypicalProperties.getTypicalPropertiesAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ListPropertyCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalPropertiesAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        String expectedMessage = withCountSuffix(expectedModel);
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, CommandResult.ViewType.PROPERTIES);
        assertCommandSuccess(new ListPropertyCommand(), model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showPropertyAtIndex(model, INDEX_FIRST_PROPERTY);

        expectedModel.updateFilteredPropertyList(PREDICATE_SHOW_ALL_PROPERTIES);

        String expectedMessage = withCountSuffix(expectedModel);
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, CommandResult.ViewType.PROPERTIES);
        assertCommandSuccess(new ListPropertyCommand(), model, expectedCommandResult, expectedModel);
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        ListPropertyCommand cmd = new ListPropertyCommand();
        assertTrue(cmd.equals(cmd));
    }

    @Test
    public void equals_differentInstanceSameType_returnsTrue() {
        assertTrue(new ListPropertyCommand().equals(new ListPropertyCommand()));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        assertFalse(new ListPropertyCommand().equals(new Object()));
    }

    @Test
    public void hashCode_sameType_returnsSameValue() {
        assertEquals(new ListPropertyCommand().hashCode(), new ListPropertyCommand().hashCode());
    }

    /** get "Listed all properties (N property[ies])" */
    private static String withCountSuffix(Model m) {
        int count = m.getFilteredPropertyList().size();
        String properties = (count == 1) ? "property" : "properties";
        return String.format("%s (%d %s)", ListPropertyCommand.MESSAGE_SUCCESS, count, properties);
    }
}

