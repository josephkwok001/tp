package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.TagContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        if (trimmedArgs.startsWith("n/")) {
            String nameArgs = trimmedArgs.substring(2).trim();
            if (nameArgs.isEmpty()) {
                throw new ParseException("No name provided after n/");
            }
            String[] nameKeywords = nameArgs.split("\\s+");
            return new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
        } else if (trimmedArgs.startsWith("t/")) {
            String tagArgs = trimmedArgs.substring(2).trim();
            if (tagArgs.isEmpty()) {
                throw new ParseException("No tag provided after t/");
            }
            String[] tagKeywords = tagArgs.split("\\s+");
            return new FindCommand(new TagContainsKeywordsPredicate(Arrays.asList(tagKeywords)));
        } else {
            throw new ParseException("Command should start with n/NAME or t/TAG for find.");
        }

    }

}
