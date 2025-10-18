package seedu.address.storage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Listing;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

/**
 * Immutable report produced when loading the address book from storage.
 * Contains a snapshot of valid data and a list of invalid entries.
 */
public final class LoadReport {

    private final ModelData modelData;
    private final List<InvalidPersonEntry> invalids;

    /**
     * Constructs a {@code LoadReport}.
     *
     * @param modelData snapshot containing valid persons
     * @param invalids list of invalid person entries
     */
    public LoadReport(ModelData modelData, List<InvalidPersonEntry> invalids) {
        this.modelData = Objects.requireNonNull(modelData);
        this.invalids = Collections.unmodifiableList(Objects.requireNonNull(invalids));
    }

    public ModelData getModelData() {
        return modelData;
    }

    public List<InvalidPersonEntry> getInvalids() {
        return invalids;
    }

    /**
     * Generates a LoadReport from a full AddressBook model.
     * Valid persons are kept in the modelData snapshot; invalid ones are reported.
     */
    public static LoadReport fromAddressBook(AddressBook source) {
        List<Person> persons = new ArrayList<>(source.getPersonList());
        AddressBook validBook = new AddressBook();
        List<InvalidPersonEntry> invalids = new ArrayList<>();

        for (int i = 0; i < persons.size(); i++) {
            Person p = persons.get(i);
            if (p.isFullyValid()) {
                validBook.addPerson(p);
            } else {
                Set<String> bad = collectInvalidFields(p);
                String reason = "Invalid field(s): " + String.join(", ", bad);
                invalids.add(new InvalidPersonEntry(
                        i,
                        reason,
                        p.getName().toString(),
                        p.getPhone().toString(),
                        p.getEmail().toString(),
                        p.getAddress().toString(),
                        p.getListing().toString(),
                        bad
                ));
            }
        }

        return new LoadReport(new ModelData(validBook), invalids);
    }

    /**
     * Collects the names of invalid fields for the given person using field validators.
     *
     * @param p person to check
     * @return set of invalid field keys, in insertion order
     */
    private static Set<String> collectInvalidFields(Person p) {
        Set<String> bad = new LinkedHashSet<>();
        if (!Name.isValidName(p.getName().fullName)) {
            bad.add("name");
        }
        if (!Phone.isValidPhone(p.getPhone().value)) {
            bad.add("phone");
        }
        if (!Email.isValidEmail(p.getEmail().value)) {
            bad.add("email");
        }
        if (!Address.isValidAddress(p.getAddress().value)) {
            bad.add("address");
        }
        if (!Listing.isValidListing(p.getListing().value)) {
            bad.add("listing");
        }
        return bad;
    }

    /**
     * Model snapshot of valid persons.
     */
    public static final class ModelData {
        private final AddressBook addressBook;

        public ModelData(AddressBook addressBook) {
            this.addressBook = Objects.requireNonNull(addressBook);
        }

        public AddressBook getAddressBook() {
            return addressBook;
        }
    }

    /**
     * Describes one invalid person entry.
     */
    public static final class InvalidPersonEntry {
        private final int index;
        private final String reason;
        private final String name;
        private final String phone;
        private final String email;
        private final String address;
        private final String listing;
        private final Set<String> invalidFields;

        /**
         * Constructs an {@code InvalidPersonEntry}.
         *
         * @param index zero-based index of the person in the original source
         * @param reason human-readable summary of invalidity
         * @param name original name string
         * @param phone original phone string
         * @param email original email string
         * @param address original address string
         * @param listing original listing string
         * @param invalidFields set of invalid field keys
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
