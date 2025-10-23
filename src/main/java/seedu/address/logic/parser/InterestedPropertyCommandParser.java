package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.InterestedPropertyCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.property.PropertyName;

/**
 * Parses input arguments and creates a new InterestedPropertyCommand object
 */
public class InterestedPropertyCommandParser implements Parser<InterestedPropertyCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the InterestedPropertyCommand
     * and returns an InterestedPropertyCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public InterestedPropertyCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_INDEX, PREFIX_NAME);

        if (!arePrefixesPresent(argMultimap, PREFIX_INDEX, PREFIX_NAME)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    InterestedPropertyCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_INDEX, PREFIX_NAME);
        PropertyName name = ParserUtil.parsePropertyName(argMultimap.getValue(PREFIX_NAME).get());
        Index index = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_INDEX).get());

        return new InterestedPropertyCommand(name, index);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
