package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.property.ListPropertyCommand;
import seedu.address.logic.parser.property.ListPropertyCommandParser;

/** Parses input arguments and creates a new ListPropertyCommand object. */
public class ListPropertyCommandParserTest {

    private final ListPropertyCommandParser parser = new ListPropertyCommandParser();

    @Test
    public void parse_emptyArgs_returnsListPropertyCommand() throws Exception {
        assertEquals(new ListPropertyCommand(), parser.parse(""));

        assertEquals(new ListPropertyCommand(), parser.parse("   "));

        assertEquals(new ListPropertyCommand(), parser.parse(null));
    }

    @Test
    public void parse_nonEmptyArgs_returnsListPropertyCommand() throws Exception {
        assertEquals(new ListPropertyCommand(), parser.parse("extra"));

        assertEquals(new ListPropertyCommand(), parser.parse("extra arguments"));

        assertEquals(new ListPropertyCommand(), parser.parse("  extra  "));
    }
}
