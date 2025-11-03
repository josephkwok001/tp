package seedu.address.logic.commands.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyName;

/**
 * Links a property to a Person as an interested property.
 * Usage: {@code setip i/INDEX n/NAME_OF_PROPERTY}
 */
public class InterestedPropertyCommand extends Command {

    public static final String COMMAND_WORD = "setip";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sets an interested property for the specified person.\n"
            + "Parameters: "
            + "PERSON INDEX "
            + PREFIX_NAME + "NAME OF PROPERTY\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_INDEX + "1 "
            + PREFIX_NAME + "Hillside Villa";

    public static final String MESSAGE_SUCCESS = "Set interested property for %s: %s";
    public static final String MESSAGE_DUPLICATE_LINK =
            "This person is already marked as interested in this property: %s";
    public static final String MESSAGE_PROPERTY_NOT_FOUND =
            "Property not found: %s";
    public static final String MESSAGE_PERSON_NOT_FOUND =
            "Person not found in the person list";
    public static final String MESSAGE_OWNERSHIP_CONFLICT =
            "This person already owns the property: %s";

    private final PropertyName targetPropertyName;
    private final Index targetPersonIndex;

    private Property toAdd;
    private Person targetPerson;

    /**
     * Constructs a {@code InterestedPropertyCommand}.
     *
     * @param propertyName name of the property to link as interested; must be non-null
     * @param personIndex index of the person in the last shown list; must be non-null
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
                .filter(property -> property.getPropertyName().equals(targetPropertyName))
                .findFirst()
                .orElseThrow(() -> new CommandException(String.format(MESSAGE_PROPERTY_NOT_FOUND,
                        targetPropertyName.fullName)));
        try {
            targetPerson = model.getFilteredPersonList().get(targetPersonIndex.getZeroBased());
        } catch (IndexOutOfBoundsException e) {
            throw new CommandException(MESSAGE_PERSON_NOT_FOUND);
        }

        if (targetPerson.getOwnedProperties().stream().anyMatch(toAdd::isSameProperty)) {
            throw new CommandException(String.format(MESSAGE_OWNERSHIP_CONFLICT, targetPropertyName.fullName));
        }

        if (targetPerson.getInterestedProperties().stream().anyMatch(toAdd::isSameProperty)) {
            throw new CommandException(String.format(MESSAGE_DUPLICATE_LINK, targetPropertyName.fullName));
        }

        targetPerson.setInterestedProperty(toAdd);
        model.setPerson(targetPerson, targetPerson);
        return new CommandResult(String.format(MESSAGE_SUCCESS,
                targetPerson.getName().fullName, toAdd.getPropertyName().toString()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof InterestedPropertyCommand)) {
            return false;
        }
        InterestedPropertyCommand o = (InterestedPropertyCommand) other;
        return targetPropertyName.equals(o.targetPropertyName)
                && targetPersonIndex.equals(o.targetPersonIndex);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(targetPropertyName, targetPersonIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("propertyName", targetPropertyName)
                .add("personIndex", targetPersonIndex)
                .toString();
    }
}
