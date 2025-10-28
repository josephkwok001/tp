package seedu.address.logic.commands.person;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyName;

import java.util.List;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.Messages.MESSAGE_PROPERTY_NOT_FOUND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

/**
 * Deletes a property from the specificed person's list of interested properties.
 */
public class DeleteInterestedPropertyCommand extends Command {

    public static final String COMMAND_WORD = "deleteip";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes an interested property from the specified person.\n"
            + "Parameters: "
            + PREFIX_INDEX + "INDEX "
            + PREFIX_NAME + "PROPERTY NAME \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_INDEX + "1 "
            + PREFIX_NAME + "Sunshine Villa";

    public static String MESSAGE_SUCCESS = "Deleted interested property: %1$s from person: %2$s";

    private final PropertyName targetPropertyName;
    private final Index targetPersonIndex;

    private Property toDelete;

    public DeleteInterestedPropertyCommand(PropertyName propertyName, Index personIndex) {
        requireNonNull(propertyName);
        requireNonNull(personIndex);
        this.targetPropertyName = propertyName;
        this.targetPersonIndex = personIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetPersonIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person targetPerson = model.getFilteredPersonList().get(targetPersonIndex.getZeroBased());

        toDelete = model.getAddressBook().getPropertyList().stream()
                .filter(property -> property.getPropertyName().fullName.equals(targetPropertyName.getFullName()))
                .findFirst()
                .orElseThrow(() -> new CommandException(String.format(MESSAGE_PROPERTY_NOT_FOUND, targetPropertyName.getFullName())));

        if (!targetPerson.getInterestedProperties().contains(toDelete)) {
            throw new CommandException("This person is not interested in this property");
        }

        Person updatedPerson = targetPerson.removeInterestedProperty(toDelete);
        model.setPerson(targetPerson, updatedPerson);

        return new CommandResult(String.format(
                MESSAGE_SUCCESS, toDelete.getPropertyName().toString(), targetPerson.getName()));
    }
}
