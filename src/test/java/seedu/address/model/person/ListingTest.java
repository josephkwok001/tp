package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ListingTest {

    @Test
    public void constructor_validListing_createsListing() {
        // A valid listing should be constructed without errors.
        String validListingValue = "Blk 302, Tampines St 32, #05-05";
        Listing listing = new Listing(validListingValue);
        assertEquals(validListingValue, listing.value);
    }

    @Test
    public void isValidListing() {
        assertFalse(Listing.isValidListing(null));

        // blank listings -> returns false
        assertFalse(Listing.isValidListing("")); // empty string
        assertFalse(Listing.isValidListing(" ")); // spaces only
        assertFalse(Listing.isValidListing("\t\n")); // whitespace characters

        // valid listings -> returns true
        assertTrue(Listing.isValidListing("A random listing"));
        assertTrue(Listing.isValidListing("123 Main St, #01-02"));
        assertTrue(Listing.isValidListing("  Valid listing with spaces  ")); // leading/trailing spaces are fine
    }

    @Test
    public void getListing_returnsCorrectValue() {
        String listingValue = "My Listing Details";
        Listing listing = new Listing(listingValue);
        assertEquals(listingValue, listing.getListing());
    }

    @Test
    public void toString_returnsCorrectValue() {
        String listingValue = "Another Listing";
        Listing listing = new Listing(listingValue);
        assertEquals(listingValue, listing.toString());
    }

    @Test
    public void equals() {
        Listing listingA = new Listing("Blk 123, Jurong West");
        Listing listingB = new Listing("Blk 123, Jurong West");
        Listing listingC = new Listing("Blk 456, Clementi");

        // Same object -> returns true
        assertTrue(listingA.equals(listingA));

        // Same values -> returns true
        assertTrue(listingA.equals(listingB));

        // Different values -> returns false
        assertFalse(listingA.equals(listingC));

        // Different types -> returns false
        assertFalse(listingA.equals(5));
        assertFalse(listingA.equals("Blk 123, Jurong West"));

        // null -> returns false
        assertFalse(listingA.equals(null));
    }

    @Test
    public void hashCode_consistency() {
        Listing listingA = new Listing("Consistent Listing");
        Listing listingB = new Listing("Consistent Listing");
        Listing listingC = new Listing("Different Listing");

        // Same value -> same hashcode
        assertEquals(listingA.hashCode(), listingB.hashCode());

        // Different value -> different hashcode
        assertNotEquals(listingA.hashCode(), listingC.hashCode());
    }
}
