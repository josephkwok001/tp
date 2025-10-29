package seedu.address.logic.parser.person;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.person.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class ListCommandParserTest {

    private final ListCommandParser parser = new ListCommandParser();

    @Test
    public void parse_emptyArg_returnsListCommand() throws Exception {
        // empty
        assertParseSuccess(parser, "", new ListCommand());
        // whitespaces only
        assertParseSuccess(parser, "   \t  \n", new ListCommand());
    }

    @Test
    public void parse_nonEmptyArg_throwsParseException() {
        // any non-whitespace should be rejected by parser
        assertThrows(ParseException.class, () -> parser.parse("abc"));
        assertThrows(ParseException.class, () -> parser.parse("  extra"));
        assertThrows(ParseException.class, () -> parser.parse("--all"));
    }
}
