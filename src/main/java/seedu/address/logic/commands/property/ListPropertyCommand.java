package seedu.address.logic.commands.property;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PROPERTIES;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.CommandResult.ViewType;
import seedu.address.model.Model;

/**
 * Lists all properties in the address book to the user.
 */
public class ListPropertyCommand extends Command {

    public static final String COMMAND_WORD = "listp";

    public static final String MESSAGE_SUCCESS = "Listed all properties";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPropertyList(PREDICATE_SHOW_ALL_PROPERTIES);

        int count = model.getFilteredPropertyList().size();
        String msg = String.format("%s (%d %s)", MESSAGE_SUCCESS, count,
                count == 1 ? "property" : "properties");
        return new CommandResult(msg, ViewType.PROPERTIES);
    }

    @Override
    public boolean equals(Object other) {
        return other == this || other instanceof ListPropertyCommand;
    }

    @Override
    public int hashCode() {
        return ListPropertyCommand.class.hashCode();
    }
}

