package seedu.address.logic.parser.person;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.person.FindCommand;
import seedu.address.model.person.NameContainsKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        List<String> keywords = Arrays.asList("Alice", "Bob");
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(keywords), "name", "Alice Bob");

        assertParseSuccess(parser, "n/Alice Bob", expectedFindCommand);

        assertParseSuccess(parser, "n/ \n Alice \n \t Bob  \t", expectedFindCommand);
    }

    @Test
    public void parse_emptyTagValue_throwsParseException() {
        assertParseFailure(parser, "t/", "No tag provided after t/");

        assertParseFailure(parser, "t/   ", "No tag provided after t/");
    }

    @Test
    public void parse_nameAndTagCombined_throwsParseException() {
        assertParseFailure(parser, "n/ t/", "Please use only one search parameter at a time: n/NAME or t/TAG");

        assertParseFailure(parser, "n/Alice t/friends",
                "Please use only one search parameter at a time: n/NAME or t/TAG");

        assertParseFailure(parser, "t/friends n/Alice",
                "Please use only one search parameter at a time: n/NAME or t/TAG");
    }

}
