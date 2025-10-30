package seedu.address.model.property;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class PropertyNameContainsKeywordsPredicateTest {

    @Test
    public void test_singleKeyword_matches() {
        Property p = new Property(
                new Address("123 Example St"),
                new Price(1000),
                new PropertyName("Sunny Villa"));
        PropertyNameContainsKeywordsPredicate predicate =
                new PropertyNameContainsKeywordsPredicate(Arrays.asList("Sunny"));
        assertTrue(predicate.test(p));
    }

    @Test
    public void test_multipleKeywords_matchesAny() {
        Property p = new Property(
                new Address("123 Example St"),
                new Price(1000),
                new PropertyName("Sunny Villa"));
        PropertyNameContainsKeywordsPredicate predicate =
                new PropertyNameContainsKeywordsPredicate(Arrays.asList("Villa", "Other"));
        assertTrue(predicate.test(p));
    }

    @Test
    public void test_caseInsensitive_matching() {
        Property p = new Property(
                new Address("123 Example St"),
                new Price(1000),
                new PropertyName("Sunny Villa"));
        PropertyNameContainsKeywordsPredicate predicate =
                new PropertyNameContainsKeywordsPredicate(Arrays.asList("sunny", "villa"));
        assertTrue(predicate.test(p));
    }

    @Test
    public void test_noMatch_returnsFalse() {
        Property p = new Property(
                new Address("123 Example St"),
                new Price(1000),
                new PropertyName("Sunny Villa"));
        PropertyNameContainsKeywordsPredicate predicate =
                new PropertyNameContainsKeywordsPredicate(Arrays.asList("Moon"));
        assertFalse(predicate.test(p));
    }

    @Test
    public void equals_sameList_returnsTrue() {
        PropertyNameContainsKeywordsPredicate p1 =
                new PropertyNameContainsKeywordsPredicate(Arrays.asList("A", "B"));
        PropertyNameContainsKeywordsPredicate p2 =
                new PropertyNameContainsKeywordsPredicate(Arrays.asList("A", "B"));
        assertEquals(p1, p2);
    }

    @Test
    public void substringMatch_returnsTrue() {
        Property p = new Property(
                new Address("123 Example St"),
                new Price(1000),
                new PropertyName("Sunny Villa"));
        PropertyNameContainsKeywordsPredicate predicate =
                new PropertyNameContainsKeywordsPredicate(Arrays.asList("sun"));
        assertTrue(predicate.test(p));
    }
}
