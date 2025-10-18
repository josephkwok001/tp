package seedu.address;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import seedu.address.commons.core.Config;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.Version;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.util.ConfigUtil;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.storage.AddressBookStorage;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.Storage;
import seedu.address.storage.StorageManager;
import seedu.address.storage.UserPrefsStorage;
import seedu.address.ui.Ui;
import seedu.address.ui.UiManager;

/**
 * Runs the application.
 */
public class MainApp extends Application {

    public static final Version VERSION = new Version(0, 2, 2, true);
    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;

    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing AddressBook ]===========================");
        super.init();

        AppParameters appParameters = AppParameters.parse(getParameters());
        config = initConfig(appParameters.getConfigPath());
        initLogging(config);

        UserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(config.getUserPrefsFilePath());
        UserPrefs userPrefs = initPrefs(userPrefsStorage);
        AddressBookStorage addressBookStorage = new JsonAddressBookStorage(userPrefs.getAddressBookFilePath());
        storage = new StorageManager(addressBookStorage, userPrefsStorage);

        model = initModelManager(storage, userPrefs);
        logic = new LogicManager(model, storage);
        ui = new UiManager(logic);
    }

    private Model initModelManager(Storage storageArg, ReadOnlyUserPrefs userPrefs) {
        logger.info("Using data file : " + storageArg.getAddressBookFilePath());
        ReadOnlyAddressBook initialData;
        try {
            seedu.address.storage.LoadReport report = storageArg.readAddressBookWithReport();
            initialData = report.getModelData().getAddressBook();
            logger.info("Invalid entries detected: " + report.getInvalids().size());
        } catch (seedu.address.commons.exceptions.DataLoadingException e) {
            logger.warning("Data file at " + storageArg.getAddressBookFilePath()
                    + " could not be loaded. Will be starting with an empty AddressBook.");
            initialData = new seedu.address.model.AddressBook();
        } catch (Exception e) {
            logger.warning("Unexpected error loading data: " + e.getMessage());
            initialData = new seedu.address.model.AddressBook();
        }
        return new ModelManager(initialData, userPrefs);
    }

    private void initLogging(Config cfg) {
        LogsCenter.init(cfg);
    }

    /**
     * Loads config from file or returns default config.
     */
    protected Config initConfig(Path configFilePath) {
        Config initializedConfig;
        Path configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if (configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            if (!configOptional.isPresent()) {
                logger.info("Creating new config file " + configFilePathUsed);
            }
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataLoadingException e) {
            logger.warning("Config file at " + configFilePathUsed
                    + " could not be loaded. Using default config properties.");
            initializedConfig = new Config();
        }

        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    private seedu.address.model.AddressBook toValidOnly(seedu.address.storage.LoadReport report) {
        java.util.Set<Integer> badIdx = report.getInvalids().stream()
                .map(seedu.address.storage.LoadReport.InvalidPersonEntry::index)
                .collect(java.util.stream.Collectors.toCollection(java.util.LinkedHashSet::new));
        seedu.address.model.AddressBook filtered = new seedu.address.model.AddressBook();
        java.util.List<seedu.address.model.person.Person> persons =
                report.getModelData().getAddressBook().getPersonList();
        for (int i = 0; i < persons.size(); i++) {
            if (!badIdx.contains(i)) {
                filtered.addPerson(persons.get(i));
            }
        }
        return filtered;
    }

    private String buildInvalidSummary(seedu.address.storage.LoadReport report) {
        java.util.Map<Integer, java.util.List<seedu.address.storage.LoadReport.InvalidPersonEntry>> byIdx =
                report.getInvalids().stream().collect(
                        java.util.stream.Collectors.groupingBy(
                                seedu.address.storage.LoadReport.InvalidPersonEntry::index,
                                java.util.TreeMap::new,
                                java.util.stream.Collectors.toList()
                        )
                );
        int invalidPersonCount = byIdx.size();
        int invalidRecordCount = report.getInvalids().size();
        int validCount = report.getModelData().getAddressBook().getPersonList().size();
        int total = validCount + invalidPersonCount;
        int kept = validCount;

        StringBuilder sb = new StringBuilder();
        sb.append("Some entries in your data file are invalid and will be ignored.\n\n")
                .append(String.format("Total persons: %d\n", total))
                .append(String.format("Invalid persons (ignored): %d\n", invalidPersonCount))
                .append(String.format("Valid persons (loaded): %d\n", kept))
                .append(String.format("Invalid records: %d\n\n", invalidRecordCount))
                .append("Details by person index:\n\n");

        java.util.List<String> allFields = java.util.Arrays.asList("name", "phone", "email", "address", "listing");

        for (var e : byIdx.entrySet()) {
            int idx = e.getKey();
            java.util.Set<String> badFields = new java.util.LinkedHashSet<>();
            for (var inv : e.getValue()) {
                for (String f : allFields) {
                    if (inv.fieldInvalid(f)) {
                        badFields.add(f);
                    }
                }
            }
            sb.append(String.format("• Person #%d\n", idx + 1));
            if (!badFields.isEmpty()) {
                sb.append("  - Invalid fields: ").append(String.join(", ", badFields)).append("\n");
                for (String f : badFields) {
                    String msg;
                    if (f.equals("name")) {
                        msg = seedu.address.model.person.Name.MESSAGE_CONSTRAINTS;
                    } else if (f.equals("phone")) {
                        msg = seedu.address.model.person.Phone.MESSAGE_CONSTRAINTS;
                    } else if (f.equals("email")) {
                        msg = seedu.address.model.person.Email.SHORT_MESSAGE_CONSTRAINTS;
                    } else if (f.equals("address")) {
                        msg = seedu.address.model.person.Address.MESSAGE_CONSTRAINTS;
                    } else if (f.equals("listing")) {
                        msg = seedu.address.model.person.Listing.MESSAGE_CONSTRAINTS;
                    } else {
                        msg = "Invalid value.";
                    }
                    String prettyMsg = msg.replaceAll("(?m)^", "        ");
                    sb.append("    - ").append(f).append(":\n").append(prettyMsg).append("\n");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Loads user preferences from storage or returns defaults.
     */
    protected UserPrefs initPrefs(UserPrefsStorage storageArg) {
        Path prefsFilePath = storageArg.getUserPrefsFilePath();
        logger.info("Using preference file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storageArg.readUserPrefs();
            if (!prefsOptional.isPresent()) {
                logger.info("Creating new preference file " + prefsFilePath);
            }
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataLoadingException e) {
            logger.warning("Preference file at " + prefsFilePath
                    + " could not be loaded. Using default preferences.");
            initializedPrefs = new UserPrefs();
        }

        try {
            storageArg.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting AddressBook " + VERSION);
        primaryStage.setScene(new javafx.scene.Scene(new javafx.scene.Group()));
        try {
            var report = storage.readAddressBookWithReport();
            int invalidCount = report.getInvalids().size();
            logger.info("Invalid entry count at startup: " + invalidCount);
            if (invalidCount > 0) {
                String summary = buildInvalidSummary(report);
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.INFORMATION);
                alert.initOwner(primaryStage);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setTitle("Invalid Entries Detected");
                alert.setHeaderText("Some entries in your data file are invalid and will be ignored.");
                javafx.scene.control.TextArea ta = new javafx.scene.control.TextArea(summary);
                ta.setEditable(false);
                ta.setWrapText(true);
                ta.setPrefRowCount(18);
                alert.getDialogPane().setContent(ta);
                alert.getDialogPane().setMinWidth(640);
                alert.showAndWait();
            }
        } catch (seedu.address.commons.exceptions.DataLoadingException e) {
            logger.warning("Load report failed: " + e.getMessage());
        }
        ui.start(primaryStage);
    }

    /**
     * Reads LoadReport and returns the number of invalid entries.
     * Returns 0 if loading fails.
     */
    private int countInvalidEntries() {
        try {
            seedu.address.storage.LoadReport report = storage.readAddressBookWithReport();
            return report.getInvalids().size();
        } catch (seedu.address.commons.exceptions.DataLoadingException e) {
            logger.warning("Load report failed: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Opens a correction wizard repeatedly until all invalid entries are fixed.
     * Returns false if the user cancels (so the UI should not start),
     * or true once the invalid entries are resolved.
     */
    private boolean runFixWizardUntilClear(Stage owner) {
        while (true) {
            seedu.address.storage.LoadReport report;
            try {
                report = storage.readAddressBookWithReport();
            } catch (seedu.address.commons.exceptions.DataLoadingException e) {
                logger.warning("Load report failed during wizard: " + e.getMessage());
                return false;
            }

            if (report.getInvalids().isEmpty()) {
                return true; // all fixed
            }

            var inv = report.getInvalids().get(0);

            var fieldsOpt = showFixDialog(owner, inv);
            if (fieldsOpt.isEmpty()) {
                return false; // user canceled -> do not start UI
            }
            var f = fieldsOpt.get();

            // Merge: user supplies only invalid fields; keep originals otherwise
            String name = inv.fieldInvalid("name") ? f.name : inv.name();
            String phone = inv.fieldInvalid("phone") ? f.phone : inv.phone();
            String email = inv.fieldInvalid("email") ? f.email : inv.email();
            String address = inv.fieldInvalid("address") ? f.address : inv.address();
            String listing = inv.fieldInvalid("listing") ? f.listing : inv.listing();

            // IMPORTANT: send 1-based index for ParserUtil; parser will convert back to 0-based.
            String cmd = String.format(
                    "fix-invalid i/%d n/%s p/%s e/%s a/%s l/%s",
                    inv.index() + 1, name, phone, email, address, listing
            );

            try {
                logic.execute(cmd);
            } catch (Exception e) {
                logger.info("fix-invalid failed, will prompt again: " + e.getMessage());
                // loop and ask again
            }
        }
    }



    private Optional<PersonFields> showFixDialog(
            Stage owner, seedu.address.storage.LoadReport.InvalidPersonEntry inv) {
        Dialog<PersonFields> dialog = new Dialog<>();
        dialog.initOwner(owner);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Fix Invalid Entry");
        dialog.setHeaderText(
                "Invalid entries were detected:\n"
                        + "Invalid entry detected at index " + (inv.index() + 1) + ":\n"
                        + inv.reason()
                        + "\n\nOnly the highlighted fields are required."
        );

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType skipBtn = new ButtonType("Skip", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, skipBtn);

        TextField name = new TextField(inv.name());
        TextField phone = new TextField(inv.phone());
        TextField email = new TextField(inv.email());
        TextField address = new TextField(inv.address());
        TextField listing = new TextField(inv.listing());

        name.setEditable(inv.fieldInvalid("name"));
        phone.setEditable(inv.fieldInvalid("phone"));
        email.setEditable(inv.fieldInvalid("email"));
        address.setEditable(inv.fieldInvalid("address"));
        listing.setEditable(inv.fieldInvalid("listing"));

        java.util.function.Consumer<TextField> grey = tf -> {
            if (!tf.isEditable()) {
                tf.setStyle("-fx-opacity: 0.75; -fx-control-inner-background: #f4f4f4;");
            }
        };

        grey.accept(name);
        grey.accept(phone);
        grey.accept(email);
        grey.accept(address);
        grey.accept(listing);


        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        grid.setPadding(new Insets(10));
        grid.add(new Label("Name:"), 0, 0);
        grid.add(name, 1, 0);
        grid.add(new Label("Phone:"), 0, 1);
        grid.add(phone, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(email, 1, 2);
        grid.add(new Label("Address:"), 0, 3);
        grid.add(address, 1, 3);
        grid.add(new Label("Listing:"), 0, 4);
        grid.add(listing, 1, 4);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> btn == saveBtn
                ? new PersonFields(name.getText().trim(), phone.getText().trim(),
                email.getText().trim(), address.getText().trim(), listing.getText().trim())
                : null);

        return dialog.showAndWait();
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping AddressBook ] =============================");
        try {
            storage.saveUserPrefs(model.getUserPrefs());
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
    }

    /**
     * Returns true if the on-disk data contains invalid entries.
     */
    private boolean hasInvalidEntries() {
        try {
            seedu.address.storage.LoadReport report = storage.readAddressBookWithReport();
            return !report.getInvalids().isEmpty();
        } catch (seedu.address.commons.exceptions.DataLoadingException e) {
            logger.warning("Load report failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Container for dialog results.
     */
    private static final class PersonFields {
        final String name;
        final String phone;
        final String email;
        final String address;
        final String listing;

        PersonFields(String n, String p, String e, String a, String l) {
            this.name = n;
            this.phone = p;
            this.email = e;
            this.address = a;
            this.listing = l;
        }
    }
}
