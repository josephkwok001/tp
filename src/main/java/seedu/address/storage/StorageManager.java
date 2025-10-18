package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;

/**
 * Coordinates reading/writing of both AddressBook data and UserPrefs.
 * This class mostly delegates to concrete storage implementations.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private AddressBookStorage addressBookStorage;
    private UserPrefsStorage userPrefsStorage;

    /**
     * Constructs a StorageManager with the provided storage implementations.
     */
    public StorageManager(AddressBookStorage addressBookStorage, UserPrefsStorage userPrefsStorage) {
        this.addressBookStorage = addressBookStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataLoadingException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }

    // ================ AddressBook methods ==============================

    @Override
    public Path getAddressBookFilePath() {
        return addressBookStorage.getAddressBookFilePath();
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook() throws DataLoadingException {
        return readAddressBook(addressBookStorage.getAddressBookFilePath());
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook(Path filePath) throws DataLoadingException {
        logger.fine("Attempting to read data from file: " + filePath);
        return addressBookStorage.readAddressBook(filePath);
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
        saveAddressBook(addressBook, addressBookStorage.getAddressBookFilePath());
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        addressBookStorage.saveAddressBook(addressBook, filePath);
    }

    /**
     * Reads data and also returns information about invalid entries collected during parse.
     */
    @Override
    public LoadReport readAddressBookWithReport() throws DataLoadingException {
        Path path = addressBookStorage.getAddressBookFilePath();
        logger.fine("Attempting to read data (with report) from file: " + path);
        return addressBookStorage.readAddressBookWithReport(path);
    }

    @Override
    public LoadReport readAddressBookWithReport(Path filePath) throws DataLoadingException {
        logger.fine("Attempting to read data (with report) from file: " + filePath);
        return addressBookStorage.readAddressBookWithReport(filePath);
    }

    /**
     * In-place overwrite of a single raw JSON entry, then return a fresh LoadReport.
     */
    @Override
    public LoadReport overwriteRawEntryAtIndex(int index, seedu.address.model.person.Person person)
            throws seedu.address.commons.exceptions.DataLoadingException, java.io.IOException {
        return addressBookStorage.overwriteRawEntryAtIndex(index, person);
    }
}
