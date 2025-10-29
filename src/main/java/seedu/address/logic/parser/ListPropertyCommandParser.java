package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.ListPropertyCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses input arguments and creates a new ListPropertyCommand object. */
public class ListPropertyCommandParser implements Parser<ListPropertyCommand> {
    @Override
    public ListPropertyCommand parse(String args) throws ParseException {
        String trimmed = args == null ? "" : args.trim();
        if (trimmed.isEmpty()) {
            return new ListPropertyCommand();
        }
        throw new ParseException(MESSAGE_INVALID_COMMAND_FORMAT);
    }
}

