package seedu.address.storage;

import seedu.address.model.ReadOnlyAddressBook;

/**
 * A lightweight read-only wrapper used by storage load reports to
 * pass model data without exposing a concrete mutable type.
 */
public interface ReadOnlyAddressBookModelData {
    /**
     * Returns the in-memory {@link ReadOnlyAddressBook} view
     * constructed from persisted data.
     */
    ReadOnlyAddressBook toModel();
}
