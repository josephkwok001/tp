package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TestUtil.getTypicalCombinedAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.property.Property;
import seedu.address.testutil.PropertyBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class InterestedPropertyCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalCombinedAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_linkProperty_success() {
        Property validProperty = model.getFilteredPropertyList().get(1);
        Index index = Index.fromZeroBased(1);

        assertCommandSuccess(new InterestedPropertyCommand(validProperty.getPropertyName(), index), model,
                String.format(InterestedPropertyCommand.MESSAGE_SUCCESS, Messages.formatProperty(validProperty),
                        Messages.formatPerson(model.getFilteredPersonList().get(index.getZeroBased()))),
                model);
    }

    @Test
    public void execute_invalidProperty_throwsCommandException() {
        Property invalidProperty = new PropertyBuilder().withName("NonExistentProperty")
                .withAddress("123 Non-existant Road")
                .withPrice(100).build();
        Index index = Index.fromZeroBased(1);

        assertCommandFailure(new InterestedPropertyCommand(invalidProperty.getPropertyName(), index), model,
                String.format(InterestedPropertyCommand.MESSAGE_PROPERTY_NOT_FOUND));
    }

    @Test
    public void execute_invalidPerson_throwsCommandException() {
        Property validProperty = model.getFilteredPropertyList().get(2);
        Index invalidIndex = Index.fromOneBased(model.getFilteredPropertyList().size() + 1);

        assertCommandFailure(new InterestedPropertyCommand(validProperty.getPropertyName(), invalidIndex), model,
                String.format(InterestedPropertyCommand.MESSAGE_PERSON_NOT_FOUND));
    }

    @Test
    public void execute_duplicateProperty_throwsCommandException() {
        Property validProperty = model.getFilteredPropertyList().get(1);
        Index index = Index.fromZeroBased(1);

        assertCommandFailure(new InterestedPropertyCommand(validProperty.getPropertyName(), index), model,
                InterestedPropertyCommand.MESSAGE_DUPLICATE_LINK);
    }
}
