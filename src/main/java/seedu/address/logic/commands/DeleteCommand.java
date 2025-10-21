package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Email;
import seedu.address.model.person.Person;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the index number used in the displayed person list or by email.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "  or: e/EMAIL\n"
            + "Example: " + COMMAND_WORD + " 1\n"
            + "         " + COMMAND_WORD + " e/example@email.com";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";

    private final Index targetIndex;
    private final Email targetEmail;

    // Constructor for index
    /**
     * Creates a DeleteCommand to delete the person at the specified index.
     *
     * @param targetIndex Index of the person to delete.
     */
    public DeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
        this.targetEmail = null;
    }

    // Constructor for email
    /**
     * Creates a DeleteCommand to delete the person with the specified email.
     *
     * @param targetEmail Email of the person to delete.
     */
    public DeleteCommand(Email targetEmail) {
        this.targetIndex = null;
        this.targetEmail = targetEmail;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (targetIndex != null) {
            List<Person> lastShownList = model.getFilteredPersonList();

            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }

            Person personToDelete = lastShownList.get(targetIndex.getZeroBased());
            model.deletePerson(personToDelete);
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS,
                    Messages.formatPerson(personToDelete)));
        } else if (targetEmail != null) {
            Optional<Person> personToDelete = model.getFilteredPersonList().stream()
                    .filter(p -> p.getEmail().equals(targetEmail))
                    .findFirst();
            if (personToDelete.isEmpty()) {
                throw new CommandException("No person found with the specified email.");
            }
            model.deletePerson(personToDelete.get());
            return new CommandResult(
                    String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.formatPerson(personToDelete.get())));
        } else {
            throw new CommandException("Invalid delete command.");
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof DeleteCommand)) {
            return false;
        }
        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return (targetIndex != null && targetIndex.equals(otherDeleteCommand.targetIndex))
                || (targetEmail != null && targetEmail.equals(otherDeleteCommand.targetEmail));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getCanonicalName()).append("{");
        if (targetIndex != null) {
            sb.append("targetIndex=").append(targetIndex);
        } else if (targetEmail != null) {
            sb.append("targetEmail=").append(targetEmail);
        }
        sb.append("}");
        return sb.toString();
    }
}
