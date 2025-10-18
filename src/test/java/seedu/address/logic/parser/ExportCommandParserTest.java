package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ExportCommand;

public class ExportCommandParserTest {

    private final ExportCommandParser parser = new ExportCommandParser();

    @Test
    public void parse_validFilename_returnsExportCommand() {
        String userInput = "clients"; // no .csv needed
        ExportCommand expectedCommand = new ExportCommand("clients");

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_emptyFilename_throwsParseException() {
        String userInput = "";
        String expectedMessage = String.format(
                seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                ExportCommand.MESSAGE_USAGE
        );

        assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_whitespaceFilename_throwsParseException() {
        String userInput = "   ";
        String expectedMessage = String.format(
                seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                ExportCommand.MESSAGE_USAGE
        );

        assertParseFailure(parser, userInput, expectedMessage);
    }

}
