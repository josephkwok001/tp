package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.property.Property;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;

public class MainWindowTest {

    private static boolean fxReady = false;
    private MainWindow mainWindow;
    private Logic logic;

    @BeforeAll
    public static void initJavaFxToolkit() {
        boolean ok = true;
        try {
            Platform.startup(() -> { });
        } catch (IllegalStateException ex) {
            ok = true;
        } catch (UnsupportedOperationException ex) {
            ok = false;
        }
        fxReady = ok;
    }

    @BeforeEach
    public void setUp() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);

        runOnFx(() -> {
            JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(Paths.get("dummy"));
            JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(Paths.get("dummy"));
            StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);

            Model model = new ModelManager();
            logic = new TestLogic(model, storage);

            mainWindow = new MainWindow(new Stage(), logic);
            mainWindow.fillInnerParts();
        });
    }

    private static void runOnFx(Runnable runnable) throws Exception {
        java.util.concurrent.FutureTask<Void> task = new java.util.concurrent.FutureTask<>(runnable, null);
        Platform.runLater(task);
        task.get();
    }

    @Test
    public void constructor_initializesComponents() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);

        runOnFx(() -> {
            assertNotNull(mainWindow);
            assertNotNull(mainWindow.getRoot());
            assertNotNull(mainWindow.getPrimaryStage());
        });
    }

    @Test
    public void getPersonListPanel_returnsNotNull() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);

        runOnFx(() -> {
            PersonListPanel panel = mainWindow.getPersonListPanel();
            assertNotNull(panel);
            assertNotNull(panel.getRoot());
        });
    }

    @Test
    public void getPropertyListPanel_returnsNotNull() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);

        runOnFx(() -> {
            PropertyListPanel panel = mainWindow.getPropertyListPanel();
            assertNotNull(panel);
            assertNotNull(panel.getRoot());
        });
    }

    @Test
    public void show_displaysWindow() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);

        runOnFx(() -> {
            Stage primaryStage = mainWindow.getPrimaryStage();
            mainWindow.show();

            assertTrue(primaryStage.isShowing());
        });
    }

    @Test
    public void handleHelp_opensHelpWindow() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);

        runOnFx(() -> {
            mainWindow.handleHelp();

            // Help window should be showing
            HelpWindow helpWindow = getPrivateField(mainWindow, "helpWindow");
            assertNotNull(helpWindow);
        });
    }

    /**
     * A test implementation of Logic that returns predictable results.
     */
    private static class TestLogic implements Logic {
        private final Model model;
        private final StorageManager storage;

        TestLogic(Model model, StorageManager storage) {
            this.model = model;
            this.storage = storage;
        }

        @Override
        public CommandResult execute(String commandText) throws CommandException, ParseException {
            return new CommandResult("Test feedback");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }

        @Override
        public Path getAddressBookFilePath() {
            return Paths.get("test.json");
        }

        @Override
        public GuiSettings getGuiSettings() {
            return new GuiSettings();
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            // Do nothing for testing
        }

        @Override
        public javafx.collections.ObservableList<Person> getFilteredPersonList() {
            return javafx.collections.FXCollections.observableArrayList();
        }

        @Override
        public javafx.collections.ObservableList<Property> getFilteredPropertyList() {
            return javafx.collections.FXCollections.observableArrayList();
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T getPrivateField(MainWindow mainWindow, String fieldName) {
        try {
            java.lang.reflect.Field f = MainWindow.class.getDeclaredField(fieldName);
            f.setAccessible(true);
            return (T) f.get(mainWindow);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

