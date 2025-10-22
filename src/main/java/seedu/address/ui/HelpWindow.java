package seedu.address.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String BUTTON_MESSAGE = "Copy URL";
    public static final String COPY_SUCCESSFUL_MESSAGE = "URL Copied!";
    public static final String USERGUIDE_URL = "https://ay2526s1-cs2103t-w12-4.github.io/tp/UserGuide.html";
    public static final String HELP_MESSAGE = "EstateSearch Help\n\n"
            + "========================\n"
            + "  COMMAND SUMMARY\n"
            + "========================\n\n"

            // Managing Contacts
            + "--- Managing Contacts ---\n"
            + "add: Adds a new client to the address book.\n"
            + "  Format: add n/NAME p/PHONE e/EMAIL a/ADDRESS [t/TAG]...\n"
            + "  Example: add n/John Doe p/98765432 e/johnd@example.com a/311, "
            + "Clementi Ave 2 l/The Clementi Mall t/buyer\n\n"

            + "edit: Edits an existing client by their index number.\n"
            + "  Format: edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]...\n"
            + "  Example: edit 1 p/91234567 e/new.email@example.com\n\n"

            + "delete: Deletes a client by their index number.\n"
            + "  Format: delete INDEX\n"
            + "  Example: delete 2\n\n"

            + "clear: Clears all clients from the address book.\n"
            + "  Format: clear\n"
            + "  >> WARNING: This command is irreversible! <<\n\n"

            // Viewing Data
            + "--- Viewing Data ---\n"
            + "list: Shows a list of all clients.\n"
            + "  Format: list\n\n"

            + "find: Finds clients who match all given criteria (case-insensitive).\n"
            + "  Format: find [n/KEYWORD]... [t/KEYWORD]...\n"
            + "  Example: find n/Alex t/buyer\n\n"

            // Application Commands
            + "--- Application ---\n"
            + "help: Shows this help menu.\n"
            + "  Format: help\n\n"

            + "exit: Exits the application.\n"
            + "  Format: exit\n\n";

    public static final String URL_MESSAGE = "For more information refer to the user guide:";

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private Button copyButton;

    @FXML
    private Label helpMessage;

    @FXML
    private Label urlMessage;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        helpMessage.setText(HELP_MESSAGE);
        urlMessage.setText(URL_MESSAGE);
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
    }

    /**
     * Shows the help window.
     * @throws IllegalStateException
     *     <ul>
     *         <li>
     *             if this method is called on a thread other than the JavaFX Application Thread.
     *         </li>
     *         <li>
     *             if this method is called during animation or layout processing.
     *         </li>
     *         <li>
     *             if this method is called on the primary stage.
     *         </li>
     *         <li>
     *             if {@code dialogStage} is already showing.
     *         </li>
     *     </ul>
     */
    public void show() {
        copyButton.setText(BUTTON_MESSAGE);
        logger.fine("Showing help page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    /**
     * Copies the URL to the user guide to the clipboard.
     */
    @FXML
    private void copyUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent url = new ClipboardContent();
        url.putString(USERGUIDE_URL);
        clipboard.setContent(url);
        copyButton.setText(COPY_SUCCESSFUL_MESSAGE);
    }
}
