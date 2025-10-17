package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
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
        Optional<JsonSerializableAddressBook> json =
                JsonUtil.readJsonFile(file, JsonSerializableAddressBook.class);
        if (json.isEmpty()) {
            // No file or empty file -> no model.
            return Optional.empty();
        }
        try {
            return Optional.of(json.get().toModelType());
        } catch (IllegalValueException ive) {
            // Structural issues or validation failures while converting to model.
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
        FileUtil.createIfMissing(file); // Create file and parent dirs if needed.
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

        Optional<JsonSerializableAddressBook> json =
                JsonUtil.readJsonFile(file, JsonSerializableAddressBook.class);

        if (json.isEmpty()) {
            // Empty/missing file => empty model, no invalids.
            return new LoadReport(
                    new LoadReport.ModelData(new seedu.address.model.AddressBook()),
                    java.util.Collections.emptyList());
        }

        try {
            // JsonSerializableAddressBook is expected to provide this method.
            return json.get().toModelTypeWithReport();
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + file + ": " + ive.getMessage());
            throw new DataLoadingException(ive);
        }
    }

    /**
     * Replaces exactly one element in the raw JSON "persons" array at the specified index.
     *
     * This preserves the order and indices of all other entries. It does NOT rebuild the whole
     * file from the in-memory model, thus avoiding unintended reordering.
     *
     * After writing, it re-reads the file and returns a fresh LoadReport so the caller can
     * refresh the model and continue fixing remaining invalid entries (if any).
     */
    @Override
    public LoadReport overwriteRawEntryAtIndex(int index, Person person)
            throws DataLoadingException, IOException {
        requireNonNull(person);

        // Use the same ObjectMapper as the rest of the app for consistent settings.
        final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        final File file = filePath.toFile();

        // Ensure the file exists; if absent, create an empty JSON file.
        FileUtil.createIfMissing(filePath);

        // Read the current JSON root. For an empty file, readTree(...) returns null.
        ObjectNode root;
        var node = mapper.readTree(file);
        if (node == null || !node.isObject()) {
            // Initialize a minimal root with an empty "persons" array
            root = mapper.createObjectNode();
            root.set("persons", mapper.createArrayNode());
        } else {
            root = (ObjectNode) node;
        }

        // Locate the "persons" array; create it if missing, but fail if it's not an array.
        var personsNode = root.get("persons");
        if (personsNode == null) {
            personsNode = mapper.createArrayNode();
            root.set("persons", personsNode);
        }
        if (!personsNode.isArray()) {
            throw new IOException("'persons' is not a JSON array in " + filePath);
        }
        ArrayNode persons = (ArrayNode) personsNode;

        // Validate bounds strictly to avoid creating holes or silently appending.
        if (index < 0 || index >= persons.size()) {
            throw new IOException("Index out of bounds: " + index + " (size=" + persons.size() + ")");
        }

        // Convert the corrected Person to its JSON-adapted form expected by the file format.
        ObjectNode replacement = (ObjectNode) mapper.valueToTree(new JsonAdaptedPerson(person));

        // In-place replacement at the exact index.
        persons.set(index, replacement);

        // Write the updated JSON via a temp file, then atomically move it into place.
        Path tmp = filePath.resolveSibling(filePath.getFileName() + ".tmp");
        mapper.writerWithDefaultPrettyPrinter().writeValue(tmp.toFile(), root);
        java.nio.file.Files.move(
                tmp, filePath,
                java.nio.file.StandardCopyOption.REPLACE_EXISTING,
                java.nio.file.StandardCopyOption.ATOMIC_MOVE
        );

        // Re-read and produce a fresh LoadReport after the modification.
        return readAddressBookWithReport(filePath);
    }
}
