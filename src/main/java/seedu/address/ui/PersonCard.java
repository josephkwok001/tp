package seedu.address.ui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;
import seedu.address.model.property.Property;

/**
 * UI component that displays a {@link Person} in the person list.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;
    @FXML
    private FlowPane ownedProperties;
    @FXML
    private FlowPane interestedProperties;

    /**
     * Creates a {@code PersonCard} to render the given {@code Person} at the specified index.
     *
     * @param person the person to display
     * @param displayedIndex 1-based index shown on the card
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);

        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));

        ownedProperties.setHgap(0);
        interestedProperties.setHgap(0);

        for (String text : renderPropertyTexts(person.getOwnedProperties())) {
            Label chip = new Label(text);
            chip.getStyleClass().add("owned-property");
            ownedProperties.getChildren().add(chip);
        }
        for (String text : renderPropertyTexts(person.getInterestedProperties())) {
            Label chip = new Label(text);
            chip.getStyleClass().add("interested-property");
            interestedProperties.getChildren().add(chip);
        }
    }

    /**
     * Returns the display texts for properties joined by ", " where only non-last items carry the suffix.
     * Extracted as pure logic to enable headless unit testing and improve coverage.
     */
    static List<String> renderPropertyTexts(List<Property> properties) {
        List<String> out = new ArrayList<>();
        for (int i = 0; i < properties.size(); i++) {
            Property p = properties.get(i);
            String suffix = (i < properties.size() - 1) ? ", " : "";
            out.add(p.getPropertyName().toString() + suffix);
        }
        return out;
    }
}
