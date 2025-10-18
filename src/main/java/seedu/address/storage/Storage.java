package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;

/**
 * API of the Storage component.
 *
 * This interface aggregates both AddressBookStorage and UserPrefsStorage and
 * adds higher-level helpers that some features (e.g., invalid-entry repair)
 * rely on.
 */
public interface Storage extends AddressBookStorage, UserPrefsStorage {

    // ---- UserPrefs passthroughs ----

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataLoadingException;

    @Override
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    // ---- AddressBook passthroughs ----

    @Override
    Path getAddressBookFilePath();

    @Override
    Optional<ReadOnlyAddressBook> readAddressBook() throws DataLoadingException;

    @Override
    void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException;

    /**
     * Reads the address book and returns a {@link LoadReport} that includes:
     *  - the successfully parsed model snapshot; and
     *  - a list of invalid (quarantined) records with human-readable reasons.
     *
     * Implementations should not throw merely because some entries are invalid;
     * they should still return a report with valid entries and a list of invalids.
     *
     * @throws DataLoadingException if I/O or JSON decoding fails outright.
     */
    LoadReport readAddressBookWithReport() throws DataLoadingException;

    /**
     * Overwrites the raw JSON record at the given zero-based index with the given Person,
     * preserving the array order and all other records. This enables "in-place" correction
     * without reserializing the entire model and accidentally reordering entries.
     *
     * After writing, implementations should re-read and return a fresh {@link LoadReport}
     * so the caller can refresh the in-memory model and see remaining invalids, if any.
     *
     * @param index   zero-based index in the JSON "persons" array
     * @param person  corrected person to write back
     */
    LoadReport overwriteRawEntryAtIndex(int index, seedu.address.model.person.Person person)
            throws seedu.address.commons.exceptions.DataLoadingException, java.io.IOException;
}
