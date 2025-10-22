package seedu.address.logic;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.storage.Storage;

/**
 * The main logic layer entry point.
 * Parses user input into commands and persists model updates via {@link Storage}.
 * Only mutating commands trigger saving to storage.
 */
public class LogicManager implements Logic {

    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";
    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
            "Could not save data to file %s due to insufficient permissions to write to the file or the folder.";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final AddressBookParser addressBookParser;

    /**
     * Creates a {@code LogicManager}.
     *
     * @param model   application model to mutate and query.
     * @param storage storage backend used to persist model changes.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        this.addressBookParser = new AddressBookParser(storage);
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        Command command = addressBookParser.parseCommand(commandText);
        CommandResult commandResult = command.execute(model);

        if (isMutatingCommand(command)) {
            try {
                storage.saveAddressBook(model.getAddressBook());
            } catch (AccessDeniedException e) {
                throw new CommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
            } catch (IOException ioe) {
                throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
            }
        }

        return commandResult;
    }

    /**
     * Determines if the given command modifies the AddressBook data.
     * Non-mutating commands (such as list, find, help, exit) do not trigger saving.
     */
    private boolean isMutatingCommand(Command command) {
        String name = command.getClass().getSimpleName();
        return name.equals("AddCommand")
                || name.equals("EditCommand")
                || name.equals("DeleteCommand")
                || name.equals("ClearCommand")
                || name.equals("FixInvalidCommand")
                || name.equals("ImportCommand")
                || name.equals("ExportCommand")
                || name.equals("TagCommand")
                || name.equals("UntagCommand")
                || name.equals("SetOwnedPropertyCommand")
                || name.equals("AddPropertyCommand");
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return model.getAddressBook();
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    @Override
    public Path getAddressBookFilePath() {
        return model.getAddressBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}
