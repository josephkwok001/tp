package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's listing in the address book.
 * Guarantees: immutable; is always valid
 */
public class Listing {

    public static final String MESSAGE_CONSTRAINTS = "Listings can take any values, and it should not be blank";

    public final String value;

    /**
     * Constructs a {@code Listing}.
     *
     * @param listing A valid, non-blank listing description.
     * @throws NullPointerException if {@code listing} is null.
     * @throws IllegalArgumentException if {@code listing} is blank.
     */
    public Listing(String listing) {
        requireNonNull(listing); // Checks for null first, a common practice
        checkArgument(isValidListing(listing), MESSAGE_CONSTRAINTS);
        this.value = listing;
    }

    /**
     * Returns the listing value.
     */
    public String getListing() {
        return value;
    }

    /**
     * Returns true if a given string is a valid listing.
     */
    public static boolean isValidListing(String test) {
        return test != null && !test.isBlank();
    }

    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Listing)) {
            return false;
        }

        Listing otherListing = (Listing) other;
        return value.equals(otherListing.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
