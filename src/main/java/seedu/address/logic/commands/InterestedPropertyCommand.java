package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyName;


/**
 * Links a property to a Person that is interested
 */
public class InterestedPropertyCommand extends Command {

    public static final String COMMAND_WORD = "setip";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Links a Property as an interested property to a Person."
            + "Parameters: "
            + PREFIX_INDEX + "PERSON INDEX "
            + PREFIX_NAME + "NAME OF PROPERTY \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_INDEX + "1 "
            + PREFIX_NAME + "Hillside Villa ";

    public static final String MESSAGE_SUCCESS = "Property %1$s linked to %2$s as an interested property";
    public static final String MESSAGE_DUPLICATE_LINK =
            "This person is already marked as interested in this property";
    public static final String MESSAGE_PROPERTY_NOT_FOUND =
            "Property not found in the property list";
    public static final String MESSAGE_PERSON_NOT_FOUND =
            "Person not found in the person list";

    private final PropertyName targetPropertyName;
    private final Index targetPersonIndex;

    private Property toAdd;
    private Person targetPerson;

    /**
     * Creates an InterestedPropertyCommand to add {@code Property} to the specified {@code Person}
     */
    public InterestedPropertyCommand(PropertyName propertyName, Index personIndex) {
        requireNonNull(propertyName);
        requireNonNull(personIndex);
        targetPropertyName = propertyName;
        targetPersonIndex = personIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        toAdd = model.getAddressBook().getPropertyList().stream()
                .filter(property -> property.getPropertyName().fullName.equals(targetPropertyName.fullName))
                .findFirst()
                .orElseThrow(() -> new CommandException(MESSAGE_PROPERTY_NOT_FOUND));
        try {
            targetPerson = model.getFilteredPersonList().get(targetPersonIndex.getZeroBased());
        } catch (IndexOutOfBoundsException e) {
            throw new CommandException(MESSAGE_PERSON_NOT_FOUND);
        }

        if (targetPerson.getInterestedProperties().contains(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_LINK);
        }

        targetPerson.setInterestedProperties(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS,
                Messages.formatProperty(toAdd), Messages.formatPerson(targetPerson)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof InterestedPropertyCommand otherAddCommand)) {
            return false;
        }

        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .add("targetPerson", targetPerson)
                .toString();
    }
}
