package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    public void constructor_bindsPropertyList_successfully() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);

        ObservableList<Property> propertyList = FXCollections.observableArrayList();
        propertyList.add(TypicalProperties.PROPERTY_A);
        propertyList.add(TypicalProperties.PROPERTY_B);

        PropertyListPanel panel = runOnFxAndGet(() -> new PropertyListPanel(propertyList));

        assertNotNull(panel);
        assertNotNull(panel.getRoot());
    }

    @Test
    public void listView_displaysMultipleProperties_correctly() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);

        ObservableList<Property> propertyList = FXCollections.observableArrayList(
            TypicalProperties.PROPERTY_A,
            TypicalProperties.PROPERTY_B,
            TypicalProperties.PROPERTY_C
        );

        PropertyListPanel panel = runOnFxAndGet(() -> new PropertyListPanel(propertyList));

        ListView<Property> listView = getPrivateField(panel, "propertyListView");
        assertNotNull(listView);
        assertEquals(3, listView.getItems().size());
        assertEquals(TypicalProperties.PROPERTY_A, listView.getItems().get(0));
        assertEquals(TypicalProperties.PROPERTY_B, listView.getItems().get(1));
        assertEquals(TypicalProperties.PROPERTY_C, listView.getItems().get(2));
    }

    @Test
    public void listView_withEmptyList_displaysEmpty() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);

        ObservableList<Property> propertyList = FXCollections.observableArrayList();

        PropertyListPanel panel = runOnFxAndGet(() -> new PropertyListPanel(propertyList));

        ListView<Property> listView = getPrivateField(panel, "propertyListView");
        assertNotNull(listView);
        assertEquals(0, listView.getItems().size());
    }

    @Test
    public void listView_withSingleProperty_displaysCorrectly() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);

        Property property = new PropertyBuilder()
                .withName("Single Property")
                .withAddress("123 Test St")
                .withPrice(500000)
                .build();

        ObservableList<Property> propertyList = FXCollections.observableArrayList(property);

        PropertyListPanel panel = runOnFxAndGet(() -> new PropertyListPanel(propertyList));

        ListView<Property> listView = getPrivateField(panel, "propertyListView");
        assertNotNull(listView);
        assertEquals(1, listView.getItems().size());
        assertEquals(property, listView.getItems().get(0));
    }

    @Test
    public void listView_updates_whenListChanges() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);

        ObservableList<Property> propertyList = FXCollections.observableArrayList(
            TypicalProperties.PROPERTY_A
        );

        PropertyListPanel panel = runOnFxAndGet(() -> new PropertyListPanel(propertyList));

        ListView<Property> listView = getPrivateField(panel, "propertyListView");
        assertEquals(1, listView.getItems().size());

        // Add more properties
        propertyList.add(TypicalProperties.PROPERTY_B);
        propertyList.add(TypicalProperties.PROPERTY_C);

        assertEquals(3, listView.getItems().size());
    }

    @Test
    public void listViewCellRenderer_rendersPropertyCards() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);

        Property property = new PropertyBuilder()
                .withName("Test Property")
                .withAddress("456 Main Ave")
                .withPrice(750000)
                .build();

        ObservableList<Property> propertyList = FXCollections.observableArrayList(property);

        PropertyListPanel panel = runOnFxAndGet(() -> new PropertyListPanel(propertyList));

        ListView<Property> listView = getPrivateField(panel, "propertyListView");
        listView.getSelectionModel().select(0);

        // The cell factory should be set to PropertyListViewCell
        assertNotNull(listView.getCellFactory());
    }

    @Test
    public void constructor_initializesCellFactory() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(fxReady);

        ObservableList<Property> propertyList = FXCollections.observableArrayList(
            TypicalProperties.PROPERTY_A
        );

        PropertyListPanel panel = runOnFxAndGet(() -> new PropertyListPanel(propertyList));

        ListView<Property> listView = getPrivateField(panel, "propertyListView");
        assertNotNull(listView.getCellFactory());
    }

    private static <T> T runOnFxAndGet(java.util.concurrent.Callable<T> callable) throws Exception {
        java.util.concurrent.FutureTask<T> task = new java.util.concurrent.FutureTask<>(callable);
        Platform.runLater(task);
        return task.get();
    }

    @SuppressWarnings("unchecked")
    private static <T> T getPrivateField(PropertyListPanel panel, String fieldName) throws Exception {
        java.lang.reflect.Field f = PropertyListPanel.class.getDeclaredField(fieldName);
        f.setAccessible(true);
        return (T) f.get(panel);
    }
}

