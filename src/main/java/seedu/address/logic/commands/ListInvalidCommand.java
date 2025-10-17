package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.storage.LoadReport;

/**
 * Lists invalid entries that were quarantined during the last load.
 * If no invalid entries exist, the command returns "0".
 */
public class ListInvalidCommand extends Command {

    public static final String COMMAND_WORD = "list-invalid";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists entries that failed to load.\n"
            + "Example: " + COMMAND_WORD;

    private final Supplier<LoadReport> reportSupplier;

    /**
     * @param reportSupplier a supplier that returns the latest {@link LoadReport}
     *                       (typically provided by Storage).
     */
    public ListInvalidCommand(Supplier<LoadReport> reportSupplier) {
        this.reportSupplier = Objects.requireNonNull(reportSupplier);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        final LoadReport report = reportSupplier.get();
        final List<LoadReport.InvalidPersonEntry> invalids = report.getInvalids();

        if (invalids.isEmpty()) {
            // Keep this "0" string so the startup check can detect "no invalids".
            return new CommandResult("0");
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < invalids.size(); i++) {
            LoadReport.InvalidPersonEntry inv = invalids.get(i);
            sb.append("#").append(i + 1)
                    .append(" index=").append(inv.index())
                    .append(" - ").append(inv.reason())
                    .append("\n");
        }

        // Trim the trailing newline for neatness.
        return new CommandResult(sb.toString().trim());
    }
}
