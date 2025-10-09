package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses input arguments and creates a ListCommand. */
public class ListCommandParser implements Parser<ListCommand> {
    /** Parses args and returns a ListCommand, rejecting any extraneous tokens. */
    public ListCommand parse(String args) throws ParseException {
        if (!args.trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_SUCCESS));
        }
        return new ListCommand();
    }
}
