package seedu.address.logic.commands.person;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Find contacts by name or tag and displays them as a "
            + "list.\n"
            + "Parameters: find n/NAME OR find t/TAG\n"
            + "Example: " + COMMAND_WORD + " n/Alice\n"
            + "Example: " + COMMAND_WORD + " t/Friends";

    private final Predicate<Person> predicate;
    private final String searchType;
    private final String keywords;

    /**
     * Creates a FindCommand to find persons matching the given predicate.
     *
     * @param predicate The predicate to filter persons.
     * @param searchType The type of search (e.g., "name" or "tag").
     * @param keywords The keywords used for searching.
     */
    public FindCommand(Predicate<Person> predicate, String searchType, String keywords) {
        this.predicate = predicate;
        this.searchType = searchType;
        this.keywords = keywords;
    }


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        String resultMessage = String.format("Found %d person%s matching %s: %s",
                model.getFilteredPersonList().size(),
                model.getFilteredPersonList().size() == 1 ? "" : "s",
                searchType, keywords);
        return new CommandResult(resultMessage);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
