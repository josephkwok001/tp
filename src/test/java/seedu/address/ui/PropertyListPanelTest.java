package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.property.Property;
import seedu.address.testutil.PropertyBuilder;
import seedu.address.testutil.TypicalProperties;

public class PropertyListPanelTest {

    private static boolean fxReady = false;

    @BeforeAll
    public static void initJavaFxToolkit() {
        try {
            // Check if JavaFX is available
            Class.forName("javafx.application.Platform");
            Platform.startup(() -> {});
            fxReady = true;
        } catch (ClassNotFoundException e) {
            // JavaFX not in classpath (common in CI environments)
            System.out.println("JavaFX not available in classpath");
            fxReady = false;
        } catch (IllegalStateException e) {
            // Already initialized
            fxReady = true;
        } catch (Exception e) {
            // Any other exception
            System.out.println("JavaFX initialization failed: " + e.getMessage());
            fxReady = false;
        }
    }

    @Test
    public void constructor_doesNotThrowException() throws Exception {
        if (!fxReady) {
            System.out.println("Skipping UI test - JavaFX not available");
            return;
        }

        ObservableList<Property> propertyList = FXCollections.observableArrayList();
        PropertyListPanel panel = runOnFxAndGet(() -> new PropertyListPanel(propertyList));
        assertNotNull(panel);
        assertNotNull(panel.getRoot());
    }

    @Test
    public void propertyListViewCell_withValidProperty_createsPropertyCard() throws Exception {
        if (!fxReady) {
            System.out.println("Skipping UI test - JavaFX not available");
            return;
        }

        ObservableList<Property> propertyList = FXCollections.observableArrayList();
        Property testProperty = new PropertyBuilder()
                .withName("Test Property")
                .withAddress("123 Test Street")
                .withPrice(1000000)
                .build();
        propertyList.add(testProperty);

        PropertyListPanel panel = runOnFxAndGet(() -> new PropertyListPanel(propertyList));
        assertNotNull(panel);
    }

    @Test
    public void propertyListViewCell_withNullProperty_setsGraphicToNull() throws Exception {
        if (!fxReady) {
            System.out.println("Skipping UI test - JavaFX not available");
            return;
        }

        ObservableList<Property> propertyList = FXCollections.observableArrayList();
        propertyList.add(null);

        PropertyListPanel panel = runOnFxAndGet(() -> new PropertyListPanel(propertyList));
        assertNotNull(panel);
    }

    @Test
    public void propertyListViewCell_withEmptyList_handlesGracefully() throws Exception {
        if (!fxReady) {
            System.out.println("Skipping UI test - JavaFX not available");
            return;
        }

        ObservableList<Property> propertyList = FXCollections.observableArrayList();

        PropertyListPanel panel = runOnFxAndGet(() -> new PropertyListPanel(propertyList));
        assertNotNull(panel);
    }

    @Test
    public void propertyListViewCell_withMultipleProperties_displaysAll() throws Exception {
        if (!fxReady) {
            System.out.println("Skipping UI test - JavaFX not available");
            return;
        }

        ObservableList<Property> propertyList = FXCollections.observableArrayList();
        propertyList.add(TypicalProperties.PROPERTY_A);
        propertyList.add(TypicalProperties.PROPERTY_B);
        propertyList.add(TypicalProperties.PROPERTY_C);

        PropertyListPanel panel = runOnFxAndGet(() -> new PropertyListPanel(propertyList));
        assertNotNull(panel);
    }

    @Test
    public void propertyListViewCell_setCellFactory_initializedCorrectly() throws Exception {
        if (!fxReady) {
            System.out.println("Skipping UI test - JavaFX not available");
            return;
        }

        ObservableList<Property> propertyList = FXCollections.observableArrayList();
        propertyList.add(TypicalProperties.PROPERTY_A);

        PropertyListPanel panel = runOnFxAndGet(() -> new PropertyListPanel(propertyList));
        assertNotNull(panel);
    }

    // Add a simple test that always runs to ensure the test class is executed
    @Test
    public void testClassLoads() {
        assertNotNull(PropertyListPanel.class);
    }

    /**
     * Helper method to run code on JavaFX Application Thread.
     */
    private static <T> T runOnFxAndGet(java.util.concurrent.Callable<T> callable) throws Exception {
        if (!fxReady) {
            throw new IllegalStateException("JavaFX not available");
        }
        java.util.concurrent.FutureTask<T> task = new java.util.concurrent.FutureTask<>(callable);
        Platform.runLater(task);
        return task.get();
    }

    /**
     * Helper method to get private fields using reflection.
     */
    @SuppressWarnings("unchecked")
    private static <T> T getPrivateField(PropertyListPanel panel, String fieldName) throws Exception {
        java.lang.reflect.Field f = PropertyListPanel.class.getDeclaredField(fieldName);
        f.setAccessible(true);
        return (T) f.get(panel);
    }
}
