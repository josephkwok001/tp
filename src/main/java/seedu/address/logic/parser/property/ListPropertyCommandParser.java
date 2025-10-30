package seedu.address.logic.parser.property;

import seedu.address.logic.commands.property.ListPropertyCommand;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses input arguments and creates a new ListPropertyCommand object. */
public class ListPropertyCommandParser implements Parser<ListPropertyCommand> {
    @Override
    public ListPropertyCommand parse(String args) throws ParseException {
        return new ListPropertyCommand();
    }
}

