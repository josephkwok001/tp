package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.commands.FixInvalidCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListInvalidCommand;
import seedu.address.logic.commands.person.AddCommand;
import seedu.address.logic.commands.person.DeleteCommand;
import seedu.address.logic.commands.person.DeleteInterestedPropertyCommand;
import seedu.address.logic.commands.person.DeleteOwnedPropertyCommand;
import seedu.address.logic.commands.person.EditCommand;
import seedu.address.logic.commands.person.FindCommand;
import seedu.address.logic.commands.person.InterestedPropertyCommand;
import seedu.address.logic.commands.person.ListCommand;
import seedu.address.logic.commands.person.SetOwnedPropertyCommand;
import seedu.address.logic.commands.property.AddPropertyCommand;
import seedu.address.logic.commands.property.DeletePropertyCommand;
import seedu.address.logic.commands.property.EditPropertyCommand;
import seedu.address.logic.commands.property.FindPropertyCommand;
import seedu.address.logic.commands.property.ListPropertyCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.person.AddCommandParser;
import seedu.address.logic.parser.person.DeleteCommandParser;
import seedu.address.logic.parser.person.DeleteInterestedPropertyCommandParser;
import seedu.address.logic.parser.person.DeleteOwnedPropertyCommandParser;
import seedu.address.logic.parser.person.EditCommandParser;
import seedu.address.logic.parser.person.FindCommandParser;
import seedu.address.logic.parser.person.InterestedPropertyCommandParser;
import seedu.address.logic.parser.person.SetOwnedPropertyCommandParser;
import seedu.address.logic.parser.property.AddPropertyCommandParser;
import seedu.address.logic.parser.property.DeletePropertyCommandParser;
import seedu.address.storage.Storage;

/**
 * Parses user input.
 */
public class EstateSearchParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private static final Logger logger = LogsCenter.getLogger(EstateSearchParser.class);

    private final Storage storage;

    public EstateSearchParser(Storage storage) {
        this.storage = storage;
    }

    public EstateSearchParser() {
        this(null);
    }

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        logger.fine("Command word: " + commandWord + "; Arguments: " + arguments);

        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case EditPropertyCommand.COMMAND_WORD:
            return new EditPropertyCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case DeletePropertyCommand.COMMAND_WORD:
            return new DeletePropertyCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case FindPropertyCommand.COMMAND_WORD:
            return new FindPropertyCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case ListPropertyCommand.COMMAND_WORD:
            return new ListPropertyCommandParser().parse(arguments);

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case FixInvalidCommand.COMMAND_WORD:
            ensureStorageAvailableFor("fix-invalid");
            return new FixInvalidCommandParser(storage).parse(arguments);

        case ListInvalidCommand.COMMAND_WORD:
            ensureStorageAvailableFor("list-invalid");
            return new ListInvalidCommand(() -> {
                try {
                    return storage.readAddressBookWithReport();
                } catch (seedu.address.commons.exceptions.DataLoadingException e) {
                    return new seedu.address.storage.LoadReport(
                            new seedu.address.storage.LoadReport.ModelData(new seedu.address.model.AddressBook()),
                            java.util.List.of());
                }
            });

        case ExportCommand.COMMAND_WORD:
            return new ExportCommandParser().parse(arguments);

        case SetOwnedPropertyCommand.COMMAND_WORD:
            return new SetOwnedPropertyCommandParser().parse(arguments);

        case AddPropertyCommand.COMMAND_WORD:
            return new AddPropertyCommandParser().parse(arguments);

        case InterestedPropertyCommand.COMMAND_WORD:
            return new InterestedPropertyCommandParser().parse(arguments);

        case DeleteInterestedPropertyCommand.COMMAND_WORD:
            return new DeleteInterestedPropertyCommandParser().parse(arguments);

        case DeleteOwnedPropertyCommand.COMMAND_WORD:
            return new DeleteOwnedPropertyCommandParser().parse(arguments);

        default:
            logger.finer("This user input caused a ParseException: " + userInput);
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

    private void ensureStorageAvailableFor(String feature) throws ParseException {
        if (storage == null) {
            throw new ParseException("Storage is not available for: " + feature);
        }
    }
}
