package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ListPropertyCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class ListPropertyCommandParserTest {

    private final ListPropertyCommandParser parser = new ListPropertyCommandParser();

    @Test
    public void parse_emptyArgs_returnsListPropertyCommand() throws Exception {
        assertEquals(new ListPropertyCommand(), parser.parse(""));

        assertEquals(new ListPropertyCommand(), parser.parse("   "));

        assertEquals(new ListPropertyCommand(), parser.parse(null));
    }

    @Test
    public void parse_nonEmptyArgs_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("extra"));

        assertThrows(ParseException.class, () -> parser.parse("extra arguments"));

        assertThrows(ParseException.class, () -> parser.parse("  extra  "));
    }
}
