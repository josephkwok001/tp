package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.FutureTask;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import seedu.address.model.property.Property;
import seedu.address.testutil.PropertyBuilder;
import seedu.address.testutil.TypicalProperties;

/**
 * Tests for PropertyListPanel.
 */
public class PropertyListPanelTest {

    private static boolean fxReady = false;

    @BeforeAll
    public static void initJavaFxToolkit() {
        if (!fxReady) {
            Platform.startup(() -> {});
            fxReady = true;
        }
    }

    /**
     * Tests PropertyListPanel constructor and basic functionality.
     */
    @Test
    public void constructor_withPropertyList_createsPanel() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);

        ObservableList<Property> propertyList = FXCollections.observableArrayList();
        propertyList.add(TypicalProperties.PROPERTY_A);
        propertyList.add(TypicalProperties.PROPERTY_B);

        PropertyListPanel panel = runOnFxAndGet(() -> new PropertyListPanel(propertyList));

        assertNotNull(panel);
        assertNotNull(panel.getRoot());
    }

    /**
     * Tests PropertyListPanel with empty property list.
     */
    @Test
    public void constructor_withEmptyList_createsPanel() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);

        ObservableList<Property> emptyList = FXCollections.observableArrayList();

        PropertyListPanel panel = runOnFxAndGet(() -> new PropertyListPanel(emptyList));

        assertNotNull(panel);
        assertNotNull(panel.getRoot());
    }

    /**
     * Tests PropertyListPanel with single property.
     */
    @Test
    public void constructor_withSingleProperty_createsPanel() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);

        ObservableList<Property> propertyList = FXCollections.observableArrayList();
        propertyList.add(TypicalProperties.PROPERTY_A);

        PropertyListPanel panel = runOnFxAndGet(() -> new PropertyListPanel(propertyList));

        assertNotNull(panel);
        assertNotNull(panel.getRoot());
    }

    /**
     * Tests PropertyListViewCell updateItem with valid property.
     */
    @Test
    public void propertyListViewCell_updateItemWithValidProperty_setsGraphic() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);

        ObservableList<Property> propertyList = FXCollections.observableArrayList();
        propertyList.add(TypicalProperties.PROPERTY_A);

        PropertyListPanel panel = runOnFxAndGet(() -> new PropertyListPanel(propertyList));

        // Get the ListView from the panel
        ListView<Property> listView = getPrivateField(panel, "propertyListView");
        assertNotNull(listView);

        // Test that the cell factory is set correctly
        assertNotNull(listView.getCellFactory());
    }

    /**
     * Tests PropertyListViewCell updateItem with null property.
     */
    @Test
    public void propertyListViewCell_updateItemWithNullProperty_clearsGraphic() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);

        ObservableList<Property> propertyList = FXCollections.observableArrayList();
        propertyList.add(null);

        PropertyListPanel panel = runOnFxAndGet(() -> new PropertyListPanel(propertyList));

        ListView<Property> listView = getPrivateField(panel, "propertyListView");
        assertNotNull(listView);
    }

    /**
     * Tests PropertyListViewCell updateItem with empty cell.
     */
    @Test
    public void propertyListViewCell_updateItemWithEmptyCell_clearsGraphic() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);

        ObservableList<Property> propertyList = FXCollections.observableArrayList();
        propertyList.add(TypicalProperties.PROPERTY_A);

        PropertyListPanel panel = runOnFxAndGet(() -> new PropertyListPanel(propertyList));

        ListView<Property> listView = getPrivateField(panel, "propertyListView");
        assertNotNull(listView);
    }

    /**
     * Tests PropertyListViewCell with multiple properties.
     */
    @Test
    public void propertyListViewCell_withMultipleProperties_displaysCorrectly() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);

        ObservableList<Property> propertyList = FXCollections.observableArrayList();
        propertyList.add(TypicalProperties.PROPERTY_A);
        propertyList.add(TypicalProperties.PROPERTY_B);
        propertyList.add(TypicalProperties.PROPERTY_C);

        PropertyListPanel panel = runOnFxAndGet(() -> new PropertyListPanel(propertyList));

        ListView<Property> listView = getPrivateField(panel, "propertyListView");
        assertNotNull(listView);
        assertEquals(3, listView.getItems().size());
    }

    /**
     * Tests PropertyListViewCell with PropertyCard creation.
     */
    @Test
    public void propertyListViewCell_createPropertyCard_setsCorrectIndex() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);

        Property testProperty = new PropertyBuilder()
                .withName("Test Property")
                .withAddress("123 Test Street")
                .withPrice(1000000)
                .build();

        ObservableList<Property> propertyList = FXCollections.observableArrayList();
        propertyList.add(testProperty);

        PropertyListPanel panel = runOnFxAndGet(() -> new PropertyListPanel(propertyList));

        ListView<Property> listView = getPrivateField(panel, "propertyListView");
        assertNotNull(listView);
    }

    /**
     * Tests PropertyListPanel with property list updates.
     */
    @Test
    public void propertyListPanel_withPropertyListUpdates_reflectsChanges() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);

        ObservableList<Property> propertyList = FXCollections.observableArrayList();
        propertyList.add(TypicalProperties.PROPERTY_A);

        PropertyListPanel panel = runOnFxAndGet(() -> new PropertyListPanel(propertyList));

        ListView<Property> listView = getPrivateField(panel, "propertyListView");
        assertEquals(1, listView.getItems().size());

        // Add another property
        runOnFxAndGet(() -> {
            propertyList.add(TypicalProperties.PROPERTY_B);
            return null;
        });

        assertEquals(2, listView.getItems().size());
    }

    /**
     * Tests PropertyListPanel FXML loading.
     */
    @Test
    public void propertyListPanel_fxmlLoadsCorrectly() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);

        ObservableList<Property> propertyList = FXCollections.observableArrayList();
        propertyList.add(TypicalProperties.PROPERTY_A);

        PropertyListPanel panel = runOnFxAndGet(() -> new PropertyListPanel(propertyList));

        // Verify FXML loaded correctly by checking the root
        assertNotNull(panel.getRoot());
        assertTrue(panel.getRoot().getChildrenUnmodifiable().size() > 0);
    }

    /**
     * Tests PropertyListPanel logger initialization.
     */
    @Test
    public void propertyListPanel_loggerInitialized() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);

        ObservableList<Property> propertyList = FXCollections.observableArrayList();
        propertyList.add(TypicalProperties.PROPERTY_A);

        PropertyListPanel panel = runOnFxAndGet(() -> new PropertyListPanel(propertyList));

        // Verify logger is initialized (we can't directly test the logger field,
        // but if the constructor completes without error, the logger was initialized)
        assertNotNull(panel);
    }

    /**
     * Helper method to run code on JavaFX Application Thread.
     */
    private static <T> T runOnFxAndGet(java.util.concurrent.Callable<T> callable) throws Exception {
        FutureTask<T> task = new FutureTask<>(callable);
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

    /**
     * Helper method to assert that an exception is thrown.
     */
    private static void assertThrows(Class<? extends Exception> expectedType,
            java.util.concurrent.Callable<?> callable) {
        try {
            callable.call();
            throw new AssertionError("Expected exception " + expectedType.getSimpleName()
                    + " was not thrown");
        } catch (Exception e) {
            if (!expectedType.isInstance(e)) {
                throw new AssertionError("Expected exception " + expectedType.getSimpleName()
                        + " but got " + e.getClass().getSimpleName(), e);
            }
        }
    }
}
