package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditPropertyCommand.EditPropertyDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.property.Address;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyName;

/**
 * Integration-style tests for {@link EditPropertyCommand} that exercise interaction
 * with the real Model (ModelManager + AddressBook).
 */
public class EditPropertyCommandIntegrationTest {

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        EditPropertyDescriptor desc = new EditPropertyDescriptor();
        desc.setPropertyName(new PropertyName("N"));
        // null index
        try {
            new EditPropertyCommand(null, desc);
            throw new AssertionError("Expected NullPointerException not thrown");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void constructor_nullDescriptor_throwsNullPointerException() {
        Index idx = Index.fromOneBased(1);
        try {
            new EditPropertyCommand(idx, null);
            throw new AssertionError("Expected NullPointerException not thrown");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void edit_noFieldsProvided_descriptorIsAnyFieldEditedFalse() {
        EditPropertyDescriptor desc = new EditPropertyDescriptor();
        // no fields set -> isAnyFieldEdited should be false
        assertEquals(false, desc.isAnyFieldEdited());
    }

    @Test
    public void edit_someFieldsProvided_descriptorIsAnyFieldEditedTrue() {
        EditPropertyDescriptor desc = new EditPropertyDescriptor();
        desc.setPropertyName(new PropertyName("New"));
        assertEquals(true, desc.isAnyFieldEdited());
    }

    @Test
    public void execute_validIndex_success() throws Exception {
        AddressBook ab = new AddressBook();
        Property original = new Property(new Address("A"), new Price(1000), new PropertyName("P"));
        ab.addProperty(original);
        ModelManager model = new ModelManager(ab, new UserPrefs());

        EditPropertyDescriptor desc = new EditPropertyDescriptor();
        desc.setPropertyName(new PropertyName("Q")); // only change name
        EditPropertyCommand cmd = new EditPropertyCommand(Index.fromOneBased(1), desc);

        CommandResult result = cmd.execute(model);

        Property edited = model.getFilteredPropertyList().get(0);
        // check message
        String expectedMessage = String.format(EditPropertyCommand.MESSAGE_EDIT_PROPERTY_SUCCESS,
                Messages.formatProperty(edited));
        assertEquals(expectedMessage, result.getFeedbackToUser());
        // check fields updated
        assertEquals("Q", edited.getPropertyName().toString());
        assertEquals(1000, edited.getPrice().getIntegerPrice());
        assertEquals("A", edited.getAddress().toString());
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        AddressBook ab = new AddressBook();
        // empty address book
        ModelManager model = new ModelManager(ab, new UserPrefs());

        EditPropertyDescriptor desc = new EditPropertyDescriptor();
        desc.setPropertyName(new PropertyName("Q"));
        EditPropertyCommand cmd = new EditPropertyCommand(Index.fromOneBased(1), desc);

        assertThrows(CommandException.class, () -> cmd.execute(model));
    }

    @Test
    public void execute_duplicateProperty_throwsCommandException() throws Exception {
        AddressBook ab = new AddressBook();
        // existing properties: propA (will be target) and propB (duplicate target if edited name matches propB)
        Property propA = new Property(new Address("A1"), new Price(100), new PropertyName("NameA"));
        Property propB = new Property(new Address("A2"), new Price(200), new PropertyName("NameB"));
        ab.addProperty(propA);
        ab.addProperty(propB);
        ModelManager model = new ModelManager(ab, new UserPrefs());

        // attempt to edit propA to have same identity as propB (same name)
        EditPropertyDescriptor desc = new EditPropertyDescriptor();
        desc.setPropertyName(new PropertyName("NameB")); // now edited property will "match" propB by identity
        EditPropertyCommand cmd = new EditPropertyCommand(Index.fromOneBased(1), desc);

        CommandException thrown = null;
        try {
            cmd.execute(model);
        } catch (CommandException e) {
            thrown = e;
        }
        // should throw duplicate property exception
        assertEquals(EditPropertyCommand.MESSAGE_DUPLICATE_PROPERTY, thrown.getMessage());
    }
}
