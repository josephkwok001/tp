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
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

/**
 * Immutable report produced when loading the address book from storage.
 * Contains a snapshot of valid data and lists of invalid entries.
 */
public final class LoadReport {

    private final ModelData modelData;
    private final List<InvalidPersonEntry> invalids;
    private final List<InvalidPropertyEntry> invalidProperties;

    /**
     * Constructs a {@code LoadReport} with both person and property invalid entries.
     *
     * @param modelData snapshot containing valid data
     * @param invalids list of invalid person entries
     * @param invalidProperties list of invalid property entries
     */
    public LoadReport(ModelData modelData,
                      List<InvalidPersonEntry> invalids,
                      List<InvalidPropertyEntry> invalidProperties) {
        this.modelData = Objects.requireNonNull(modelData);
        this.invalids = Collections.unmodifiableList(Objects.requireNonNull(invalids));
        this.invalidProperties = Collections.unmodifiableList(Objects.requireNonNull(invalidProperties));
    }

    /**
     * Constructs a {@code LoadReport} with only person invalid entries.
     * Property invalid entries are set to an empty list.
     *
     * @param modelData snapshot containing valid data
     * @param invalids list of invalid person entries
     */
    public LoadReport(ModelData modelData, List<InvalidPersonEntry> invalids) {
        this(modelData, invalids, java.util.Collections.emptyList());
    }

    /**
     * Returns the loaded model snapshot.
     *
     * @return model data
     */
    public ModelData getModelData() {
        return modelData;
    }

    /**
     * Returns the list of invalid person entries.
     *
     * @return invalid person entries
     */
    public List<InvalidPersonEntry> getInvalids() {
        return invalids;
    }

    /**
     * Returns the list of invalid property entries.
     *
     * @return invalid property entries
     */
    public List<InvalidPropertyEntry> getInvalidPropertyEntries() {
        return invalidProperties;
    }

    /**
     * Generates a LoadReport from a full AddressBook model.
     * Valid persons are kept in the modelData snapshot; invalid ones are reported.
     *
     * @param source address book model
     * @return load report
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
                        i, reason,
                        p.getName().toString(),
                        p.getPhone().toString(),
                        p.getEmail().toString(),
                        p.getAddress().toString(),
                        bad
                ));
            }
        }

        return new LoadReport(new ModelData(validBook), invalids, java.util.Collections.emptyList());
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
        return bad;
    }

    /**
     * Model snapshot of valid persons and properties.
     */
    public static final class ModelData {
        private final AddressBook addressBook;

        /**
         * Constructs a model data snapshot.
         *
         * @param addressBook address book model
         */
        public ModelData(AddressBook addressBook) {
            this.addressBook = Objects.requireNonNull(addressBook);
        }

        /**
         * Returns the address book snapshot.
         *
         * @return address book
         */
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
        private final Set<String> invalidFields;

        /**
         * Constructs an {@code InvalidPersonEntry}.
         *
         * @param index zero-based index in the persons array
         * @param reason human-readable summary of invalidity
         * @param name original name string
         * @param phone original phone string
         * @param email original email string
         * @param address original address string
         * @param invalidFields set of invalid field keys
         */
        public InvalidPersonEntry(int index,
                                  String reason,
                                  String name,
                                  String phone,
                                  String email,
                                  String address,
                                  Set<String> invalidFields) {
            this.index = index;
            this.reason = reason;
            this.name = name;
            this.phone = phone;
            this.email = email;
            this.address = address;
            this.invalidFields = invalidFields;
        }

        /**
         * Returns the original JSON index.
         *
         * @return index
         */
        public int index() {
            return index;
        }

        /**
         * Returns the reason text.
         *
         * @return reason
         */
        public String reason() {
            return reason;
        }

        /**
         * Returns the original name string.
         *
         * @return name
         */
        public String name() {
            return name;
        }

        /**
         * Returns the original phone string.
         *
         * @return phone
         */
        public String phone() {
            return phone;
        }

        /**
         * Returns the original email string.
         *
         * @return email
         */
        public String email() {
            return email;
        }

        /**
         * Returns the original address string.
         *
         * @return address
         */
        public String address() {
            return address;
        }

        /**
         * Returns the set of invalid field keys.
         *
         * @return invalid field keys
         */
        public Set<String> invalidFields() {
            return invalidFields;
        }

        /**
         * Returns whether the given key is marked invalid.
         *
         * @param key field key
         * @return true if invalid
         */
        public boolean fieldInvalid(String key) {
            return invalidFields.contains(key);
        }
    }

    /**
     * Describes one invalid property entry.
     */
    public static final class InvalidPropertyEntry {
        private final int index;
        private final String reason;
        private final String address;
        private final Integer price;
        private final String propertyName;
        private final Set<String> invalidFields;

        /**
         * Constructs an {@code InvalidPropertyEntry}.
         *
         * @param index zero-based index in the properties array
         * @param reason human-readable summary of invalidity
         * @param address original address string
         * @param price original price value
         * @param propertyName original property name string
         * @param invalidFields set of invalid field keys among {"propertyName","address","price"}
         */
        public InvalidPropertyEntry(int index,
                                    String reason,
                                    String address,
                                    Integer price,
                                    String propertyName,
                                    Set<String> invalidFields) {
            this.index = index;
            this.reason = reason;
            this.address = address;
            this.price = price;
            this.propertyName = propertyName;
            this.invalidFields = invalidFields;
        }

        /**
         * Returns the original JSON index.
         *
         * @return index
         */
        public int index() {
            return index;
        }

        /**
         * Returns the reason text.
         *
         * @return reason
         */
        public String reason() {
            return reason;
        }

        /**
         * Returns the original address string.
         *
         * @return address
         */
        public String address() {
            return address;
        }

        /**
         * Returns the original price value.
         *
         * @return price
         */
        public Integer price() {
            return price;
        }

        /**
         * Returns the original property name string.
         *
         * @return property name
         */
        public String propertyName() {
            return propertyName;
        }

        /**
         * Returns the set of invalid field keys.
         *
         * @return invalid field keys
         */
        public Set<String> invalidFields() {
            return invalidFields;
        }

        /**
         * Returns whether the given key is marked invalid.
         *
         * @param key field key
         * @return true if invalid
         */
        public boolean fieldInvalid(String key) {
            return invalidFields.contains(key);
        }
    }
}
