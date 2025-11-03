package seedu.address.logic.commands.property;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRICE;

import java.util.Optional;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyName;

/**
 * Adds a property to the address book.
 */
public class AddPropertyCommand extends Command {

    public static final String COMMAND_WORD = "addp";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a property to the address book.\n"
            + "Parameters: "
            + PREFIX_ADDRESS + "ADDRESS "
            + PREFIX_PRICE + "PRICE "
            + PREFIX_NAME + "NAME\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_PRICE + "1000000 "
            + PREFIX_NAME + "Hillside Villa";


    public static final String MESSAGE_SUCCESS = "New property added: %1$s";
    public static final String MESSAGE_DUPLICATE_PROPERTY = "This property already exists in the address book";

    private final Property toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddPropertyCommand(Property property) {
        requireNonNull(property);
        toAdd = property;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasProperty(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PROPERTY);
        }

        String newKey = PropertyName.canonicalLoose(toAdd.getPropertyName().toString());
        Optional<Property> similar = model.getAddressBook().getPropertyList().stream()
                .filter(p -> PropertyName.canonicalLoose(p.getPropertyName().toString()).equals(newKey))
                .filter(p -> !p.getPropertyName().equals(toAdd.getPropertyName()))
                .findFirst();

        model.addProperty(toAdd);

        String msg = String.format(MESSAGE_SUCCESS, Messages.formatProperty(toAdd));
        if (similar.isPresent()) {
            msg += String.format(
                    "\nWarning: A similar property name already exists: \"%s\" (differs only by spacing/case).",
                    similar.get().getPropertyName().toString());
        }
        return new CommandResult(msg);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddPropertyCommand)) {
            return false;
        }

        AddPropertyCommand otherAddCommand = (AddPropertyCommand) other;
        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAddProperty", toAdd)
                .toString();
    }
}
