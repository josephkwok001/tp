package seedu.address.logic.commands.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
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
        Property testProperty = new Property(new Address("123 Main St"), new Price(500000),
                new PropertyName("Dream Home"));

        person.setInterestedProperty(testProperty);
        model.addProperty(testProperty);
        System.out.println(person.getInterestedProperties());

        DeleteInterestedPropertyCommand command = new DeleteInterestedPropertyCommand(
                new PropertyName(testProperty.getPropertyName().fullName), INDEX_FIRST_PERSON);

        CommandResult result = command.execute(model);

        System.out.println(result.getFeedbackToUser());

        String expectedMessage = String.format(DeleteInterestedPropertyCommand.getSuccessMessage(),
                testProperty.getPropertyName(), person.getName().fullName);

        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void execute_propertyNotFound_throwsCommandException() {
        DeleteInterestedPropertyCommand command = new DeleteInterestedPropertyCommand(
                new PropertyName("Nonexistent Property"), INDEX_FIRST_PERSON);

        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_invalidPersonIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteInterestedPropertyCommand command =
                new DeleteInterestedPropertyCommand(new PropertyName("Sunshine Villa"), outOfBoundIndex);

        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_propertyNotInInterestedList_throwsCommandException() {
        Property propertyNotInList = new Property(new Address("1 Property Drive"),
                new Price(300000), new PropertyName("new property"));
        model.addProperty(propertyNotInList);
        Index validIndex = Index.fromOneBased(1);

        DeleteInterestedPropertyCommand command =
                new DeleteInterestedPropertyCommand(propertyNotInList.getPropertyName(), validIndex);

        assertThrows(CommandException.class, () -> command.execute(model));
    }
}
