package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;

/**
 * An immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";
    public static final String MESSAGE_DUPLICATE_PROPERTY = "Properties list contains duplicate property(ies).";

    private static final Logger LOGGER = LogsCenter.getLogger(JsonSerializableAddressBook.class);

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();
    private final List<JsonAdaptedProperty> properties = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given persons and properties.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("persons") List<JsonAdaptedPerson> persons,
                                       @JsonProperty("properties") List<JsonAdaptedProperty> properties) {
        if (persons != null) {
            this.persons.addAll(persons);
        }
        if (properties != null) {
            this.properties.addAll(properties);
        }
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     */
    public JsonSerializableAddressBook(seedu.address.model.ReadOnlyAddressBook source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedPerson::new).toList());
        properties.addAll(source.getPropertyList().stream().map(JsonAdaptedProperty::new).toList());
    }

    /**
     * Converts this JSON-serializable address book into the model type while
     * generating a {@link LoadReport} that includes invalid entries for both
     * properties and persons.
     *
     * @return load report with model snapshot and invalid entries
     */
    public LoadReport toModelTypeWithReport() throws seedu.address.commons.exceptions.IllegalValueException {
        AddressBook model = new AddressBook();
        java.util.List<LoadReport.InvalidPersonEntry> invalidPersons = new java.util.ArrayList<>();
        java.util.List<LoadReport.InvalidPropertyEntry> invalidProps = new java.util.ArrayList<>();

        for (int i = 0; i < properties.size(); i++) {
            JsonAdaptedProperty jap = properties.get(i);
            try {
                seedu.address.model.property.Property prop = jap.toModelType();
                if (model.hasProperty(prop)) {
                    invalidProps.add(new LoadReport.InvalidPropertyEntry(
                            i,
                            MESSAGE_DUPLICATE_PROPERTY,
                            jap.getAddress(),
                            jap.getPrice(),
                            jap.getName(),
                            java.util.Set.of("propertyName")
                    ));
                    continue;
                }
                model.addProperty(prop);
            } catch (seedu.address.commons.exceptions.IllegalValueException ive) {
                java.util.Set<String> keys = jap.invalidFieldKeys();
                String reason = ive.getMessage() == null ? "Invalid property" : ive.getMessage();
                invalidProps.add(new LoadReport.InvalidPropertyEntry(
                        i,
                        reason,
                        jap.getAddress(),
                        jap.getPrice(),
                        jap.getName(),
                        keys
                ));
            }
        }

        java.util.Map<Integer, seedu.address.model.person.Person> indexToPerson = new java.util.LinkedHashMap<>();
        for (int i = 0; i < persons.size(); i++) {
            JsonAdaptedPerson jap = persons.get(i);
            try {
                seedu.address.model.person.Person p = jap.toModelType(model);
                if (model.hasPerson(p)) {
                    invalidPersons.add(new LoadReport.InvalidPersonEntry(
                            i,
                            MESSAGE_DUPLICATE_PERSON,
                            jap.getName(),
                            jap.getPhone(),
                            jap.getEmail(),
                            jap.getAddress(),
                            java.util.Set.of()
                    ));
                    continue;
                }
                model.addPerson(p);
                indexToPerson.put(i, p);
            } catch (seedu.address.commons.exceptions.IllegalValueException ive) {
                String reason = ive.getMessage() == null ? "Invalid person" : ive.getMessage();
                invalidPersons.add(new LoadReport.InvalidPersonEntry(
                        i,
                        reason,
                        jap.getName(),
                        jap.getPhone(),
                        jap.getEmail(),
                        jap.getAddress(),
                        jap.invalidFieldKeys()
                ));
            }
        }

        java.util.Map<String, seedu.address.model.property.Property> propertyByName = new java.util.LinkedHashMap<>();
        for (seedu.address.model.property.Property p : model.getPropertyList()) {
            propertyByName.put(p.getPropertyName().toString(), p);
        }

        for (int i = 0; i < persons.size(); i++) {
            seedu.address.model.person.Person base = indexToPerson.get(i);
            if (base == null) {
                continue;
            }
            JsonAdaptedPerson jap = persons.get(i);
            java.util.List<String> ownedNames = jap.getOwnedProperties();
            java.util.List<String> interestedNames = jap.getInterestedProperties();
            java.util.List<seedu.address.model.property.Property> resolvedOwned = new java.util.ArrayList<>();
            java.util.List<seedu.address.model.property.Property> resolvedInterested = new java.util.ArrayList<>();
            java.util.List<String> unknowns = new java.util.ArrayList<>();

            for (String n : ownedNames) {
                var prop = propertyByName.get(n);
                if (prop != null) {
                    resolvedOwned.add(prop);
                } else {
                    unknowns.add(n);
                }
            }
            for (String n : interestedNames) {
                var prop = propertyByName.get(n);
                if (prop != null) {
                    resolvedInterested.add(prop);
                } else {
                    unknowns.add(n);
                }
            }

            seedu.address.model.person.Person replaced = new seedu.address.model.person.Person(
                    base.getName(), base.getPhone(), base.getEmail(), base.getAddress(),
                    base.getTags(), resolvedOwned, resolvedInterested
            );
            model.setPerson(base, replaced);

            if (!unknowns.isEmpty()) {
                invalidPersons.add(new LoadReport.InvalidPersonEntry(
                        i,
                        "Unknown properties: " + String.join(", ", unknowns),
                        jap.getName(), jap.getPhone(), jap.getEmail(), jap.getAddress(),
                        java.util.Set.of("properties")
                ));
            }
        }

        return new LoadReport(new LoadReport.ModelData(model), invalidPersons, invalidProps);
    }

    /**
     * Converts this JSON-serializable address book into the model type.
     *
     * @return an {@code AddressBook} containing all valid entries
     * @throws IllegalValueException if there are duplicate or invalid entries
     */
    public AddressBook toModelType() throws IllegalValueException {
        LoadReport report = toModelTypeWithReport();
        if (!report.getInvalids().isEmpty()) {
            throw new IllegalValueException(report.getInvalids().get(0).reason());
        }
        return report.getModelData().getAddressBook();
    }

    /**
     * Replaces the person at the given index with the specified replacement.
     *
     * @param index index of the person to replace
     * @param replacement the replacement {@link JsonAdaptedPerson}
     */
    void replaceAt(int index, JsonAdaptedPerson replacement) {
        if (index < 0 || index >= persons.size()) {
            throw new IndexOutOfBoundsException("Invalid person index: " + index);
        }
        persons.set(index, replacement);
    }
}
