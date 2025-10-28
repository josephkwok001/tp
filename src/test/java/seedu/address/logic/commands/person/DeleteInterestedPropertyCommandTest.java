package seedu.address.logic.commands.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.property.Address;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyName;

public class DeleteInterestedPropertyCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validInputs_success() throws Exception {
        Person person = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Property property = person.getInterestedProperties().get(0); // Assume the person has at least one property

        DeleteInterestedPropertyCommand command = new DeleteInterestedPropertyCommand(
                new PropertyName(property.getPropertyName().fullName), INDEX_FIRST_PERSON);

        CommandResult result = command.execute(model);

        String expectedMessage = String.format(DeleteInterestedPropertyCommand.MESSAGE_SUCCESS,
                property.getPropertyName(), person.getName().fullName);

        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void execute_invalidPersonIndex_throwsCommandException() {
        DeleteInterestedPropertyCommand command = new DeleteInterestedPropertyCommand(
                new PropertyName("Nonexistent Property"), INDEX_FIRST_PERSON);

        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_propertyNotFound_throwsCommandException() {
        DeleteInterestedPropertyCommand command = new DeleteInterestedPropertyCommand(
                new PropertyName("Nonexistent Property"), INDEX_FIRST_PERSON);

        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_propertyNotInInterestedList_throwsCommandException() {
        Person person = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Property property = new Property(new Address("random address")
                , new Price(1000000), new PropertyName("Nonexistent Property"));

        DeleteInterestedPropertyCommand command = new DeleteInterestedPropertyCommand(
                property.getPropertyName(), INDEX_FIRST_PERSON);

        assertThrows(CommandException.class, () -> command.execute(model));
    }
}
