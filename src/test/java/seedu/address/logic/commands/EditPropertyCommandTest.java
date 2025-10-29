package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.property.Address;
import seedu.address.model.property.Price;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyName;

public class EditPropertyCommandTest {

    @Test
    public void equals_sameDescriptorAndIndex_returnsTrue() {
        Index idx = Index.fromOneBased(1);
        EditPropertyCommand.EditPropertyDescriptor d1 = new EditPropertyCommand.EditPropertyDescriptor();
        d1.setPropertyName(new PropertyName("N"));
        EditPropertyCommand c1 = new EditPropertyCommand(idx, d1);
        EditPropertyCommand.EditPropertyDescriptor d2 = new EditPropertyCommand.EditPropertyDescriptor(d1);
        EditPropertyCommand c2 = new EditPropertyCommand(idx, d2);
        assertEquals(c1, c2);
    }

    @Test
    public void equals_differentDescriptor_returnsFalse() {
        Index idx = Index.fromOneBased(1);
        EditPropertyCommand.EditPropertyDescriptor d1 = new EditPropertyCommand.EditPropertyDescriptor();
        d1.setPropertyName(new PropertyName("N1"));
        EditPropertyCommand c1 = new EditPropertyCommand(idx, d1);

        EditPropertyCommand.EditPropertyDescriptor d2 = new EditPropertyCommand.EditPropertyDescriptor();
        d2.setPropertyName(new PropertyName("N2"));
        EditPropertyCommand c2 = new EditPropertyCommand(idx, d2);

        assertNotEquals(c1, c2);
    }

    @Test
    public void createEditedProperty_matchesExpected() {
        Property original = new Property(new Address("A"), new Price(1000), new PropertyName("P"));
        EditPropertyCommand.EditPropertyDescriptor descriptor = new EditPropertyCommand.EditPropertyDescriptor();
        descriptor.setPropertyName(new PropertyName("Q"));
        Property edited = invokeCreateEditedProperty(original, descriptor);
        // basic field checks
        assertEquals("Q", edited.getPropertyName().toString());
        assertEquals(1000, edited.getPrice().getIntegerPrice());
        assertEquals("A", edited.getAddress().toString());
    }

    // helper to call private static method via reflection
    private Property invokeCreateEditedProperty(
        Property original, EditPropertyCommand.EditPropertyDescriptor descriptor) {
        try {
            var method = EditPropertyCommand.class.getDeclaredMethod("createEditedProperty", Property.class,
                    EditPropertyCommand.EditPropertyDescriptor.class);
            method.setAccessible(true);
            return (Property) method.invoke(null, original, descriptor);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void toString_allFields_matchesFormat() {
        Index idx = Index.fromOneBased(1);
        EditPropertyCommand.EditPropertyDescriptor descriptor = new EditPropertyCommand.EditPropertyDescriptor();
        descriptor.setPropertyName(new PropertyName("TestName"));
        descriptor.setAddress(new Address("Test Address"));
        descriptor.setPrice(new Price(500000));

        EditPropertyCommand command = new EditPropertyCommand(idx, descriptor);

        // Full command toString format
        String expected = "seedu.address.logic.commands.EditPropertyCommand"
                + "{index=seedu.address.commons.core.index.Index{zeroBasedIndex=0}, "
                + "editPropertyDescriptor=seedu.address.logic.commands.EditPropertyCommand"
                + ".EditPropertyDescriptor{name=TestName, address=Test Address, price=500000}}";

        assertEquals(expected, command.toString());

        // Descriptor toString format
        String expectedDescriptor = "seedu.address.logic.commands.EditPropertyCommand"
                + ".EditPropertyDescriptor{name=TestName, address=Test Address, price=500000}";

        assertEquals(expectedDescriptor, descriptor.toString());
    }

    @Test
    public void editPropertyDescriptor_toString_returnsCorrectFormat() {
        EditPropertyCommand.EditPropertyDescriptor descriptor = new EditPropertyCommand.EditPropertyDescriptor();
        descriptor.setPropertyName(new PropertyName("TestName"));
        descriptor.setAddress(new Address("Test Address"));
        descriptor.setPrice(new Price(500000));

        String actual = descriptor.toString();
        String expected = "seedu.address.logic.commands.EditPropertyCommand"
                + ".EditPropertyDescriptor{name=TestName, address=Test Address, price=500000}";

        assertEquals(expected, actual);
    }

}
