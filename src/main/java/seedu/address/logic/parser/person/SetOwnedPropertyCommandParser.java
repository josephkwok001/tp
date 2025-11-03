package seedu.address.logic.parser.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.person.SetOwnedPropertyCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.Prefix;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.property.PropertyName;

/**
 * Parses input arguments to create a {@link SetOwnedPropertyCommand}.
 * <p>
 * Expected format: {@code INDEX n/PROPERTY_NAME}. Both prefixes are required and the preamble must be empty.
 * Duplicate prefixes are rejected.
 */
public class SetOwnedPropertyCommandParser implements Parser<SetOwnedPropertyCommand> {

    @Override
    public SetOwnedPropertyCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, CliSyntax.PREFIX_NAME);

        if (!arePrefixesPresent(argMultimap, CliSyntax.PREFIX_NAME)
                || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    SetOwnedPropertyCommand.MESSAGE_USAGE));
        }
        Index index = ParserUtil.parseIndex(argMultimap.getPreamble());
        argMultimap.verifyNoDuplicatePrefixesFor(CliSyntax.PREFIX_NAME);

        String propertyNameRaw = argMultimap.getValue(CliSyntax.PREFIX_NAME).get().trim();
        if (!PropertyName.isValidName(propertyNameRaw)) {
            throw new ParseException(PropertyName.MESSAGE_CONSTRAINTS);
        }

        return new SetOwnedPropertyCommand(index, propertyNameRaw);
    }

    private static boolean arePrefixesPresent(ArgumentMultimap am, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> am.getValue(prefix).isPresent());
    }
}
