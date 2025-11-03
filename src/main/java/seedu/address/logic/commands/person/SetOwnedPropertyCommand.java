package seedu.address.logic.commands.person;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.property.Property;

/**
 * Adds an existing {@link Property} in the address book to a target {@link Person}'s owned properties.
 * Usage: {@code setop INDEX n/NAME_OF_PROPERTY}
 */
public class SetOwnedPropertyCommand extends Command {

    public static final String COMMAND_WORD = "setop";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sets an owned property for the specified person.\n"
            + "Parameters: INDEX n/NAME_OF_PROPERTY\n"
            + "Example: " + COMMAND_WORD + " 1 n/Marina Bay Apt 12F";

    public static final String MESSAGE_SUCCESS = "Set owned property for %s: %s";
    public static final String MESSAGE_PROP_NOT_FOUND = "Property not found: %s";
    public static final String MESSAGE_DUPLICATE_PROP = "This person already owns the property: %s";
    public static final String MESSAGE_INTEREST_CONFLICT =
            "This person is already marked as interested in the property: %s";

    private final Index index;
    private final String propertyName;

    /**
     * Constructs a {@code SetOwnedPropertyCommand}.
     *
     * @param index index of the person in the last shown list (1-based in UI, zero-based internally); must be non-null
     * @param propertyName exact name of the property to add; must be non-null
     */
    public SetOwnedPropertyCommand(Index index, String propertyName) {
        requireNonNull(index);
        requireNonNull(propertyName);
        this.index = index;
        this.propertyName = propertyName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownList = model.getFilteredPersonList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person target = lastShownList.get(index.getZeroBased());

        Property prop = model.getAddressBook().getPropertyList().stream()
                .filter(p -> p.getPropertyName().toString().trim().equalsIgnoreCase(propertyName.trim()))
                .findFirst()
                .orElseThrow(() -> new CommandException(String.format(MESSAGE_PROP_NOT_FOUND, propertyName)));

        if (target.getInterestedProperties().stream().anyMatch(prop::isSameProperty)) {
            throw new CommandException(String.format(MESSAGE_INTEREST_CONFLICT, propertyName));
        }

        if (target.getOwnedProperties().stream().anyMatch(prop::isSameProperty)) {
            throw new CommandException(String.format(MESSAGE_DUPLICATE_PROP, propertyName));
        }

        List<Property> newOwned = new ArrayList<>(target.getOwnedProperties());
        newOwned.add(prop);

        Person edited = new Person(
                target.getName(),
                target.getPhone(),
                target.getEmail(),
                target.getAddress(),
                target.getTags(),
                newOwned,
                target.getInterestedProperties()
        );

        model.setPerson(target, edited);

        return new CommandResult(String.format(MESSAGE_SUCCESS,
                target.getName().fullName, prop.getPropertyName().toString()));
    }

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
