package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.property.FindPropertyCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.property.PropertyNameContainsKeywordsPredicate;

public class FindPropertyCommandParserTest {

    private final FindPropertyCommandParser parser = new FindPropertyCommandParser();

    @Test
    public void parse_validNameSingleKeyword_returnsFindPropertyCommand() throws ParseException {
        String userInput = "n/Sunny";
        FindPropertyCommand expected = new FindPropertyCommand(
                new PropertyNameContainsKeywordsPredicate(Arrays.asList("Sunny")));
        FindPropertyCommand result = parser.parse(userInput);
        assertEquals(expected, result);
    }

    @Test
    public void parse_validNameMultipleKeywords_returnsFindPropertyCommand() throws ParseException {
        String userInput = "n/Sunny Villa";
        FindPropertyCommand expected = new FindPropertyCommand(
                new PropertyNameContainsKeywordsPredicate(Arrays.asList("Sunny", "Villa")));
        FindPropertyCommand result = parser.parse(userInput);
        assertEquals(expected, result);
    }

    @Test
    public void parse_missingPrefix_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("Sunny"));
    }

    @Test
    public void parse_emptyNameAfterPrefix_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("n/   "));
    }
}
