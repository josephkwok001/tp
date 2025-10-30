package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.property.Property;

/**
 * Panel containing the list of properties.
 */
public class PropertyListPanel extends UiPart<Region> {
    private static final String FXML = "PropertyListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PropertyListPanel.class);

    @FXML
    private ListView<Property> propertyListView;

    /**
     * Creates a {@code PropertyListPanel} with the given {@code ObservableList}.
     */
    public PropertyListPanel(ObservableList<Property> propertyList) {
        super(FXML);
        propertyListView.setFixedCellSize(-1);
        propertyListView.setItems(propertyList);
        propertyListView.setMaxHeight(Double.MAX_VALUE);
        HBox.setHgrow(propertyListView, Priority.ALWAYS);

        propertyListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Property property, boolean empty) {
                super.updateItem(property, empty);

                if (empty || property == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    PropertyCard card = new PropertyCard(property, getIndex() + 1);
                    setGraphic(card.getRoot());
                }
            }
        });
    }



}

