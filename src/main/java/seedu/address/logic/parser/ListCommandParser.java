package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a {@link ListCommand}.
 */
public class ListCommandParser implements Parser<ListCommand> {

    /**
     * Parses the given {@code args} and returns a {@link ListCommand}.
     * Rejects any extraneous tokens (the list command takes no arguments).
     *
     * @param args arguments string after the command word
     * @return a {@code ListCommand}
     * @throws ParseException if any arguments are provided
     */
    public ListCommand parse(String args) throws ParseException {
        if (!args.trim().isEmpty()) {
            throw new ParseException("List command does not take any arguments.");
        }
        return new ListCommand();
    }
}
