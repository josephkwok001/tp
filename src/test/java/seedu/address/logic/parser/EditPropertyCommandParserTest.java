package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.property.EditPropertyCommand;
import seedu.address.logic.commands.property.EditPropertyCommand.EditPropertyDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.property.Address;
import seedu.address.model.property.Price;
import seedu.address.model.property.PropertyName;
import seedu.address.testutil.TypicalIndexes;

public class EditPropertyCommandParserTest {

    private final EditPropertyCommandParser parser = new EditPropertyCommandParser();

    @Test
    public void parse_allFieldsPresent_success() throws Exception {
        String userInput = TypicalIndexes.INDEX_FIRST_PERSON.getOneBased() + " n/NewName a/New Address pr/150000";
        EditPropertyDescriptor descriptor = new EditPropertyDescriptor();
        descriptor.setPropertyName(new PropertyName("NewName"));
        descriptor.setAddress(new Address("New Address"));
        descriptor.setPrice(new Price(150000));
        EditPropertyCommand expected = new EditPropertyCommand(TypicalIndexes.INDEX_FIRST_PERSON, descriptor);

        EditPropertyCommand result = parser.parse(userInput);
        assertEquals(expected, result);
    }

    @Test
    public void parse_invalidPrice_throwsParseException() {
        String userInput = TypicalIndexes.INDEX_FIRST_PERSON.getOneBased() + " pr/notanumber";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }
}
