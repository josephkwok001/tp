package seedu.address.model.property;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PropertyBuilder;

public class PropertyNameContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        PropertyNameContainsKeywordsPredicate firstPredicate = 
            new PropertyNameContainsKeywordsPredicate(firstPredicateKeywordList);
        PropertyNameContainsKeywordsPredicate secondPredicate = 
            new PropertyNameContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        PropertyNameContainsKeywordsPredicate firstPredicateCopy = 
            new PropertyNameContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different keywords -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_propertyNameContainsKeywords_returnsTrue() {
        PropertyNameContainsKeywordsPredicate predicate = 
            new PropertyNameContainsKeywordsPredicate(Collections.singletonList("Pinnacle"));
        assertTrue(predicate.test(new PropertyBuilder().withName("The Pinnacle at Duxton").build()));

        predicate = new PropertyNameContainsKeywordsPredicate(Arrays.asList("Pinnacle", "Duxton"));
        assertTrue(predicate.test(new PropertyBuilder().withName("The Pinnacle at Duxton").build()));

        predicate = new PropertyNameContainsKeywordsPredicate(Arrays.asList("SkyVille", "Dawson"));
        assertTrue(predicate.test(new PropertyBuilder().withName("SkyVille at Dawson").build()));
    }

    @Test
    public void test_propertyNameDoesNotContainKeywords_returnsFalse() {
        PropertyNameContainsKeywordsPredicate predicate = 
            new PropertyNameContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PropertyBuilder().withName("The Pinnacle at Duxton").build()));

        predicate = new PropertyNameContainsKeywordsPredicate(Collections.singletonList("NotInName"));
        assertFalse(predicate.test(new PropertyBuilder().withName("The Pinnacle at Duxton").build()));

        predicate = new PropertyNameContainsKeywordsPredicate(Arrays.asList("123", "Jurong"));
        assertFalse(predicate.test(new PropertyBuilder()
                .withName("Sunshine Gardens")
                .withAddress("123, Jurong West Ave 6, #08-111")
                .withPrice(8000000)
                .build()));
    }

    @Test
    public void test_partialWordMatch_returnsFalse() {
        PropertyNameContainsKeywordsPredicate predicate = 
            new PropertyNameContainsKeywordsPredicate(Collections.singletonList("Pinnac"));
        assertFalse(predicate.test(new PropertyBuilder().withName("The Pinnacle at Duxton").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        PropertyNameContainsKeywordsPredicate predicate = 
            new PropertyNameContainsKeywordsPredicate(keywords);

        String expected = PropertyNameContainsKeywordsPredicate.class.getCanonicalName() 
            + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}

