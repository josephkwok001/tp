package seedu.address.logic.parser.person;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.person.DeleteInterestedPropertyCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.Prefix;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.property.PropertyName;

/**
 * Parses input arguments and creates a new DeleteInterestedPropertyCommand object
 */
public class DeleteInterestedPropertyCommandParser implements Parser<DeleteInterestedPropertyCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * DeleteInterestedPropertyCommand and returns a DeleteInterestedPropertyCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public DeleteInterestedPropertyCommand parse(String args) throws ParseException {

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_INDEX, PREFIX_NAME);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_INDEX)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    DeleteInterestedPropertyCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_INDEX);
        PropertyName name = ParserUtil.parsePropertyName(argMultimap.getValue(PREFIX_NAME).get());
        Index index = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_INDEX).get());

        return new DeleteInterestedPropertyCommand(name, index);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
