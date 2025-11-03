package seedu.address.logic.parser.person;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.person.DeleteCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Email;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * DeleteCommand
     * and returns a DeleteCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_EMAIL);

        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            String emailArg = argMultimap.getValue(PREFIX_EMAIL).get().trim();
            if (emailArg.isEmpty()) {
                throw new ParseException(Email.MESSAGE_CONSTRAINTS);
            }
            Email email = ParserUtil.parseEmail(emailArg);
            return new DeleteCommand(email);
        } else if (!argMultimap.getPreamble().isEmpty()) {
            Index index = ParserUtil.parseIndex(args.trim());
            return new DeleteCommand(index);
        } else {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }
    }
}
