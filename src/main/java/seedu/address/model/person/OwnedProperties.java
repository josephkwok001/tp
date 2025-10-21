package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import seedu.address.model.property.Property;

/**
 * Immutable value object representing a person's owned properties.
 * The internal list is defensively copied and exposed as an unmodifiable view.
 */
public class OwnedProperties {
    private final List<Property> list;

    /**
     * Creates an instance containing the given properties.
     *
     * @param properties initial property list
     */
    public OwnedProperties(List<Property> properties) {
        requireNonNull(properties);
        this.list = new ArrayList<>(properties);
    }

    /**
     * Returns an unmodifiable view of the owned properties.
     *
     * @return unmodifiable list of properties
     */
    public List<Property> getUnmodifiableList() {
        return Collections.unmodifiableList(list);
    }

    /**
     * Returns true if there are no owned properties.
     *
     * @return whether the list is empty
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Returns a new {@code OwnedProperties} with the given property added if it is not already present
     * by {@link Property#isSameProperty(Property)}.
     *
     * @param property property to add
     * @return new instance reflecting the addition
     */
    public OwnedProperties withAdded(Property property) {
        requireNonNull(property);
        List<Property> copy = new ArrayList<>(this.list);
        if (copy.stream().noneMatch(p -> p.isSameProperty(property))) {
            copy.add(property);
        }
        return new OwnedProperties(copy);
    }

    /**
     * Returns an empty {@code OwnedProperties}.
     */
    public static OwnedProperties empty() {
        return new OwnedProperties(java.util.Collections.emptyList());
    }

    /**
     * Value-based equality on the underlying list.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof OwnedProperties)) {
            return false;
        }
        OwnedProperties o = (OwnedProperties) other;
        return list.equals(o.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }

    @Override
    public String toString() {
        return list.toString();
    }
}
