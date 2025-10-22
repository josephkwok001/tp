package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.storage.LoadReport;
import seedu.address.storage.Storage;

/**
 * Represents a command that fixes an invalid person entry by overwriting raw storage data.
 */
public class FixInvalidCommand extends Command {
    /**
     * Executes the fix operation on the storage file.
     */
    public static final String COMMAND_WORD = "fix-invalid";
    public static final String MESSAGE_USAGE =
            COMMAND_WORD + ": Fixes a quarantined invalid person at index i/INDEX with corrected fields.\n"
                    + "Parameters: i/INDEX n/NAME p/PHONE e/EMAIL a/ADDRESS\n"
                    + "Example: " + COMMAND_WORD + " i/0 n/Alex p/987 e/alex@x.com a/Blk 1 l/HDB";

    private final int jsonIndex;
    private final Person corrected;
    private final Storage storage;

    /**
     * Constructs a FixInvalidCommand with the specified index, corrected person, and storage.
     *
     * @param jsonIndex The index of the invalid entry in the JSON file.
     * @param corrected The corrected {@code Person} object.
     * @param storage The storage handler to perform overwrite operations.
     */
    public FixInvalidCommand(int jsonIndex, Person corrected, Storage storage) {
        this.jsonIndex = jsonIndex;
        this.corrected = requireNonNull(corrected);
        this.storage = requireNonNull(storage);
    }

    /**
     * Executes the fix operation by overwriting the raw JSON entry and refreshing the model.
     * @throws CommandException if the fix fails or storage cannot be written
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        try {
            // Overwrite the raw JSON entry in place, then get a fresh report
            LoadReport report = storage.overwriteRawEntryAtIndex(jsonIndex, corrected);

            // Replace the in-memory model with the fully reloaded valid content
            model.setAddressBook(report.getModelData().getAddressBook());

            return new CommandResult("Fixed invalid entry at index " + jsonIndex + ".");
        } catch (Exception e) {
            throw new CommandException("Failed to fix invalid entry at index " + jsonIndex + ": " + e.getMessage(), e);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof FixInvalidCommand)) {
            return false;
        }
        FixInvalidCommand o = (FixInvalidCommand) other;
        return this.jsonIndex == o.jsonIndex && this.corrected.equals(o.corrected);
    }
}
