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
     * Builds a person with tags, creates a Person with owned properties via constructor (no reflection),
     * creates a PersonCard, and verifies text fields, tag order, and owned-property chip rendering.
     */
    @org.junit.jupiter.api.Test
    public void personCard_rendersAllFields_andOwnedProperties() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);
        seedu.address.model.person.Person base = seedu.address.testutil.TypicalPersons.ALICE;
        seedu.address.model.person.Person personWithTags = new seedu.address.testutil.PersonBuilder(base)
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

        seedu.address.model.person.Person personWithData = new seedu.address.model.person.Person(
                personWithTags.getName(),
                personWithTags.getPhone(),
                personWithTags.getEmail(),
                personWithTags.getAddress(),
                personWithTags.getTags(),
                java.util.List.of(prop1, prop2)
        );

        seedu.address.ui.PersonCard card = runOnFxAndGet(() -> new seedu.address.ui.PersonCard(personWithData, 3));

        javafx.scene.control.Label nameLabel = getPrivateField(card, "name");
        javafx.scene.control.Label phoneLabel = getPrivateField(card, "phone");
        javafx.scene.control.Label addressLabel = getPrivateField(card, "address");
        javafx.scene.control.Label emailLabel = getPrivateField(card, "email");
        javafx.scene.layout.FlowPane tagsPane = getPrivateField(card, "tags");
        javafx.scene.layout.FlowPane ownedPane = getPrivateField(card, "ownedProperties");

        org.junit.jupiter.api.Assertions.assertEquals(
                personWithData.getName().fullName, nameLabel.getText());
        org.junit.jupiter.api.Assertions.assertEquals(
                personWithData.getPhone().value, phoneLabel.getText());
        org.junit.jupiter.api.Assertions.assertEquals(
                personWithData.getAddress().value, addressLabel.getText());
        org.junit.jupiter.api.Assertions.assertEquals(
                personWithData.getEmail().value, emailLabel.getText());

        org.junit.jupiter.api.Assertions.assertTrue(tagsPane.getChildren().size() >= 2);
        String first = ((javafx.scene.control.Label) tagsPane.getChildren().get(0)).getText();
        String second = ((javafx.scene.control.Label) tagsPane.getChildren().get(1)).getText();
        org.junit.jupiter.api.Assertions.assertTrue(first.compareTo(second) <= 0);

        int propertyCount = personWithData.getOwnedProperties().size();
        int expectedMin = propertyCount;
        int expectedMax = propertyCount * 2 - 1;

        int actual = ownedPane.getChildren().size();
        org.junit.jupiter.api.Assertions.assertTrue(
                actual >= expectedMin && actual <= expectedMax,
                "Owned properties pane should contain chips (and optional commas). Actual: " + actual
        );
        javafx.scene.control.Label chip0 = (javafx.scene.control.Label) ownedPane.getChildren().get(0);
        org.junit.jupiter.api.Assertions.assertTrue(chip0.getStyleClass().contains("owned-property"));
    }

    /**
     * Verifies that PersonCard handles zero owned properties without rendering chips.
     */
    @org.junit.jupiter.api.Test
    public void personCard_handlesEmptyOwnedProperties() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);
        seedu.address.model.person.Person base =
                new seedu.address.testutil.PersonBuilder(seedu.address.testutil.TypicalPersons.ALICE)
                        .withTags()
                        .build();

        seedu.address.model.person.Person p = new seedu.address.model.person.Person(
                base.getName(),
                base.getPhone(),
                base.getEmail(),
                base.getAddress(),
                base.getTags(),
                java.util.List.of()
        );

        seedu.address.ui.PersonCard card = runOnFxAndGet(() -> new seedu.address.ui.PersonCard(p, 1));
        javafx.scene.layout.FlowPane ownedPane = getPrivateField(card, "ownedProperties");
        org.junit.jupiter.api.Assertions.assertEquals(0, ownedPane.getChildren().size());
    }

    private static <T> T runOnFxAndGet(java.util.concurrent.Callable<T> callable) throws Exception {
        java.util.concurrent.FutureTask<T> task = new java.util.concurrent.FutureTask<>(callable);
        javafx.application.Platform.runLater(task);
        return task.get();
    }

    @SuppressWarnings("unchecked")
    private static <T> T getPrivateField(seedu.address.ui.PersonCard card, String fieldName) throws Exception {
        java.lang.reflect.Field f = seedu.address.ui.PersonCard.class.getDeclaredField(fieldName);
        f.setAccessible(true);
        return (T) f.get(card);
    }
}
