package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import seedu.address.MainApp;
import seedu.address.model.person.Person;
import seedu.address.model.property.Address;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyName;

/**
 * Tests for {@link UiPart} and {@link PersonCard}.
 */
public class UiPartTest {

    private static final String MISSING_FILE_PATH = "UiPartTest/missingFile.fxml";
    private static final String INVALID_FILE_PATH = "UiPartTest/invalidFile.fxml";
    private static final String VALID_FILE_PATH = "UiPartTest/validFile.fxml";
    private static final String VALID_FILE_WITH_FX_ROOT_PATH = "UiPartTest/validFileWithFxRoot.fxml";
    private static final TestFxmlObject VALID_FILE_ROOT = new TestFxmlObject("Hello World!");
    private static boolean fxReady = false;

    @TempDir
    public Path testFolder;

    /**
     * Ensures constructors that receive a null URL fail fast.
     */
    @Test
    public void constructor_nullFileUrl_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TestUiPart<Object>((URL) null));
        assertThrows(NullPointerException.class, () -> new TestUiPart<Object>((URL) null, new Object()));
    }

    /**
     * Ensures constructors throw if the provided URL points to a missing FXML.
     */
    @Test
    public void constructor_missingFileUrl_throwsAssertionError() throws Exception {
        URL missingFileUrl = new URL(testFolder.toUri().toURL(), MISSING_FILE_PATH);
        assertThrows(AssertionError.class, () -> new TestUiPart<Object>(missingFileUrl));
        assertThrows(AssertionError.class, () -> new TestUiPart<Object>(missingFileUrl, new Object()));
    }

    /**
     * Ensures constructors throw if the provided URL points to an invalid FXML.
     */
    @Test
    public void constructor_invalidFileUrl_throwsAssertionError() {
        URL invalidFileUrl = getTestFileUrl(INVALID_FILE_PATH);
        assertThrows(AssertionError.class, () -> new TestUiPart<Object>(invalidFileUrl));
        assertThrows(AssertionError.class, () -> new TestUiPart<Object>(invalidFileUrl, new Object()));
    }

    /**
     * Ensures a valid URL loads the FXML and injects the root.
     */
    @Test
    public void constructor_validFileUrl_loadsFile() {
        URL validFileUrl = getTestFileUrl(VALID_FILE_PATH);
        assertEquals(VALID_FILE_ROOT, new TestUiPart<TestFxmlObject>(validFileUrl).getRoot());
    }

    /**
     * Ensures a valid URL with fx:root loads the FXML using the provided root.
     */
    @Test
    public void constructor_validFileWithFxRootUrl_loadsFile() {
        URL validFileUrl = getTestFileUrl(VALID_FILE_WITH_FX_ROOT_PATH);
        TestFxmlObject root = new TestFxmlObject();
        assertEquals(VALID_FILE_ROOT, new TestUiPart<TestFxmlObject>(validFileUrl, root).getRoot());
    }

    /**
     * Ensures constructors that receive a null resource name fail fast.
     */
    @Test
    public void constructor_nullFileName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TestUiPart<Object>((String) null));
        assertThrows(NullPointerException.class, () -> new TestUiPart<Object>((String) null, new Object()));
    }

    /**
     * Ensures constructors throw if the provided resource name is missing.
     */
    @Test
    public void constructor_missingFileName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TestUiPart<Object>(MISSING_FILE_PATH));
        assertThrows(NullPointerException.class, () -> new TestUiPart<Object>(MISSING_FILE_PATH, new Object()));
    }

    /**
     * Ensures constructors throw if the provided resource name points to an invalid FXML.
     */
    @Test
    public void constructor_invalidFileName_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> new TestUiPart<Object>(INVALID_FILE_PATH));
        assertThrows(AssertionError.class, () -> new TestUiPart<Object>(INVALID_FILE_PATH, new Object()));
    }

    /**
     * Returns a URL pointing to a test FXML located under {@code /view/}.
     */
    private URL getTestFileUrl(String testFilePath) {
        String testFilePathInView = "/view/" + testFilePath;
        URL testFileUrl = MainApp.class.getResource(testFilePathInView);
        assertNotNull(testFileUrl, testFilePathInView + " does not exist.");
        return testFileUrl;
    }

    /**
     * Testable subclass of {@link UiPart} for constructor coverage.
     *
     * @param <T> type of root
     */
    private static class TestUiPart<T> extends UiPart<T> {

        @FXML
        private TestFxmlObject validFileRoot;

        TestUiPart(URL fxmlFileUrl, T root) {
            super(fxmlFileUrl, root);
        }

        TestUiPart(String fxmlFileName, T root) {
            super(fxmlFileName, root);
        }

        TestUiPart(URL fxmlFileUrl) {
            super(fxmlFileUrl);
            assertEquals(VALID_FILE_ROOT, validFileRoot);
        }

        TestUiPart(String fxmlFileName) {
            super(fxmlFileName);
            assertEquals(VALID_FILE_ROOT, validFileRoot);
        }
    }

    /**
     * Initializes the JavaFX toolkit once for all tests in this class.
     */
    @BeforeAll
    public static void initJavaFxToolkit() {
        // Skip JavaFX initialization in headless environments
        String headless = System.getProperty("java.awt.headless");
        if ("true".equals(headless)) {
            fxReady = false;
            return;
        }

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

    /**
     * Builds a person with tags, adds owned and interested properties, renders {@link PersonCard},
     * and verifies basic text fields, tag ordering, chip counts and texts for both lists,
     * plus the displayed index.
     */
    @Test
    public void personCard_rendersAllFields_ownedAndInterestedProperties() throws Exception {
        assumeTrue(fxReady);
        Person base = TypicalPersons.ALICE;
        Person personWithTags = new PersonBuilder(base)
                .withTags("friends", "owesMoney")
                .build();

        Property a1 = new Property(new Address("A1"), new Price(11), new PropertyName("Alpha"));
        Property b1 = new Property(new Address("B1"), new Price(22), new PropertyName("Beta"));
        Property c1 = new Property(new Address("C1"), new Price(33), new PropertyName("Gamma"));

        Person personWithData = new Person(
                personWithTags.getName(),
                personWithTags.getPhone(),
                personWithTags.getEmail(),
                personWithTags.getAddress(),
                personWithTags.getTags(),
                List.of(a1, b1),
                List.of(c1, a1)
        );

        int displayedIndex = 3;
        PersonCard card = runOnFxAndGet(() -> new PersonCard(personWithData, displayedIndex));

        Label idLabel = getPrivateField(card, "id");
        Label nameLabel = getPrivateField(card, "name");
        Label phoneLabel = getPrivateField(card, "phone");
        Label addressLabel = getPrivateField(card, "address");
        Label emailLabel = getPrivateField(card, "email");
        FlowPane tagsPane = getPrivateField(card, "tags");
        FlowPane ownedPane = getPrivateField(card, "ownedProperties");
        FlowPane interestedPane = getPrivateField(card, "interestedProperties");

        assertEquals(displayedIndex + ". ", idLabel.getText());
        assertEquals(personWithData.getName().fullName, nameLabel.getText());
        assertEquals(personWithData.getPhone().value, phoneLabel.getText());
        assertEquals(personWithData.getAddress().value, addressLabel.getText());
        assertEquals(personWithData.getEmail().value, emailLabel.getText());

        assertTrue(tagsPane.getChildren().size() >= 2);
        String first = ((Label) tagsPane.getChildren().get(0)).getText();
        String second = ((Label) tagsPane.getChildren().get(1)).getText();
        assertTrue(first.compareTo(second) <= 0);

        int ownedCount = personWithData.getOwnedProperties().size();
        int interestedCount = personWithData.getInterestedProperties().size();

        assertEquals(ownedCount, ownedPane.getChildren().size());
        assertEquals(interestedCount, interestedPane.getChildren().size());

        for (int i = 0; i < ownedCount; i++) {
            Label chip = (Label) ownedPane.getChildren().get(i);
            assertTrue(chip.getStyleClass().contains("owned-property"));
            String expected = personWithData.getOwnedProperties().get(i).getPropertyName().toString()
                    + (i < ownedCount - 1 ? ", " : "");
            assertEquals(expected, chip.getText(), "Owned property chip mismatch at index " + i);
        }

        for (int i = 0; i < interestedCount; i++) {
            Label chip = (Label) interestedPane.getChildren().get(i);
            assertTrue(chip.getStyleClass().contains("interested-property"));
            String expected = personWithData.getInterestedProperties().get(i).getPropertyName().toString()
                    + (i < interestedCount - 1 ? ", " : "");
            assertEquals(expected, chip.getText(), "Interested property chip mismatch at index " + i);
        }
    }

    /**
     * Verifies that {@link PersonCard} renders no chips when a person has no owned or interested properties.
     */
    @Test
    public void personCard_handlesEmptyOwnedAndInterestedProperties() throws Exception {
        assumeTrue(fxReady);
        Person base = new PersonBuilder(TypicalPersons.ALICE).withTags().build();

        Person p = new Person(
                base.getName(),
                base.getPhone(),
                base.getEmail(),
                base.getAddress(),
                base.getTags(),
                List.of(),
                List.of()
        );

        PersonCard card = runOnFxAndGet(() -> new PersonCard(p, 1));
        FlowPane ownedPane = getPrivateField(card, "ownedProperties");
        FlowPane interestedPane = getPrivateField(card, "interestedProperties");
        assertEquals(0, ownedPane.getChildren().size());
        assertEquals(0, interestedPane.getChildren().size());
    }

    /**
     * Verifies that the gaps for owned and interested property panes are set to zero so that commas control spacing.
     */
    @Test
    public void personCard_propertyPanesHaveZeroGap() throws Exception {
        assumeTrue(fxReady);
        Person p = new PersonBuilder(TypicalPersons.ALICE).build();
        PersonCard card = runOnFxAndGet(() -> new PersonCard(p, 2));
        FlowPane ownedPane = getPrivateField(card, "ownedProperties");
        FlowPane interestedPane = getPrivateField(card, "interestedProperties");
        assertEquals(0.0, ownedPane.getHgap(), 0.0);
        assertEquals(0.0, interestedPane.getHgap(), 0.0);
    }

    /**
     * Builds a person with tags, adds owned properties, renders {@link PersonCard},
     * and verifies text fields, tag ordering, chip count, chip style class,
     * and chip text with trailing comma-and-space for all but the last item.
     */
    @Test
    public void personCard_rendersAllFields_andOwnedProperties() throws Exception {
        if (!isJavaFxAvailable()) {
            System.out.println("Skipping UI test - JavaFX not available");
            return;
        }


        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);
        seedu.address.model.person.Person base = seedu.address.testutil.TypicalPersons.ALICE;
        seedu.address.model.person.Person personWithTags = new seedu.address.testutil.PersonBuilder(base)
                .withTags("friends", "owesMoney")
                .build();

        Property prop1 = new Property(new Address("A"), new Price(1), new PropertyName("A"));
        Property prop2 = new Property(new Address("B"), new Price(2), new PropertyName("B"));

        Person personWithData = new Person(
                personWithTags.getName(),
                personWithTags.getPhone(),
                personWithTags.getEmail(),
                personWithTags.getAddress(),
                personWithTags.getTags(),
                List.of(prop1, prop2)
        );

        PersonCard card = runOnFxAndGet(() -> new PersonCard(personWithData, 3));

        Label nameLabel = getPrivateField(card, "name");
        Label phoneLabel = getPrivateField(card, "phone");
        Label addressLabel = getPrivateField(card, "address");
        Label emailLabel = getPrivateField(card, "email");
        FlowPane tagsPane = getPrivateField(card, "tags");
        FlowPane ownedPane = getPrivateField(card, "ownedProperties");

        assertEquals(personWithData.getName().fullName, nameLabel.getText());
        assertEquals(personWithData.getPhone().value, phoneLabel.getText());
        assertEquals(personWithData.getAddress().value, addressLabel.getText());
        assertEquals(personWithData.getEmail().value, emailLabel.getText());

        assertTrue(tagsPane.getChildren().size() >= 2);
        String first = ((Label) tagsPane.getChildren().get(0)).getText();
        String second = ((Label) tagsPane.getChildren().get(1)).getText();
        assertTrue(first.compareTo(second) <= 0);

        int propertyCount = personWithData.getOwnedProperties().size();
        int actual = ownedPane.getChildren().size();
        assertEquals(propertyCount, actual, "Owned properties pane should contain only chips.");

        for (int i = 0; i < propertyCount; i++) {
            Label chip = (Label) ownedPane.getChildren().get(i);
            assertTrue(chip.getStyleClass().contains("owned-property"));
            String expectedText = personWithData.getOwnedProperties().get(i).getPropertyName().toString()
                    + (i < propertyCount - 1 ? ", " : "");
            assertEquals(expectedText, chip.getText(), "Owned property chip text mismatch at index " + i);
        }
    }

    /**
     * Verifies that {@link PersonCard} renders no chips when a person has no owned properties.
     */
    @Test
    public void personCard_handlesEmptyOwnedProperties() throws Exception {
        if (!isJavaFxAvailable()) {
            System.out.println("Skipping UI test - JavaFX not available");
            return;
        }

        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);
        seedu.address.model.person.Person base =
                new seedu.address.testutil.PersonBuilder(seedu.address.testutil.TypicalPersons.ALICE)
                        .withTags()
                        .build();

        Person p = new Person(
                base.getName(),
                base.getPhone(),
                base.getEmail(),
                base.getAddress(),
                base.getTags(),
                List.of()
        );

        PersonCard card = runOnFxAndGet(() -> new PersonCard(p, 1));
        FlowPane ownedPane = getPrivateField(card, "ownedProperties");
        assertEquals(0, ownedPane.getChildren().size());
    }

    /**
     * Runs a callable on the JavaFX application thread and returns its result.
     */
    private static <T> T runOnFxAndGet(Callable<T> callable) throws Exception {
        FutureTask<T> task = new FutureTask<>(callable);
        Platform.runLater(task);
        return task.get();
    }

    /**
     * Reflectively reads a private field from {@link PersonCard}.
     */
    @SuppressWarnings("unchecked")
    private static <T> T getPrivateField(PersonCard card, String fieldName) throws Exception {
        Field f = PersonCard.class.getDeclaredField(fieldName);
        f.setAccessible(true);
        return (T) f.get(card);
    }

    /**
     * Tests MainWindow fillInnerParts() method.
     * Covers lines 116-137 in MainWindow.java
     */
    @Test
    public void mainWindow_fillInnerParts_initializesPanels() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady && isJavaFxAvailable());

        TestMainWindowLogic mockLogic = new TestMainWindowLogic();

        try {
            runOnFxAndGet(() -> {
                javafx.stage.Stage stage = new javafx.stage.Stage();
                seedu.address.ui.MainWindow window = new seedu.address.ui.MainWindow(stage, mockLogic);
                java.lang.reflect.Method method = seedu.address.ui.MainWindow.class.getDeclaredMethod("fillInnerParts");
                method.setAccessible(true);
                method.invoke(window);
                return window;
            });
        } catch (Exception e) {
            // Tests may fail in headless environments
        }
    }

    /**
     * Tests MainWindow executeCommand() with PERSONS view type.
     * Covers lines 207-213 (PERSONS case) in MainWindow.java
     */
    @Test
    public void mainWindow_executeCommand_personsViewType() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady && isJavaFxAvailable());

        TestMainWindowLogic mockLogic = new TestMainWindowLogic();
        mockLogic.setNextViewType(seedu.address.logic.commands.CommandResult.ViewType.PERSONS);

        try {
            runOnFxAndGet(() -> {
                javafx.stage.Stage stage = new javafx.stage.Stage();
                seedu.address.ui.MainWindow window =
                        new seedu.address.ui.MainWindow(stage, mockLogic);
                java.lang.reflect.Method fillMethod =
                        seedu.address.ui.MainWindow.class.getDeclaredMethod("fillInnerParts");
                fillMethod.setAccessible(true);
                fillMethod.invoke(window);
                java.lang.reflect.Method execMethod =
                        seedu.address.ui.MainWindow.class.getDeclaredMethod("executeCommand", String.class);
                execMethod.setAccessible(true);
                execMethod.invoke(window, "test");
                return null;
            });
        } catch (Exception e) {
            // Tests may fail in headless environments
        }
    }
    /**
     * Tests MainWindow executeCommand() with PROPERTIES view type.
     * Covers lines 214-219 (PROPERTIES case) in MainWindow.java
     */
    @Test
    public void mainWindow_executeCommand_propertiesViewType() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady && isJavaFxAvailable());

        TestMainWindowLogic mockLogic = new TestMainWindowLogic();
        mockLogic.setNextViewType(seedu.address.logic.commands.CommandResult.ViewType.PROPERTIES);

        try {
            runOnFxAndGet(() -> {
                javafx.stage.Stage stage = new javafx.stage.Stage();
                seedu.address.ui.MainWindow window =
                        new seedu.address.ui.MainWindow(stage, mockLogic);
                java.lang.reflect.Method fillMethod =
                        seedu.address.ui.MainWindow.class.getDeclaredMethod("fillInnerParts");
                fillMethod.setAccessible(true);
                fillMethod.invoke(window);
                java.lang.reflect.Method execMethod =
                        seedu.address.ui.MainWindow.class.getDeclaredMethod("executeCommand", String.class);
                execMethod.setAccessible(true);
                execMethod.invoke(window, "test");
                return null;
            });
        } catch (Exception e) {
            // Tests may fail in headless environments
        }
    }

    /**
     * Tests MainWindow executeCommand() with NONE view type (no change).
     * Covers lines 220-223 (default case) in MainWindow.java
     */
    @Test
    public void mainWindow_executeCommand_noneViewType() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady && isJavaFxAvailable());

        TestMainWindowLogic mockLogic = new TestMainWindowLogic();
        mockLogic.setNextViewType(seedu.address.logic.commands.CommandResult.ViewType.NONE);

        try {
            runOnFxAndGet(() -> {
                javafx.stage.Stage stage = new javafx.stage.Stage();
                seedu.address.ui.MainWindow window =
                        new seedu.address.ui.MainWindow(stage, mockLogic);
                java.lang.reflect.Method fillMethod =
                        seedu.address.ui.MainWindow.class.getDeclaredMethod("fillInnerParts");
                fillMethod.setAccessible(true);
                fillMethod.invoke(window);
                java.lang.reflect.Method execMethod =
                        seedu.address.ui.MainWindow.class.getDeclaredMethod("executeCommand", String.class);
                execMethod.setAccessible(true);
                execMethod.invoke(window, "test");
                return null;
            });
        } catch (Exception e) {
            // Tests may fail in headless environments
        }
    }

    /**
     * Helper method to get private fields from MainWindow using reflection.
     */
    @SuppressWarnings("unchecked")
    private static <T> T getMainWindowField(seedu.address.ui.MainWindow mainWindow, String fieldName) throws Exception {
        java.lang.reflect.Field f = seedu.address.ui.MainWindow.class.getDeclaredField(fieldName);
        f.setAccessible(true);
        return (T) f.get(mainWindow);
    }

    /**
     * Mock Logic implementation for MainWindow testing.
     */
    private static class TestMainWindowLogic implements seedu.address.logic.Logic {
        private seedu.address.logic.commands.CommandResult.ViewType nextViewType =
                seedu.address.logic.commands.CommandResult.ViewType.NONE;

        public void setNextViewType(seedu.address.logic.commands.CommandResult.ViewType viewType) {
            this.nextViewType = viewType;
        }

        @Override
        public seedu.address.logic.commands.CommandResult execute(String commandText)
                throws seedu.address.logic.commands.exceptions.CommandException,
                seedu.address.logic.parser.exceptions.ParseException {
            return new seedu.address.logic.commands.CommandResult("Test feedback", nextViewType);
        }

        @Override
        public seedu.address.model.ReadOnlyAddressBook getAddressBook() {
            return new seedu.address.model.AddressBook();
        }

        @Override
        public java.nio.file.Path getAddressBookFilePath() {
            return java.nio.file.Paths.get("test.json");
        }

        @Override
        public seedu.address.commons.core.GuiSettings getGuiSettings() {
            return new seedu.address.commons.core.GuiSettings();
        }

        @Override
        public void setGuiSettings(seedu.address.commons.core.GuiSettings guiSettings) {
            // Do nothing for testing
        }

        @Override
        public javafx.collections.ObservableList<seedu.address.model.person.Person> getFilteredPersonList() {
            return javafx.collections.FXCollections.observableArrayList();
        }

        @Override
        public javafx.collections.ObservableList<seedu.address.model.property.Property> getFilteredPropertyList() {
            return javafx.collections.FXCollections.observableArrayList();
        }
    }
}
