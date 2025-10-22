package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_PROPERTY_1;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PROPERTY_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PROPERTY_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PROPERTY_PRICE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_PROPERTY_1;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.PRICE_DESC_PROPERTY_1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_PROPERTY_1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_PROPERTY_1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRICE_PROPERTY_1;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalProperties.PROPERTY_1;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddPropertyCommand;
import seedu.address.model.property.Address;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyName;
import seedu.address.testutil.PropertyBuilder;

public class AddPropertyCommandParserTest {
    private AddPropertyCommandParser parser = new AddPropertyCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Property expectedProperty = new PropertyBuilder(PROPERTY_1).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_PROPERTY_1 + ADDRESS_DESC_PROPERTY_1
                + PRICE_DESC_PROPERTY_1, new AddPropertyCommand(expectedProperty));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPropertyCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_PROPERTY_1 + ADDRESS_DESC_PROPERTY_1 + PRICE_DESC_PROPERTY_1,
                expectedMessage);

        // missing address prefix
        assertParseFailure(parser, NAME_DESC_PROPERTY_1 + PRICE_DESC_PROPERTY_1,
                expectedMessage);

        // missing price prefix
        assertParseFailure(parser, NAME_DESC_PROPERTY_1 + ADDRESS_DESC_PROPERTY_1,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_PROPERTY_1 + VALID_ADDRESS_PROPERTY_1 + VALID_PRICE_PROPERTY_1,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_PROPERTY_NAME_DESC + ADDRESS_DESC_PROPERTY_1
                + PRICE_DESC_PROPERTY_1, PropertyName.MESSAGE_CONSTRAINTS);

        // invalid price
        assertParseFailure(parser, NAME_DESC_PROPERTY_1 + ADDRESS_DESC_PROPERTY_1
                + INVALID_PROPERTY_PRICE_DESC,
                Price.MESSAGE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, NAME_DESC_PROPERTY_1 + PRICE_DESC_PROPERTY_1
                + INVALID_PROPERTY_ADDRESS_DESC, Address.MESSAGE_CONSTRAINTS);


        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_PROPERTY_NAME_DESC + PRICE_DESC_PROPERTY_1
                        + INVALID_PROPERTY_ADDRESS_DESC, PropertyName.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_PROPERTY_1 + PRICE_DESC_PROPERTY_1
                        + ADDRESS_DESC_PROPERTY_1,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPropertyCommand.MESSAGE_USAGE));
    }

}
