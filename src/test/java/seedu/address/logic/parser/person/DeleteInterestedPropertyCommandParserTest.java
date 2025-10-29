package seedu.address.logic.parser.person;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.person.DeleteInterestedPropertyCommand;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.model.property.PropertyName;


public class DeleteInterestedPropertyCommandParserTest {

    private final DeleteInterestedPropertyCommandParser parser = new DeleteInterestedPropertyCommandParser();

    @Test
    public void parse_allFieldsPresent_success() throws Exception {
        String userInput = " n/PropertyName i/1";
        DeleteInterestedPropertyCommand expectedCommand = new DeleteInterestedPropertyCommand(
                new PropertyName("PropertyName"), Index.fromOneBased(1));

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_compulsoryFieldsMissing_failure() {

        // missing name
        String userInputWithoutName = " i/1";
        assertParseFailure(parser, userInputWithoutName, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteInterestedPropertyCommand.MESSAGE_USAGE));

        // missing index
        String userInputWithoutIndex = " n/PropertyName";
        assertParseFailure(parser, userInputWithoutName, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteInterestedPropertyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArguments_failure() {
        String userInput = " n/PropertyName i/abc";
        assertParseFailure(parser, userInput, ParserUtil.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_repeatedValue_failure() {
        String validUserInput = " n/PropertyName i/1";

        // multiple names
        assertParseFailure(parser, NAME_DESC_AMY + validUserInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple indexes
        assertParseFailure(parser, validUserInput + " i/2",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_INDEX));
    }
}
