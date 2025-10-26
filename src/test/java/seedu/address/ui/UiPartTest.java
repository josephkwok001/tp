package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

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
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.TypicalPersons;

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

    /**
     * Builds a person with tags, creates a Person with owned properties via constructor (no reflection),
     * creates a PersonCard, and verifies text fields, tag order, and owned-property chip rendering.
     */
    @Test
    public void personCard_rendersAllFields_andOwnedProperties() throws Exception {
        assumeTrue(fxReady);
        Person base = TypicalPersons.ALICE;
        Person personWithTags = new PersonBuilder(base)
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
        int expectedMin = propertyCount;
        int expectedMax = propertyCount * 2 - 1;

        int actual = ownedPane.getChildren().size();
        assertTrue(
                actual >= expectedMin && actual <= expectedMax,
                "Owned properties pane should contain chips (and optional commas). Actual: " + actual
        );
        Label chip0 = (Label) ownedPane.getChildren().get(0);
        assertTrue(chip0.getStyleClass().contains("owned-property"));
    }

    /**
     * Verifies that PersonCard handles zero owned properties without rendering chips.
     */
    @Test
    public void personCard_handlesEmptyOwnedProperties() throws Exception {
        assumeTrue(fxReady);
        Person base = new PersonBuilder(TypicalPersons.ALICE)
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

    private static <T> T runOnFxAndGet(Callable<T> callable) throws Exception {
        FutureTask<T> task = new FutureTask<>(callable);
        Platform.runLater(task);
        return task.get();
    }

    @SuppressWarnings("unchecked")
    private static <T> T getPrivateField(PersonCard card, String fieldName) throws Exception {
        Field f = PersonCard.class.getDeclaredField(fieldName);
        f.setAccessible(true);
        return (T) f.get(card);
    }
}
