package seedu.address.logic.commands.person;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TestUtil.getTypicalCombinedAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.property.Property;

/**
 * Integration tests for {@link InterestedPropertyCommand}.
 */
public class InterestedPropertyCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalCombinedAddressBook(), new UserPrefs());
    }

    /**
     * Links a property as interested to a valid person and verifies unified success format:
     * "Set interested property for {person}: {property}".
     */
    @Test
    public void execute_linkProperty_success() {
        Property property = model.getFilteredPropertyList().get(1);
        Index personIndex = Index.fromZeroBased(1);
        Person person = model.getFilteredPersonList().get(personIndex.getZeroBased());

        String expectedMessage = String.format(
                InterestedPropertyCommand.MESSAGE_SUCCESS,
                person.getName().fullName,
                property.getPropertyName().toString()
        );

        assertCommandSuccess(
                new InterestedPropertyCommand(property.getPropertyName(), personIndex),
                model,
                expectedMessage,
                model
        );
    }

    /**
     * Fails when property name does not exist in the property list.
     */
    @Test
    public void execute_invalidProperty_throwsCommandException() {
        Index personIndex = Index.fromZeroBased(1);
        String missingName = "NonExistentProperty";

        assertCommandFailure(
                new InterestedPropertyCommand(new seedu.address.model.property.PropertyName(missingName), personIndex),
                model,
                String.format(InterestedPropertyCommand.MESSAGE_PROPERTY_NOT_FOUND, missingName)
        );
    }

    /**
     * Fails when person index is out of bounds.
     */
    @Test
    public void execute_invalidPerson_throwsCommandException() {
        Property property = model.getFilteredPropertyList().get(2);
        Index invalidPersonIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        assertCommandFailure(
                new InterestedPropertyCommand(property.getPropertyName(), invalidPersonIndex),
                model,
                InterestedPropertyCommand.MESSAGE_PERSON_NOT_FOUND
        );
    }

    /**
     * Fails with duplicate when the person is already interested in the property.
     */
    @Test
    public void execute_duplicateProperty_throwsCommandException() {
        int pIdx = -1;
        int prIdx = -1;
        outer:
        for (int i = 0; i < model.getFilteredPersonList().size(); i++) {
            Person p = model.getFilteredPersonList().get(i);
            for (int j = 0; j < model.getFilteredPropertyList().size(); j++) {
                Property pr = model.getFilteredPropertyList().get(j);
                boolean owns = p.getOwnedProperties().stream().anyMatch(pr::isSameProperty);
                boolean interested = p.getInterestedProperties().stream().anyMatch(pr::isSameProperty);
                if (!owns && !interested) {
                    pIdx = i;
                    prIdx = j;
                    break outer;
                }
            }
        }
        org.junit.jupiter.api.Assertions.assertTrue(
                pIdx >= 0 && prIdx >= 0,
                "No (person, property) pair available that is neither owned nor already interested. "
                        + "persons=" + model.getFilteredPersonList().size()
                        + ", properties=" + model.getFilteredPropertyList().size()
                        + ". Fix typical data or loosen constraints."
        );

        Index personIndex = Index.fromZeroBased(pIdx);
        Property property = model.getFilteredPropertyList().get(prIdx);

        String successMsg = String.format(
                InterestedPropertyCommand.MESSAGE_SUCCESS,
                model.getFilteredPersonList().get(personIndex.getZeroBased()).getName().fullName,
                property.getPropertyName().toString()
        );
        assertCommandSuccess(
                new InterestedPropertyCommand(property.getPropertyName(), personIndex),
                model,
                successMsg,
                model
        );

        String expectedDup = String.format(
                InterestedPropertyCommand.MESSAGE_DUPLICATE_LINK,
                property.getPropertyName().fullName
        );
        assertCommandFailure(
                new InterestedPropertyCommand(property.getPropertyName(), personIndex),
                model,
                expectedDup
        );
    }

    /**
     * Fails with ownership conflict when the person already owns the property.
     */
    @Test
    public void execute_ownershipConflict_throwsCommandException() {
        Property property = model.getFilteredPropertyList().get(0);
        Index personIndex = Index.fromZeroBased(0);

        assertCommandSuccess(
                new SetOwnedPropertyCommand(personIndex, property.getPropertyName().toString()),
                model,
                String.format(SetOwnedPropertyCommand.MESSAGE_SUCCESS,
                        model.getFilteredPersonList().get(personIndex.getZeroBased()).getName().fullName,
                        property.getPropertyName().toString()),
                model
        );

        assertCommandFailure(
                new InterestedPropertyCommand(property.getPropertyName(), personIndex),
                model,
                String.format(InterestedPropertyCommand.MESSAGE_OWNERSHIP_CONFLICT, property.getPropertyName().fullName)
        );
    }
}
