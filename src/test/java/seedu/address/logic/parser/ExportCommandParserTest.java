package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.commands.exceptions.CommandException;

public class ExportCommandParserTest {

    private final ExportCommandParser parser = new ExportCommandParser();

    @Test
    public void parse_validFilename_returnsExportCommand() throws CommandException {
        String userInput = " clients"; // space prefix simulates actual parser behavior
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

    @Test
    public void parse_filenameWithSpecialCharacters_throwsParseException() {
        assertParseFailure(parser, " file@name",
                "Filename can only contain alphanumeric characters, hyphens, and underscores.");
        assertParseFailure(parser, " file!name",
                "Filename can only contain alphanumeric characters, hyphens, and underscores.");
        assertParseFailure(parser, " file#name",
                "Filename can only contain alphanumeric characters, hyphens, and underscores.");
    }

    @Test
    public void parse_filenameWithSpaces_throwsParseException() {
        assertParseFailure(parser, " file name",
                "Filename can only contain alphanumeric characters, hyphens, and underscores.");
    }

    @Test
    public void parse_filenameWithExtraSpaces_throwsParseException() {
        assertParseFailure(parser, "  clients", "Filename cannot contain extra spaces.");
        assertParseFailure(parser, " clients ", "Filename cannot contain extra spaces.");
    }

    @Test
    public void parse_validFilenameWithHyphensAndUnderscores_success() throws CommandException {
        assertParseSuccess(parser, " my-file", new ExportCommand("my-file"));
        assertParseSuccess(parser, " my_file", new ExportCommand("my_file"));
        assertParseSuccess(parser, " my-file_123", new ExportCommand("my-file_123"));
    }

}
