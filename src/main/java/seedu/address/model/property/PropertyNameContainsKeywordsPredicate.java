package seedu.address.model.property;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Property}'s {@code Name} matches any of the keywords given.
 */
public class PropertyNameContainsKeywordsPredicate implements Predicate<Property> {
    private final List<String> keywords;

    public PropertyNameContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Property property) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(
                    property.getPropertyName().fullName, keyword)
                    || property.getPropertyName().fullName.toLowerCase().contains(keyword.toLowerCase()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PropertyNameContainsKeywordsPredicate)) {
            return false;
        }

        PropertyNameContainsKeywordsPredicate otherPropertyNameContainsKeywordsPredicate =
            (PropertyNameContainsKeywordsPredicate) other;
        return keywords.equals(otherPropertyNameContainsKeywordsPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
