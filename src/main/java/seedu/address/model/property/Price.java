package seedu.address.model.property;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Property's price in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPrice(Integer)}
 */
public class Price {

    public static final String MESSAGE_CONSTRAINTS = "Price can only take positive numbers less than $"
            + Integer.MAX_VALUE + " and it should be a singular integer with no commas or decimal points.";

    public final Integer price;

    /**
     * Constructs a {@code Price}.
     *
     * @param price A valid price.
     */
    public Price(Integer price) {
        requireNonNull(price);
        checkArgument(isValidPrice(price), MESSAGE_CONSTRAINTS);
        this.price = price;
    }

    /**
     * Returns true if a given integer is a valid price.
     */
    public static boolean isValidPrice(Integer price) {
        return price > 0; // Price must be positive
    }

    public Integer getIntegerPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("%,d", price);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Price // instanceof handles nulls
                        && price.equals(((Price) other).price)); // state check
    }

    @Override
    public int hashCode() {
        return price.hashCode();
    }

}
