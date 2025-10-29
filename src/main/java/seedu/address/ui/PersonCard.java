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

        List<String> ownedTexts = renderPropertyTexts(person.getOwnedProperties());
        for (int i = 0; i < ownedTexts.size(); i++) {
            Label chip = new Label(ownedTexts.get(i));
            chip.getStyleClass().add("owned-property");
            ownedProperties.getChildren().add(chip);
        }

        List<String> interestedTexts = renderPropertyTexts(person.getInterestedProperties());
        for (int i = 0; i < interestedTexts.size(); i++) {
            Label chip = new Label(interestedTexts.get(i));
            chip.getStyleClass().add("interested-property");
            interestedProperties.getChildren().add(chip);
        }
    }

    /**
     * Returns the display texts for properties joined by ", " where only non-last items carry the suffix.
     */
    static List<String> renderPropertyTexts(List<Property> properties) {
        List<String> out = new ArrayList<>();
        for (int i = 0; i < properties.size(); i++) {
            String name = properties.get(i).getPropertyName().toString();
            String suffix = (i < properties.size() - 1) ? ", " : "";
            out.add(name + suffix);
        }
        return out;
    }
}
