package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.OwnedProperties;
import seedu.address.model.person.Person;
import seedu.address.model.property.Property;

/**
 * Adds a property from the address book to the specified person's owned properties.
 * <p>
 * Usage: {@code setop i/INDEX n/NAME_OF_PROPERTY}
 */
public class SetOwnedPropertyCommand extends Command {

    /** Command word to invoke this command. */
    public static final String COMMAND_WORD = "setop";

    /** Usage message shown on invalid input. */
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sets an owned property for the specified person.\n"
            + "Parameters: i/INDEX n/NAME_OF_PROPERTY\n"
            + "Example: " + COMMAND_WORD + " i/1 n/Marina Bay Apt 12F";

    /** Success message template. */
    public static final String MESSAGE_SUCCESS = "Set owned property for %s: %s";

    /** Error when the named property does not exist in the address book. */
    public static final String MESSAGE_PROP_NOT_FOUND = "Property not found: %s";

    private final Index index;
    private final String propertyName;

    /**
     * Constructs a {@code SetOwnedPropertyCommand}.
     *
     * @param index index of the person in the last shown list
     * @param propertyName exact name of the property to add
     */
    public SetOwnedPropertyCommand(Index index, String propertyName) {
        requireNonNull(index);
        requireNonNull(propertyName);
        this.index = index;
        this.propertyName = propertyName;
    }

    /**
     * Executes the command by adding the referenced property to the target person's owned list.
     *
     * @param model model in which the change is performed
     * @return result containing the feedback message
     * @throws CommandException if index is invalid or the property cannot be found
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownList = model.getFilteredPersonList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person target = lastShownList.get(index.getZeroBased());

        Property prop = model.getAddressBook().getPropertyList().stream()
                .filter(p -> p.getPropertyName().toString().equals(propertyName))
                .findFirst()
                .orElseThrow(() -> new CommandException(String.format(MESSAGE_PROP_NOT_FOUND, propertyName)));

        OwnedProperties updatedOwned = target.getOwnedProperties().withAdded(prop);

        Person edited = new Person(
                target.getName(),
                target.getPhone(),
                target.getEmail(),
                target.getAddress(),
                target.getTags(),
                updatedOwned
        );

        model.setPerson(target, edited);

        return new CommandResult(String.format(
                MESSAGE_SUCCESS, target.getName().fullName, prop.getPropertyName().toString()));
    }

    /**
     * Returns true if both commands target the same person index and property name.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof SetOwnedPropertyCommand)) {
            return false;
        }
        SetOwnedPropertyCommand o = (SetOwnedPropertyCommand) other;
        return index.equals(o.index) && propertyName.equals(o.propertyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, propertyName);
    }
}
