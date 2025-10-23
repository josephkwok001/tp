package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PROPERTY;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PROPERTY;
import static seedu.address.testutil.TypicalProperties.getTypicalPropertiesAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.property.Property;

public class DeletePropertyCommandTest {
    private Model model = new ModelManager(getTypicalPropertiesAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        System.out.println("DEBUG: Properties in model: " + model.getAddressBook().getPropertyList().size());
        System.out.println("DEBUG: Properties in model: " + model.getAddressBook().getPropertyList());

        Property propertyToDelete = model.getAddressBook().getPropertyList().get(INDEX_FIRST_PROPERTY.getZeroBased());
        DeletePropertyCommand deletePropertyCommand = new DeletePropertyCommand(INDEX_FIRST_PROPERTY);

        String expectedMessage = String.format(DeletePropertyCommand.MESSAGE_DELETE_PROPERTY_SUCCESS,
                Messages.formatProperty(propertyToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteProperty(propertyToDelete);

        assertCommandSuccess(deletePropertyCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getAddressBook().getPropertyList().size() + 1);
        DeletePropertyCommand deletePropertyCommand = new DeletePropertyCommand(outOfBoundIndex);

        assertCommandFailure(deletePropertyCommand, model, Messages.MESSAGE_INVALID_PROPERTY_DISPLAYED_INDEX);
    }

    /*@Test
    public void execute_validIndexFilteredList_success() {
        Property propertyToDelete = model.getAddressBook().getPropertyList().get(INDEX_FIRST_PROPERTY.getZeroBased());
        DeletePropertyCommand deletePropertyCommand = new DeletePropertyCommand(INDEX_FIRST_PROPERTY);

        String expectedMessage = String.format(DeletePropertyCommand.MESSAGE_DELETE_PROPERTY_SUCCESS,
                Messages.formatProperty(propertyToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteProperty(propertyToDelete);

        assertCommandSuccess(deletePropertyCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getAddressBook().getPropertyList().size() + 1);
        DeletePropertyCommand deletePropertyCommand = new DeletePropertyCommand(outOfBoundIndex);

        assertCommandFailure(deletePropertyCommand, model, Messages.MESSAGE_INVALID_PROPERTY_DISPLAYED_INDEX);
      }
    */

    @Test
    public void equals() {
        DeletePropertyCommand deleteFirstCommand = new DeletePropertyCommand(INDEX_FIRST_PROPERTY);
        DeletePropertyCommand deleteSecondCommand = new DeletePropertyCommand(INDEX_SECOND_PROPERTY);

        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        DeletePropertyCommand deleteFirstCommandCopy = new DeletePropertyCommand(INDEX_FIRST_PROPERTY);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        assertFalse(deleteFirstCommand.equals(1));

        assertFalse(deleteFirstCommand.equals(null));

        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeletePropertyCommand deletePropertyCommand = new DeletePropertyCommand(targetIndex);
        String expected = DeletePropertyCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, deletePropertyCommand.toString());
    }
}




