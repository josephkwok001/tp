package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.FixInvalidCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.storage.Storage;

/**
 * Parses input arguments and creates a {@code FixInvalidCommand}.
 */
public class FixInvalidCommandParser implements Parser<FixInvalidCommand> {

    private final Storage storage;

    public FixInvalidCommandParser(Storage storage) {
        this.storage = storage;
    }

    /**
     * Parses the given {@code String} of arguments and returns a {@code FixInvalidCommand}.
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public FixInvalidCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_INDEX, PREFIX_NAME, PREFIX_PHONE,
                        PREFIX_EMAIL, PREFIX_ADDRESS);

        if (!argMultimap.getValue(PREFIX_INDEX).isPresent()
                || !argMultimap.getValue(PREFIX_NAME).isPresent()
                || !argMultimap.getValue(PREFIX_PHONE).isPresent()
                || !argMultimap.getValue(PREFIX_EMAIL).isPresent()
                || !argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            throw new ParseException("Missing required fields. Usage: " + FixInvalidCommand.MESSAGE_USAGE);
        }

        Index parsed = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_INDEX).get());
        int jsonIndex = parsed.getZeroBased(); // this will become 0 for i/1

        Name name = new Name(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = new Phone(argMultimap.getValue(PREFIX_PHONE).get());
        Email email = new Email(argMultimap.getValue(PREFIX_EMAIL).get());
        Address address = new Address(argMultimap.getValue(PREFIX_ADDRESS).get());

        Person corrected = new Person(name, phone, email, address, new java.util.HashSet<>());

        return new FixInvalidCommand(jsonIndex, corrected, storage);
    }
}
