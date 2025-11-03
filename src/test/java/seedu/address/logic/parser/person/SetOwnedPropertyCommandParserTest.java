package seedu.address.logic.parser.person;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.person.SetOwnedPropertyCommand;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.model.property.PropertyName;

public class SetOwnedPropertyCommandParserTest {

    private final SetOwnedPropertyCommandParser parser = new SetOwnedPropertyCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Index idx = Index.fromOneBased(1);
        String name = "City Loft";
        String userInput = " " + "1 "
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
        String userInput = " " + "1";
        assertParseFailure(parser, userInput,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, SetOwnedPropertyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidIndex_failure() {
        String userInput = " " + "0 "
                + CliSyntax.PREFIX_NAME + "City Loft";
        assertParseFailure(parser, userInput, ParserUtil.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_invalidName_failure() {
        String bad = "  ";
        String userInput = " " + "1 "
                + CliSyntax.PREFIX_NAME + bad;
        assertParseFailure(parser, userInput, PropertyName.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_indexwithspace_failure() {
        String userInput = " " + "1 "
                + "2 "
                + CliSyntax.PREFIX_NAME + "City Loft";
        assertParseFailure(parser, userInput,
                ParserUtil.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_indexalphabet_failure() {
        String userInput = "abc "
                + "1 "
                + CliSyntax.PREFIX_NAME + "City Loft";
        assertParseFailure(parser, userInput,
                ParserUtil.MESSAGE_INVALID_INDEX);
    }
}
