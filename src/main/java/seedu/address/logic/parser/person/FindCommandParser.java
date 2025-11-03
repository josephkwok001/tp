package seedu.address.logic.parser.person;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.address.logic.commands.person.FindCommand;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.TagContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {
    private static final String TAG_VALIDATION_REGEX = "^[a-zA-Z0-9\\s]+$";

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

        if ((trimmedArgs.startsWith("n/") && trimmedArgs.contains(" t/"))
                || (trimmedArgs.startsWith("t/") && trimmedArgs.contains(" n/"))) {
            throw new ParseException("Please use only one search parameter at a time: n/NAME or t/TAG");
        } else if (trimmedArgs.startsWith("n/")) {
            String nameArgs = trimmedArgs.substring(2).trim();
            if (nameArgs.isEmpty()) {
                throw new ParseException("No name provided after n/");
            }
            if (!nameArgs.matches(TAG_VALIDATION_REGEX)) {
                throw new ParseException("Client name can only contain letters, numbers, and spaces");
            }
            String[] nameKeywords = nameArgs.split("\\s+");
            return new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList(nameKeywords)), "name", nameArgs);
        } else if (trimmedArgs.startsWith("t/")) {
            String tagArgs = trimmedArgs.substring(2).trim();
            if (tagArgs.isEmpty()) {
                throw new ParseException("No tag provided after t/");
            }

            if (!tagArgs.matches(TAG_VALIDATION_REGEX)) {
                throw new ParseException("Tag names can only contain letters, numbers, and spaces");
            }

            String[] tagKeywords = tagArgs.split("\\s+");
            return new FindCommand(new TagContainsKeywordsPredicate(Arrays.asList(tagKeywords)), "tag", tagArgs);
        } else {
            throw new ParseException("Command should start with n/NAME or t/TAG for find.");
        }

    }

}
