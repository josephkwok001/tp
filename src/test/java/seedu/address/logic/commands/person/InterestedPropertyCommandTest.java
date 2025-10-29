package seedu.address.logic.commands.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
     * After a successful link, interested list increments by exactly one and contains the property.
     */
    @Test
    public void execute_success_incrementsListOnce() {
        Property property = model.getFilteredPropertyList().get(0);
        Index personIndex = Index.fromZeroBased(0);
        Person before = model.getFilteredPersonList().get(personIndex.getZeroBased());
        int beforeSize = before.getInterestedProperties().size();

        String expectedMessage = String.format(
                InterestedPropertyCommand.MESSAGE_SUCCESS,
                before.getName().fullName,
                property.getPropertyName().toString()
        );

        assertCommandSuccess(
                new InterestedPropertyCommand(property.getPropertyName(), personIndex),
                model,
                expectedMessage,
                model
        );

        Person after = model.getFilteredPersonList().get(personIndex.getZeroBased());
        assertEquals(beforeSize + 1, after.getInterestedProperties().size());
        boolean contains = after.getInterestedProperties()
                .stream().anyMatch(property::isSameProperty);
        assertTrue(contains);
    }

    /**
     * Fails when property name does not exist in the property list.
     * Model must remain unchanged.
     */
    @Test
    public void execute_invalidProperty_throwsCommandException() {
        Index personIndex = Index.fromZeroBased(1);
        String missingName = "NonExistentProperty";

        Person before = model.getFilteredPersonList().get(personIndex.getZeroBased());
        int sizeBefore = before.getInterestedProperties().size();

        assertCommandFailure(
                new InterestedPropertyCommand(new seedu.address.model.property.PropertyName(missingName), personIndex),
                model,
                String.format(InterestedPropertyCommand.MESSAGE_PROPERTY_NOT_FOUND, missingName)
        );

        Person after = model.getFilteredPersonList().get(personIndex.getZeroBased());
        assertEquals(sizeBefore, after.getInterestedProperties().size());
    }

    /**
     * Fails when person index is out of bounds.
     * Model must remain unchanged.
     */
    @Test
    public void execute_invalidPerson_throwsCommandException() {
        Property property = model.getFilteredPropertyList().get(2);
        Index validIndexForSnapshot = Index.fromZeroBased(0);
        Person before = model.getFilteredPersonList().get(validIndexForSnapshot.getZeroBased());
        int sizeBefore = before.getInterestedProperties().size();

        Index invalidPersonIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        assertCommandFailure(
                new InterestedPropertyCommand(property.getPropertyName(), invalidPersonIndex),
                model,
                InterestedPropertyCommand.MESSAGE_PERSON_NOT_FOUND
        );

        Person after = model.getFilteredPersonList().get(validIndexForSnapshot.getZeroBased());
        assertEquals(sizeBefore, after.getInterestedProperties().size());
    }

    /**
     * Fails with duplicate when the person is already interested in the property.
     * Model must remain unchanged on failure.
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
                "No (person, property) pair available that is neither owned nor already interested."
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

        Person before = model.getFilteredPersonList().get(personIndex.getZeroBased());
        int sizeBefore = before.getInterestedProperties().size();

        String expectedDup = String.format(
                InterestedPropertyCommand.MESSAGE_DUPLICATE_LINK,
                property.getPropertyName().fullName
        );
        assertCommandFailure(
                new InterestedPropertyCommand(property.getPropertyName(), personIndex),
                model,
                expectedDup
        );

        Person after = model.getFilteredPersonList().get(personIndex.getZeroBased());
        assertEquals(sizeBefore, after.getInterestedProperties().size());
    }

    /**
     * Fails with ownership conflict when the person already owns the property.
     * We first pick a (person, property) pair that is neither owned nor interested,
     * set it as owned successfully, then linking as interested must fail.
     */
    @Test
    public void execute_ownershipConflict_throwsCommandException() {
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
                "No (person, property) pair available that is neither owned nor already interested."
        );

        Index personIndex = Index.fromZeroBased(pIdx);
        Property property = model.getFilteredPropertyList().get(prIdx);
        Person person = model.getFilteredPersonList().get(personIndex.getZeroBased());

        String setopSuccess = String.format(
                SetOwnedPropertyCommand.MESSAGE_SUCCESS,
                person.getName().fullName,
                property.getPropertyName().toString()
        );
        assertCommandSuccess(
                new SetOwnedPropertyCommand(personIndex, property.getPropertyName().toString()),
                model,
                setopSuccess,
                model
        );

        String expectedConflict = String.format(
                InterestedPropertyCommand.MESSAGE_OWNERSHIP_CONFLICT,
                property.getPropertyName().fullName
        );
        assertCommandFailure(
                new InterestedPropertyCommand(property.getPropertyName(), personIndex),
                model,
                expectedConflict
        );
    }
}
