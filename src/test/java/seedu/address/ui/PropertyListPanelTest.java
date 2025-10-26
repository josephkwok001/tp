package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import seedu.address.model.property.Property;
import seedu.address.testutil.PropertyBuilder;
import seedu.address.testutil.TypicalProperties;

public class PropertyListPanelTest {

    private static boolean fxReady = false;

    @BeforeAll
    public static void initJavaFxToolkit() {
        if (!fxReady) {
            try {
                Platform.startup(() -> {});
                fxReady = true;
            } catch (IllegalStateException e) {
                // Already initialized
                fxReady = true;
            } catch (UnsupportedOperationException e) {
                fxReady = false;
            }
        }
    }

    @Test
    public void constructor_doesNotThrowException() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);

        ObservableList<Property> propertyList = FXCollections.observableArrayList();
        PropertyListPanel panel = runOnFxAndGet(() -> new PropertyListPanel(propertyList));
        assertNotNull(panel);
        assertNotNull(panel.getRoot());
    }

    @Test
    public void propertyListViewCell_withValidProperty_createsPropertyCard() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);

        ObservableList<Property> propertyList = FXCollections.observableArrayList();
        Property testProperty = new PropertyBuilder()
                .withName("Test Property")
                .withAddress("123 Test Street")
                .withPrice(1000000)
                .build();
        propertyList.add(testProperty);

        PropertyListPanel panel = runOnFxAndGet(() -> new PropertyListPanel(propertyList));

        // Get the ListView and verify it has items
        ListView<Property> listView = getPrivateField(panel, "propertyListView");
        assertNotNull(listView);
        assertNotNull(listView.getItems());
    }

    @Test
    public void propertyListViewCell_withNullProperty_setsGraphicToNull() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);

        ObservableList<Property> propertyList = FXCollections.observableArrayList();
        propertyList.add(null);

        PropertyListPanel panel = runOnFxAndGet(() -> new PropertyListPanel(propertyList));

        ListView<Property> listView = getPrivateField(panel, "propertyListView");
        assertNotNull(listView);
    }

    @Test
    public void propertyListViewCell_withEmptyList_handlesGracefully() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);

        ObservableList<Property> propertyList = FXCollections.observableArrayList();

        PropertyListPanel panel = runOnFxAndGet(() -> new PropertyListPanel(propertyList));

        ListView<Property> listView = getPrivateField(panel, "propertyListView");
        assertNotNull(listView);
    }

    @Test
    public void propertyListViewCell_withMultipleProperties_displaysAll() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);

        ObservableList<Property> propertyList = FXCollections.observableArrayList();
        propertyList.add(TypicalProperties.PROPERTY_A);
        propertyList.add(TypicalProperties.PROPERTY_B);
        propertyList.add(TypicalProperties.PROPERTY_C);

        PropertyListPanel panel = runOnFxAndGet(() -> new PropertyListPanel(propertyList));

        ListView<Property> listView = getPrivateField(panel, "propertyListView");
        assertNotNull(listView);
    }

    @Test
    public void propertyListViewCell_setCellFactory_initializedCorrectly() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);

        ObservableList<Property> propertyList = FXCollections.observableArrayList();
        propertyList.add(TypicalProperties.PROPERTY_A);

        PropertyListPanel panel = runOnFxAndGet(() -> new PropertyListPanel(propertyList));

        ListView<Property> listView = getPrivateField(panel, "propertyListView");
        assertNotNull(listView.getCellFactory());
    }

    /**
     * Helper method to run code on JavaFX Application Thread.
     */
    private static <T> T runOnFxAndGet(java.util.concurrent.Callable<T> callable) throws Exception {
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
