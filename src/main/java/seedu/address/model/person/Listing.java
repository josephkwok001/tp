package seedu.address.model.person;

/**
 * Represents a Person's listing in the address book.
 * Guarantees: immutable; is always valid
 */
public class Listing {

    public final String value;

    /**
     * Constructs a {@code Listing}.
     *
     * @param listing A valid listing.
     */
    public Listing(String listing) {
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
        return true;
    }

    public String toString() {
        return value;
    }
}
