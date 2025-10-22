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
import seedu.address.model.person.OwnedProperties;
import seedu.address.model.person.Person;
import seedu.address.model.property.Property;

/**
 * An immutable AddressBook that is serializable to and from JSON.
 * Also provides a helper that returns both the model and a list of
 * quarantined (invalid) entries for startup correction.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";
    public static final String MESSAGE_DUPLICATE_PROPERTY = "Properties list contains duplicate property(ies).";

    private static final Logger LOGGER =
            LogsCenter.getLogger(JsonSerializableAddressBook.class);

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();
    private final List<JsonAdaptedProperty> properties = new ArrayList<>();

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

    public JsonSerializableAddressBook(seedu.address.model.ReadOnlyAddressBook source) {
        persons.addAll(source.getPersonList().stream()
                .map(JsonAdaptedPerson::new)
                .toList());
        properties.addAll(source.getPropertyList().stream()
                .map(JsonAdaptedProperty::new)
                .toList());
    }

    /**
     * Builds the model and a {@link LoadReport} that lists all invalid entries
     * encountered during conversion.
     */
    public LoadReport toModelTypeWithReport() throws IllegalValueException {
        AddressBook model = new AddressBook();
        List<LoadReport.InvalidPersonEntry> invalidList = new ArrayList<>();
        for (int i = 0; i < properties.size(); i++) {
            JsonAdaptedProperty jap = properties.get(i);
            Property prop = jap.toModelType();
            if (model.hasProperty(prop)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PROPERTY);
            }
            model.addProperty(prop);
        }

        for (int i = 0; i < persons.size(); i++) {
            JsonAdaptedPerson jap = persons.get(i);
            try {
                Person p = jap.toModelType();

                if (model.hasPerson(p)) {
                    invalidList.add(new LoadReport.InvalidPersonEntry(
                            i,
                            MESSAGE_DUPLICATE_PERSON,
                            jap.getName(),
                            jap.getPhone(),
                            jap.getEmail(),
                            jap.getAddress(),
                            java.util.Set.of() // no specific field is invalid; it's a duplicate
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

            java.util.List<String> names = jap.getOwnedProperties();
            java.util.List<Property> resolved = new java.util.ArrayList<>();
            java.util.List<String> unknowns = new java.util.ArrayList<>();

            for (String n : names) {
                Property prop = propertyByName.get(n);
                if (prop != null) {
                    resolved.add(prop);
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
                    new OwnedProperties(resolved)
            );
            model.setPerson(base, replaced);

            if (!unknowns.isEmpty()) {
                invalidList.add(new LoadReport.InvalidPersonEntry(
                        i,
                        "Unknown owned properties: " + String.join(", ", unknowns),
                        jap.getName(),
                        jap.getPhone(),
                        jap.getEmail(),
                        jap.getAddress(),
                        java.util.Set.of("ownedProperties")
                ));
            }
        }
        return new LoadReport(new LoadReport.ModelData(model), invalidList);
    }

    /**
     * Legacy method for code paths that only expect a model.
     * If there are invalid entries, this throws the first {@link IllegalValueException}
     * encountered, matching the original behavior.
     */
    public AddressBook toModelType() throws IllegalValueException {
        LoadReport report = toModelTypeWithReport();
        if (!report.getInvalids().isEmpty()) {
            throw new IllegalValueException(report.getInvalids().get(0).reason());
        }
        return report.getModelData().getAddressBook();
    }

    void replaceAt(int index, JsonAdaptedPerson replacement) {
        if (index < 0 || index >= persons.size()) {
            throw new IndexOutOfBoundsException("Invalid person index: " + index);
        }
        persons.set(index, replacement);
    }
}
