package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static seedu.address.testutil.Assert.assertThrows;

import java.net.URL;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javafx.fxml.FXML;
import seedu.address.MainApp;

public class UiPartTest {

    private static final String MISSING_FILE_PATH = "UiPartTest/missingFile.fxml";
    private static final String INVALID_FILE_PATH = "UiPartTest/invalidFile.fxml";
    private static final String VALID_FILE_PATH = "UiPartTest/validFile.fxml";
    private static final String VALID_FILE_WITH_FX_ROOT_PATH = "UiPartTest/validFileWithFxRoot.fxml";
    private static final TestFxmlObject VALID_FILE_ROOT = new TestFxmlObject("Hello World!");
    /**
     * Flag indicating whether JavaFX toolkit is available in this runtime.
     */
    private static boolean fxReady = false;

    @TempDir
    public Path testFolder;

    @Test
    public void constructor_nullFileUrl_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TestUiPart<Object>((URL) null));
        assertThrows(NullPointerException.class, () -> new TestUiPart<Object>((URL) null, new Object()));
    }

    @Test
    public void constructor_missingFileUrl_throwsAssertionError() throws Exception {
        URL missingFileUrl = new URL(testFolder.toUri().toURL(), MISSING_FILE_PATH);
        assertThrows(AssertionError.class, () -> new TestUiPart<Object>(missingFileUrl));
        assertThrows(AssertionError.class, () -> new TestUiPart<Object>(missingFileUrl, new Object()));
    }

    @Test
    public void constructor_invalidFileUrl_throwsAssertionError() {
        URL invalidFileUrl = getTestFileUrl(INVALID_FILE_PATH);
        assertThrows(AssertionError.class, () -> new TestUiPart<Object>(invalidFileUrl));
        assertThrows(AssertionError.class, () -> new TestUiPart<Object>(invalidFileUrl, new Object()));
    }

    @Test
    public void constructor_validFileUrl_loadsFile() {
        URL validFileUrl = getTestFileUrl(VALID_FILE_PATH);
        assertEquals(VALID_FILE_ROOT, new TestUiPart<TestFxmlObject>(validFileUrl).getRoot());
    }

    @Test
    public void constructor_validFileWithFxRootUrl_loadsFile() {
        URL validFileUrl = getTestFileUrl(VALID_FILE_WITH_FX_ROOT_PATH);
        TestFxmlObject root = new TestFxmlObject();
        assertEquals(VALID_FILE_ROOT, new TestUiPart<TestFxmlObject>(validFileUrl, root).getRoot());
    }

    @Test
    public void constructor_nullFileName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TestUiPart<Object>((String) null));
        assertThrows(NullPointerException.class, () -> new TestUiPart<Object>((String) null, new Object()));
    }

    @Test
    public void constructor_missingFileName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TestUiPart<Object>(MISSING_FILE_PATH));
        assertThrows(NullPointerException.class, () -> new TestUiPart<Object>(MISSING_FILE_PATH, new Object()));
    }

    @Test
    public void constructor_invalidFileName_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> new TestUiPart<Object>(INVALID_FILE_PATH));
        assertThrows(AssertionError.class, () -> new TestUiPart<Object>(INVALID_FILE_PATH, new Object()));
    }

    private URL getTestFileUrl(String testFilePath) {
        String testFilePathInView = "/view/" + testFilePath;
        URL testFileUrl = MainApp.class.getResource(testFilePathInView);
        assertNotNull(testFileUrl, testFilePathInView + " does not exist.");
        return testFileUrl;
    }

    /**
     * UiPart used for testing.
     * It should only be used with invalid FXML files or the valid file located at {@link VALID_FILE_PATH}.
     */
    private static class TestUiPart<T> extends UiPart<T> {

        @FXML
        private TestFxmlObject validFileRoot; // Check that @FXML annotations work

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
     * Initializes JavaFX toolkit for UI tests in this class.
     */
    @org.junit.jupiter.api.BeforeAll
    public static void initJavaFxToolkit() {
        boolean ok = true;
        try {
            javafx.application.Platform.startup(() -> { });
        } catch (IllegalStateException ex) {
            ok = true;
        } catch (UnsupportedOperationException ex) {
            ok = false;
        }
        fxReady = ok;
    }

    /**
     * Builds a person with tags, injects owned properties reflectively, creates a PersonCard, and verifies text fields,
     * tag rendering order, and owned-property chip rendering with expected style class.
     */
    @org.junit.jupiter.api.Test
    public void personCard_rendersAllFields_andOwnedProperties() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);
        seedu.address.model.person.Person base = seedu.address.testutil.TypicalPersons.ALICE;
        seedu.address.model.person.Person personWithData = new seedu.address.testutil.PersonBuilder(base)
                .withTags("friends", "owesMoney")
                .build();

        seedu.address.model.property.Property prop1 =
                new seedu.address.model.property.Property(
                        new seedu.address.model.property.Address("A"),
                        new seedu.address.model.property.Price(1),
                        new seedu.address.model.property.PropertyName("A"));
        seedu.address.model.property.Property prop2 =
                new seedu.address.model.property.Property(
                        new seedu.address.model.property.Address("B"),
                        new seedu.address.model.property.Price(2),
                        new seedu.address.model.property.PropertyName("B"));

        injectOwnedPropertiesReflectively(personWithData, java.util.List.of(prop1, prop2));

        seedu.address.ui.PersonCard card = runOnFxAndGet(() -> new seedu.address.ui.PersonCard(personWithData, 3));

        javafx.scene.control.Label nameLabel = getPrivateField(card, "name");
        javafx.scene.control.Label phoneLabel = getPrivateField(card, "phone");
        javafx.scene.control.Label addressLabel = getPrivateField(card, "address");
        javafx.scene.control.Label emailLabel = getPrivateField(card, "email");
        javafx.scene.layout.FlowPane tagsPane = getPrivateField(card, "tags");
        javafx.scene.layout.FlowPane ownedPane = getOwnedPropertiesPane(card);

        org.junit.jupiter.api.Assertions.assertEquals(
                personWithData.getName().fullName, nameLabel.getText());
        org.junit.jupiter.api.Assertions.assertEquals(
                personWithData.getPhone().value, phoneLabel.getText());
        org.junit.jupiter.api.Assertions.assertEquals(
                personWithData.getAddress().value, addressLabel.getText());
        org.junit.jupiter.api.Assertions.assertEquals(
                personWithData.getEmail().value, emailLabel.getText());

        org.junit.jupiter.api.Assertions.assertTrue(
                tagsPane.getChildren().size() >= 2);

        String first = ((javafx.scene.control.Label) tagsPane.getChildren().get(0)).getText();
        String second = ((javafx.scene.control.Label) tagsPane.getChildren().get(1)).getText();
        org.junit.jupiter.api.Assertions.assertTrue(first.compareTo(second) <= 0);

        org.junit.jupiter.api.Assertions.assertEquals(2, ownedPane.getChildren().size());

        javafx.scene.control.Label chip0 =
                (javafx.scene.control.Label) ownedPane.getChildren().get(0);
        org.junit.jupiter.api.Assertions.assertTrue(
                chip0.getStyleClass().contains("owned-property"));
    }

    /**
     * Verifies that PersonCard handles zero owned properties without rendering chips.
     */
    @org.junit.jupiter.api.Test
    public void personCard_handlesEmptyOwnedProperties() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);
        seedu.address.model.person.Person p = new seedu.address.testutil.PersonBuilder(
                seedu.address.testutil.TypicalPersons.ALICE).withTags().build();
        injectOwnedPropertiesReflectively(p, java.util.List.of());
        seedu.address.ui.PersonCard card = runOnFxAndGet(() -> new seedu.address.ui.PersonCard(p, 1));
        javafx.scene.layout.FlowPane ownedPane = getPrivateField(card, "ownedProperties");
        org.junit.jupiter.api.Assertions.assertEquals(0, ownedPane.getChildren().size());
    }

    /**
     * Runs a callable on the JavaFX Application Thread and returns the result.
     */
    private static <T> T runOnFxAndGet(java.util.concurrent.Callable<T> callable) throws Exception {
        java.util.concurrent.FutureTask<T> task = new java.util.concurrent.FutureTask<>(callable);
        javafx.application.Platform.runLater(task);
        return task.get();
    }

    /**
     * Reflectively obtains a private field from PersonCard.
     */
    @SuppressWarnings("unchecked")
    private static <T> T getPrivateField(seedu.address.ui.PersonCard card, String fieldName) throws Exception {
        java.lang.reflect.Field f = seedu.address.ui.PersonCard.class.getDeclaredField(fieldName);
        f.setAccessible(true);
        return (T) f.get(card);
    }

    /**
     * Reflectively injects an OwnedProperties instance into a Person.
     */
    private static void injectOwnedPropertiesReflectively(seedu.address.model.person.Person p,
                                                          java.util.List<seedu.address.model.property.Property> props)
            throws Exception {
        Class<?> ownedPropsCls = Class.forName("seedu.address.model.person.OwnedProperties");
        Object owned = ownedPropsCls.getConstructor(java.util.List.class).newInstance(props);
        java.lang.reflect.Field f = p.getClass().getDeclaredField("ownedProperties");
        f.setAccessible(true);
        f.set(p, owned);
    }

    /**
     * Returns the first matching private field from PersonCard by trying candidate names.
     */
    @SuppressWarnings("unchecked")
    private static <T> T getPrivateFieldAny(seedu.address.ui.PersonCard card, String... candidates) throws Exception {
        for (String name : candidates) {
            try {
                java.lang.reflect.Field f = seedu.address.ui.PersonCard.class.getDeclaredField(name);
                f.setAccessible(true);
                return (T) f.get(card);
            } catch (NoSuchFieldException ignore) {
                assert true;
            }
        }
        throw new NoSuchFieldException(java.util.Arrays.toString(candidates));
    }

    /**
     * Finds the FlowPane in PersonCard that contains labels with the "owned-property" style class.
     */
    private static javafx.scene.layout.FlowPane getOwnedPropertiesPane(seedu.address.ui.PersonCard card)
            throws Exception {
        for (java.lang.reflect.Field f : seedu.address.ui.PersonCard.class.getDeclaredFields()) {
            if (f.getType() == javafx.scene.layout.FlowPane.class) {
                f.setAccessible(true);
                Object val = f.get(card);
                if (val instanceof javafx.scene.layout.FlowPane) {
                    javafx.scene.layout.FlowPane pane = (javafx.scene.layout.FlowPane) val;
                    boolean hasOwnedChip = pane.getChildren().stream()
                            .filter(n -> n instanceof javafx.scene.control.Label)
                            .map(n -> (javafx.scene.control.Label) n)
                            .anyMatch(lbl -> lbl.getStyleClass().contains("owned-property"));
                    if (hasOwnedChip) {
                        return pane;
                    }
                }
            }
        }
        throw new NoSuchFieldException("owned properties pane not found");
    }
}
