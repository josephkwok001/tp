package seedu.address.storage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.property.Property;

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
     * generating a {@link LoadReport} that includes invalid entries.
     *
     * @return a {@link LoadReport} containing the model data and invalid entries
     * @throws IllegalValueException if duplicate persons or properties are found
     */
    public LoadReport toModelTypeWithReport() throws IllegalValueException {
        AddressBook model = new AddressBook();
        List<LoadReport.InvalidPersonEntry> invalidList = new ArrayList<>();

        for (JsonAdaptedProperty jap : properties) {
            Property prop = jap.toModelType();
            if (model.hasProperty(prop)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PROPERTY);
            }
            model.addProperty(prop);
        }

        for (int i = 0; i < persons.size(); i++) {
            JsonAdaptedPerson jap = persons.get(i);
            try {
                Person p = jap.toModelType(model);
                if (model.hasPerson(p)) {
                    invalidList.add(new LoadReport.InvalidPersonEntry(
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
            } catch (IllegalValueException ive) {
                String reason = ive.getMessage() == null ? "Invalid person" : ive.getMessage();
                invalidList.add(new LoadReport.InvalidPersonEntry(
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

        Map<String, Property> propertyByName = new LinkedHashMap<>();
        for (Property p : model.getPropertyList()) {
            propertyByName.put(p.getPropertyName().toString(), p);
        }

        Map<String, Person> personByName = new LinkedHashMap<>();
        for (Person p : model.getPersonList()) {
            personByName.put(p.getName().fullName, p);
        }

        for (int i = 0; i < persons.size(); i++) {
            JsonAdaptedPerson jap = persons.get(i);
            Person base = personByName.get(jap.getName());
            if (base == null) {
                continue;
            }

            List<String> ownedNames = jap.getOwnedProperties();
            List<String> interestedNames = jap.getInterestedProperties();
            List<Property> resolvedOwned = new ArrayList<>();
            List<Property> resolvedInterested = new ArrayList<>();
            List<String> unknowns = new ArrayList<>();

            for (String n : ownedNames) {
                Property prop = propertyByName.get(n);
                if (prop != null) {
                    resolvedOwned.add(prop);
                } else {
                    unknowns.add(n);
                }
            }

            for (String n : interestedNames) {
                Property prop = propertyByName.get(n);
                if (prop != null) {
                    resolvedInterested.add(prop);
                } else {
                    unknowns.add(n);
                }
            }

            Person replaced = new Person(
                    base.getName(),
                    base.getPhone(),
                    base.getEmail(),
                    base.getAddress(),
                    base.getTags(),
                    resolvedOwned,
                    resolvedInterested
            );
            model.setPerson(base, replaced);

            if (!unknowns.isEmpty()) {
                invalidList.add(new LoadReport.InvalidPersonEntry(
                        i,
                        "Unknown properties: " + String.join(", ", unknowns),
                        jap.getName(),
                        jap.getPhone(),
                        jap.getEmail(),
                        jap.getAddress(),
                        java.util.Set.of("properties")
                ));
            }
        }

        return new LoadReport(new LoadReport.ModelData(model), invalidList);
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
