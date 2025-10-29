package seedu.address.ui.property;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.property.Property;
import seedu.address.ui.UiPart;

/**
 * UI component that displays a {@link Property} in the property list.
 */
public class PropertyCard extends UiPart<Region> {

    private static final String FXML = "PropertyListCard.fxml";

    public final Property property;

    @FXML
    private HBox cardPane;
    @FXML
    private Label propertyName;
    @FXML
    private Label id;
    @FXML
    private Label address;
    @FXML
    private Label price;

    /**
     * Creates a {@code PropertyCard} to render the given {@code Property} at the specified index.
     *
     * @param property the property to display
     * @param displayedIndex 1-based index shown on the card
     */
    public PropertyCard(Property property, int displayedIndex) {
        super(FXML);
        this.property = property;
        id.setText(displayedIndex + ". ");
        propertyName.setText(property.getPropertyName().toString());
        address.setText("Address: " + property.getAddress().toString());
        price.setText("Price: $" + property.getPrice().toString());
    }
}

