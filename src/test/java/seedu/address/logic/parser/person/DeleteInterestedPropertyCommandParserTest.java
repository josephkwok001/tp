package seedu.address.logic.parser.person;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
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
        String userInput = "1 n/PropertyName";
        DeleteInterestedPropertyCommand expectedCommand = new DeleteInterestedPropertyCommand(
                new PropertyName("PropertyName"), Index.fromOneBased(1));

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_compulsoryFieldsMissing_failure() {

        // missing name
        String userInputWithoutName = " 1";
        assertParseFailure(parser, userInputWithoutName, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteInterestedPropertyCommand.MESSAGE_USAGE));

        // missing index
        String userInputWithoutIndex = " n/PropertyName";
        assertParseFailure(parser, userInputWithoutName, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteInterestedPropertyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArguments_failure() {
        String userInput = "abc n/PropertyName";
        assertParseFailure(parser, userInput, ParserUtil.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_repeatedValue_failure() {
        String validUserInput = "1 n/PropertyName";

        // multiple names
        assertParseFailure(parser, validUserInput + NAME_DESC_AMY,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));
    }
}
