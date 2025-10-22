package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Exports the address book contacts to a CSV file.
 * <p>
 * The CSV file is stored in the {@code data} folder relative to the project root.
 * Each contact is exported with the following fields: Name, Phone, Email, Address, Tags.
 * Tags are separated by semicolons in the CSV. Fields containing commas, quotes, or newlines
 * are properly escaped according to CSV rules.
 *
 */
public class ExportCommand extends Command {
    /** The command word used in the CLI. */
    public static final String COMMAND_WORD = "export";

    /** Usage message explaining how to use the command. */
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Exports the current filtered contacts to a CSV file. "
            + "Parameters: FILENAME\n"
            + "Example: " + COMMAND_WORD + " clients";

    /** Message shown on successful export. */
    public static final String MESSAGE_SUCCESS = "Exported %1$d contacts to %2$s";

    /** Message shown if export fails. */
    public static final String MESSAGE_FAILURE = "Failed to export contacts: %1$s";

    /** The name of the CSV file to export to (automatically appends ".csv"). */
    private final String filename;

    /**
     * Creates an {@code ExportCommand} with the specified CSV filename.
     *
     * @param filename The name of the file to export to (without ".csv").
     */
    public ExportCommand(String filename) {
        requireNonNull(filename);
        this.filename = filename + ".csv";
    }

    /**
     * Executes the export command.
     * <p>
     * Writes all contacts in the filtered person list of the model to a CSV file
     * in the {@code data} folder. Creates the folder if it does not exist.
     *
     * @param model The model containing the address book data.
     * @return A {@code CommandResult} with a success message.
     * @throws CommandException If an {@code IOException} occurs during file writing.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        var peopleToExport = model.getFilteredPersonList();

        if (peopleToExport.isEmpty()) {
            throw new CommandException("No contacts to export.");
        }

        String folder = System.getProperty("user.dir") + "/data/";
        File file = new File(folder + filename);

        File parentFolder = file.getParentFile();
        if (!parentFolder.exists()) {
            parentFolder.mkdirs();
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.append("Name,Phone,Email,Address,Tags\n");

            for (Person p : model.getFilteredPersonList()) {
                writer.append(escapeCsv(p.getName().toString())).append(",");
                writer.append(escapeCsv(p.getPhone().toString())).append(",");
                writer.append(escapeCsv(p.getEmail().toString())).append(",");
                writer.append(escapeCsv(p.getAddress().toString())).append(",");
                writer.append(escapeCsv(
                        p.getTags().stream().map(Tag::toString).collect(Collectors.joining(";"))
                )).append("\n");
            }

            return new CommandResult(String.format(MESSAGE_SUCCESS, model.getFilteredPersonList().size(), filename));

        } catch (IOException e) {
            throw new CommandException(String.format(MESSAGE_FAILURE, e.getMessage()));
        }
    }

    /**
     * Escapes CSV special characters in a string.
     * <p>
     * If the value contains a comma, double quote, or newline, it is wrapped in quotes
     * and any internal quotes are doubled according to CSV rules.
     *
     * @param value The string value to escape.
     * @return The escaped string ready for CSV output.
     */
    private String escapeCsv(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        } else {
            return value;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ExportCommand)) {
            return false;
        }

        ExportCommand otherCommand = (ExportCommand) other;
        return filename.equals(otherCommand.filename);
    }
}



