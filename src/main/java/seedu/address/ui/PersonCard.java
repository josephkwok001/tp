package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

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

        for (int i = 0; i < person.getOwnedProperties().size(); i++) {
            var p = person.getOwnedProperties().get(i);
            String suffix = (i < person.getOwnedProperties().size() - 1) ? ", " : "";
            Label chip = new Label(p.getPropertyName().toString() + suffix);
            chip.getStyleClass().add("owned-property");
            ownedProperties.getChildren().add(chip);
        }

        for (int i = 0; i < person.getInterestedProperties().size(); i++) {
            var p = person.getInterestedProperties().get(i);
            String suffix = (i < person.getInterestedProperties().size() - 1) ? ", " : "";
            Label chip = new Label(p.getPropertyName().toString() + suffix);
            chip.getStyleClass().add("interested-property");
            interestedProperties.getChildren().add(chip);
        }
    }
}
