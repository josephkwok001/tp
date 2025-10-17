package seedu.address.storage;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Immutable report produced when loading the address book from storage.
 * <p>
 * The report contains:
 * <ul>
 *   <li>a snapshot of the successfully parsed model data, and</li>
 *   <li>a list of invalid (quarantined) entries with human-readable reasons.</li>
 * </ul>
 */
public final class LoadReport {

    private final ModelData modelData;
    private final List<InvalidPersonEntry> invalids;

    /**
     * Constructs a {@code LoadReport}.
     *
     * @param modelData successfully parsed model snapshot (must not be null)
     * @param invalids  list of invalid entries (must not be null; may be empty)
     */
    public LoadReport(ModelData modelData, List<InvalidPersonEntry> invalids) {
        this.modelData = Objects.requireNonNull(modelData);
        this.invalids = Collections.unmodifiableList(Objects.requireNonNull(invalids));
    }

    /**
     * Returns the successfully parsed model snapshot.
     *
     * @return the model data
     */
    public ModelData getModelData() {
        return modelData;
    }

    /**
     * Returns the list of invalid entries. Never null; may be empty.
     *
     * @return the invalid entries
     */
    public List<InvalidPersonEntry> getInvalids() {
        return invalids;
    }

    /**
     * Snapshot of the model constructed from valid entries.
     */
    public static final class ModelData {
        private final seedu.address.model.AddressBook addressBook;

        /**
         * Constructs {@code ModelData}.
         *
         * @param addressBook the parsed address book (must not be null)
         */
        public ModelData(seedu.address.model.AddressBook addressBook) {
            this.addressBook = Objects.requireNonNull(addressBook);
        }

        /**
         * Returns the parsed address book.
         *
         * @return the address book
         */
        public seedu.address.model.AddressBook getAddressBook() {
            return addressBook;
        }
    }

    /**
     * Describes a single invalid person entry that failed validation during load.
     */
    public static final class InvalidPersonEntry {
        private final int index;
        private final String reason;

        // NEW: original values as seen in JSON
        private final String name;
        private final String phone;
        private final String email;
        private final String address;
        private final String listing;

        // NEW: which fields are invalid (e.g. "name", "phone")
        private final Set<String> invalidFields;

        /**
         * Returns true if this entry has an invalid field with the given name.
         */
        public InvalidPersonEntry(int index,
                                  String reason,
                                  String name,
                                  String phone,
                                  String email,
                                  String address,
                                  String listing,
                                  Set<String> invalidFields) {
            this.index = index;
            this.reason = reason;
            this.name = name;
            this.phone = phone;
            this.email = email;
            this.address = address;
            this.listing = listing;
            this.invalidFields = invalidFields;
        }

        public int index() {
            return index;
        }
        public String reason() {
            return reason;
        }
        public String name() {
            return name;
        }
        public String phone() {
            return phone;
        }
        public String email() {
            return email;
        }
        public String address() {
            return address;
        }
        public String listing() {
            return listing;
        }
        public Set<String> invalidFields() {
            return invalidFields;
        }

        public boolean fieldInvalid(String key) {
            return invalidFields.contains(key);
        }
    }
}
