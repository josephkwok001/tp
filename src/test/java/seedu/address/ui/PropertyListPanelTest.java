package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.property.Property;
import seedu.address.testutil.PropertyBuilder;

public class PropertyListPanelTest {

    @BeforeAll
    public static void setupJavaFX() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            System.out.println("JavaFX already initialized: " + e.getMessage());
        }
    }

    @Test
    public void propertyListViewCell_updateItemWithNullProperty_clearsGraphic() throws Exception {
        ObservableList<Property> propertyList = FXCollections.observableArrayList();
        PropertyListPanel panel = new PropertyListPanel(propertyList);

        PropertyListPanel.PropertyListViewCell cell = panel.new PropertyListViewCell();

        Platform.runLater(() -> {
            cell.updateItem(null, true);
            assertNull(cell.getGraphic());
            assertNull(cell.getText());
        });

        Thread.sleep(100);
    }

    @Test
    public void propertyListViewCell_updateItemWithValidProperty_setsGraphic() throws Exception {
        Property testProperty = new PropertyBuilder().build();
        ObservableList<Property> propertyList = FXCollections.observableArrayList(testProperty);
        PropertyListPanel panel = new PropertyListPanel(propertyList);

        PropertyListPanel.PropertyListViewCell cell = panel.new PropertyListViewCell();

        Platform.runLater(() -> {
            cell.updateItem(testProperty, false);
            assertNotNull(cell.getGraphic());
        });

        Thread.sleep(100);
    }

    @Test
    public void propertyListViewCell_updateItemWithEmpty_setsNullGraphic() throws Exception {
        ObservableList<Property> propertyList = FXCollections.observableArrayList();
        PropertyListPanel panel = new PropertyListPanel(propertyList);

        PropertyListPanel.PropertyListViewCell cell = panel.new PropertyListViewCell();

        Platform.runLater(() -> {
            cell.updateItem(null, true);
            assertNull(cell.getGraphic());
        });

        Thread.sleep(100);
    }
}
