package seedu.address.logic.commands.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.CommandResult.ViewType;
import seedu.address.model.Model;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all persons";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        int count = model.getFilteredPersonList().size();
        String msg = String.format("%s (%d %s)", MESSAGE_SUCCESS, count, count == 1 ? "person" : "persons");
        return new CommandResult(msg, ViewType.PERSONS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this || other instanceof ListCommand;
    }

    @Override
    public int hashCode() {
        return ListCommand.class.hashCode();
    }
}
