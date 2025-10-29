package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRICE;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.property.EditPropertyCommand;
import seedu.address.logic.commands.property.EditPropertyCommand.EditPropertyDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.property.Price;

/**
 * Parses input arguments and creates a new EditPropertyCommand object
 */
public class EditPropertyCommandParser implements Parser<EditPropertyCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditPropertyCommand
     * and returns an EditPropertyCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditPropertyCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PRICE);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, EditPropertyCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PRICE);

        EditPropertyDescriptor editPropertyDescriptor = new EditPropertyDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editPropertyDescriptor.setPropertyName(ParserUtil.parsePropertyName(
                argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            editPropertyDescriptor.setAddress(ParserUtil.parsePropertyAddress(
                argMultimap.getValue(PREFIX_ADDRESS).get()));
        }
        if (argMultimap.getValue(PREFIX_PRICE).isPresent()) {
            try {
                editPropertyDescriptor.setPrice(ParserUtil.parsePrice(
                    Integer.parseInt(argMultimap.getValue(PREFIX_PRICE).get())));
            } catch (NumberFormatException e) {
                throw new ParseException(Price.MESSAGE_CONSTRAINTS);
            }
        }

        if (!editPropertyDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditPropertyCommand.MESSAGE_NOT_EDITED);
        }

        return new EditPropertyCommand(index, editPropertyDescriptor);
    }
}
