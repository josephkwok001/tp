package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.model.property.PropertyNameContainsKeywordsPredicate;

public class FindPropertyCommandTest {

    @Test
    public void equals_samePredicate_returnsTrue() {
        FindPropertyCommand cmd1 = new FindPropertyCommand(
                new PropertyNameContainsKeywordsPredicate(Arrays.asList("A")));
        FindPropertyCommand cmd2 = new FindPropertyCommand(
                new PropertyNameContainsKeywordsPredicate(Arrays.asList("A")));
        assertTrue(cmd1.equals(cmd2));
        assertEquals(cmd1, cmd2);
    }

    @Test
    public void equals_differentPredicate_returnsFalse() {
        FindPropertyCommand cmd1 = new FindPropertyCommand(
                new PropertyNameContainsKeywordsPredicate(Arrays.asList("A")));
        FindPropertyCommand cmd2 = new FindPropertyCommand(
                new PropertyNameContainsKeywordsPredicate(Arrays.asList("B")));
        assertTrue(!cmd1.equals(cmd2));
    }
}
