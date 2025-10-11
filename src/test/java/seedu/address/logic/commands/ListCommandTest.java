package seedu.address.logic.commands;

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
        assertCommandSuccess(new ListCommand(), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        String expectedMessage = withCountSuffix(expectedModel);
        assertCommandSuccess(new ListCommand(), model, expectedMessage, expectedModel);
    }

    /** get “Listed all persons (N person[s])” */
    private static String withCountSuffix(Model m) {
        int count = m.getFilteredPersonList().size();
        String people = (count == 1) ? "person" : "persons";
        return String.format("%s (%d %s)", ListCommand.MESSAGE_SUCCESS, count, people);
    }
}
