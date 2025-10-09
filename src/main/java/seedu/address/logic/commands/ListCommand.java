package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.model.Model;

/** Lists all persons and reports the number listed. */
public class ListCommand extends Command {
    public static final String COMMAND_WORD = "list";
    public static final String MESSAGE_SUCCESS = COMMAND_WORD + ": Lists all persons.\nUsage: " + COMMAND_WORD;

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        int count = model.getFilteredPersonList().size();
        return new CommandResult("Listed " + count + " persons");
    }
}
