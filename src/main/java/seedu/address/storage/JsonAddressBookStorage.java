package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.property.Property;

/**
 * JSON-backed implementation of AddressBookStorage.
 *
 * Besides the usual read/write, this class offers:
 * - readAddressBookWithReport(Path): parse while collecting invalid entries;
 * - overwriteRawEntryAtIndex(int, Person): in-place JSON array element replacement.
 */
public class JsonAddressBookStorage implements AddressBookStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonAddressBookStorage.class);

    private final Path filePath;

    public JsonAddressBookStorage(Path filePath) {
        this.filePath = requireNonNull(filePath);
    }

    @Override
    public Path getAddressBookFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook() throws DataLoadingException {
        return readAddressBook(filePath);
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook(Path file) throws DataLoadingException {
        requireNonNull(file);
        Optional<JsonSerializableAddressBook> jsonAddressBook =
                JsonUtil.readJsonFile(file, JsonSerializableAddressBook.class);
        if (jsonAddressBook.isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(jsonAddressBook.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + file + ": " + ive.getMessage());
            throw new DataLoadingException(ive);
        }
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
        saveAddressBook(addressBook, filePath);
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook, Path file) throws IOException {
        requireNonNull(addressBook);
        requireNonNull(file);
        FileUtil.createIfMissing(file);
        JsonUtil.saveJsonFile(new JsonSerializableAddressBook(addressBook), file);
    }

    /**
     * Reads JSON and returns a LoadReport containing:
     *  - model snapshot constructed from valid records;
     *  - a list of invalid records with reasons (quarantined).
     */
    @Override
    public LoadReport readAddressBookWithReport(Path file) throws DataLoadingException {
        requireNonNull(file);

        Optional<JsonSerializableAddressBook> jsonAddressBook =
                JsonUtil.readJsonFile(file, JsonSerializableAddressBook.class);

        if (jsonAddressBook.isEmpty()) {
            return new LoadReport(
                    new LoadReport.ModelData(new seedu.address.model.AddressBook()),
                    java.util.Collections.emptyList());
        }

        try {
            return jsonAddressBook.get().toModelTypeWithReport();
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + file + ": " + ive.getMessage());
            throw new DataLoadingException(ive);
        }
    }

    /**
     * Replaces exactly one element in the raw JSON "persons" array at the specified index,
     * writes back atomically, then re-reads and returns a fresh LoadReport.
     */
    @Override
    public LoadReport overwriteRawEntryAtIndex(int index, Person person)
            throws DataLoadingException, IOException {
        requireNonNull(person);

        final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        final File file = filePath.toFile();

        FileUtil.createIfMissing(filePath);

        ObjectNode root;
        var node = mapper.readTree(file);
        if (node == null || !node.isObject()) {
            root = mapper.createObjectNode();
            root.set("persons", mapper.createArrayNode());
        } else {
            root = (ObjectNode) node;
        }

        var personsNode = root.get("persons");
        if (personsNode == null) {
            personsNode = mapper.createArrayNode();
            root.set("persons", personsNode);
        }
        if (!personsNode.isArray()) {
            throw new IOException("'persons' is not a JSON array in " + filePath);
        }
        ArrayNode persons = (ArrayNode) personsNode;

        if (index < 0 || index >= persons.size()) {
            throw new IOException("Index out of bounds: " + index + " (size=" + persons.size() + ")");
        }

        ObjectNode replacement = (ObjectNode) mapper.valueToTree(new JsonAdaptedPerson(person));
        persons.set(index, replacement);

        Path tmp = filePath.resolveSibling(filePath.getFileName() + ".tmp");
        mapper.writerWithDefaultPrettyPrinter().writeValue(tmp.toFile(), root);
        java.nio.file.Files.move(
                tmp, filePath,
                java.nio.file.StandardCopyOption.REPLACE_EXISTING,
                java.nio.file.StandardCopyOption.ATOMIC_MOVE
        );

        return readAddressBookWithReport(filePath);
    }

    /**
     * Replaces exactly one element in the raw JSON "properties" array at the specified index,
     * writes back atomically, then re-reads and returns a fresh LoadReport.
     */
    public LoadReport overwriteRawPropertyAtIndex(int index, Property property)
            throws DataLoadingException, IOException {
        requireNonNull(property);

        final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        final File file = filePath.toFile();

        FileUtil.createIfMissing(filePath);

        ObjectNode root;
        var node = mapper.readTree(file);
        if (node == null || !node.isObject()) {
            root = mapper.createObjectNode();
            root.set("properties", mapper.createArrayNode());
        } else {
            root = (ObjectNode) node;
        }

        var propertiesNode = root.get("properties");
        if (propertiesNode == null) {
            propertiesNode = mapper.createArrayNode();
            root.set("properties", propertiesNode);
        }
        if (!propertiesNode.isArray()) {
            throw new IOException("'properties' is not a JSON array in " + filePath);
        }
        ArrayNode properties = (ArrayNode) propertiesNode;

        if (index < 0 || index >= properties.size()) {
            throw new IOException("Index out of bounds: " + index + " (size=" + properties.size() + ")");
        }

        ObjectNode replacement = (ObjectNode) mapper.valueToTree(new JsonAdaptedProperty(property));
        properties.set(index, replacement);

        Path tmp = filePath.resolveSibling(filePath.getFileName() + ".tmp");
        mapper.writerWithDefaultPrettyPrinter().writeValue(tmp.toFile(), root);
        java.nio.file.Files.move(
                tmp, filePath,
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.ATOMIC_MOVE
        );

        return readAddressBookWithReport(filePath);
    }
}
