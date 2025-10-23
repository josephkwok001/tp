package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.SetOwnedPropertyCommand;
import seedu.address.model.person.Name;

public class SetOwnedPropertyCommandParserTest {

    private final SetOwnedPropertyCommandParser parser = new SetOwnedPropertyCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Index idx = Index.fromOneBased(1);
        String name = "City Loft";
        String userInput = " " + CliSyntax.PREFIX_INDEX + "1 "
                + CliSyntax.PREFIX_NAME + name;

        SetOwnedPropertyCommand expected = new SetOwnedPropertyCommand(idx, name);

        assertParseSuccess(parser, userInput, expected);
    }

    @Test
    public void parse_missingIndex_failure() {
        String userInput = " " + CliSyntax.PREFIX_NAME + "City Loft";
        assertParseFailure(parser, userInput,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, SetOwnedPropertyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingName_failure() {
        String userInput = " " + CliSyntax.PREFIX_INDEX + "1";
        assertParseFailure(parser, userInput,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, SetOwnedPropertyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidIndex_failure() {
        String userInput = " " + CliSyntax.PREFIX_INDEX + "0 "
                + CliSyntax.PREFIX_NAME + "City Loft";
        assertParseFailure(parser, userInput, ParserUtil.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_invalidName_failure() {
        String bad = "  ";
        String userInput = " " + CliSyntax.PREFIX_INDEX + "1 "
                + CliSyntax.PREFIX_NAME + bad;
        assertParseFailure(parser, userInput, Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_duplicatePrefixes_failure() {
        String userInput = " " + CliSyntax.PREFIX_INDEX + "1 "
                + CliSyntax.PREFIX_INDEX + "2 "
                + CliSyntax.PREFIX_NAME + "City Loft";
        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_INDEX));
    }

    @Test
    public void parse_nonEmptyPreamble_failure() {
        String userInput = "abc "
                + CliSyntax.PREFIX_INDEX + "1 "
                + CliSyntax.PREFIX_NAME + "City Loft";
        assertParseFailure(parser, userInput,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, SetOwnedPropertyCommand.MESSAGE_USAGE));
    }
}
