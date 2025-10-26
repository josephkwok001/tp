package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.property.Property;
import seedu.address.testutil.PropertyBuilder;

public class PropertyListPanelTest {

    @Test
    public void constructor_validPropertyList_initializesSuccessfully() {
        ObservableList<Property> propertyList = FXCollections.observableArrayList(
                new PropertyBuilder().build()
        );

        // Execute & Verify
        PropertyListPanel panel = new PropertyListPanel(propertyList);
        assertNotNull(panel.getRoot());
    }

    @Test
    public void constructor_emptyPropertyList_initializesSuccessfully() {
        ObservableList<Property> emptyList = FXCollections.observableArrayList();

        PropertyListPanel panel = new PropertyListPanel(emptyList);
        assertNotNull(panel.getRoot());
    }
}
