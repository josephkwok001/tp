package seedu.address;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.storage.UserPrefsStorage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class MainAppInitPrefsTest {

    static class StubUserPrefsStorage implements UserPrefsStorage {
        private final Optional<UserPrefs> prefsToReturn;
        private final boolean throwReadError;
        private final boolean throwWriteError;

        StubUserPrefsStorage(Optional<UserPrefs> prefsToReturn,
                             boolean throwReadError,
                             boolean throwWriteError) {
            this.prefsToReturn = prefsToReturn;
            this.throwReadError = throwReadError;
            this.throwWriteError = throwWriteError;
        }

        @Override
        public Path getUserPrefsFilePath() {
            return Path.of("stub-prefs.json");
        }

        @Override
        public Optional<UserPrefs> readUserPrefs() throws DataLoadingException {
            if (throwReadError) {
                throw new DataLoadingException(new IOException("Simulated read error"));
            }
            return prefsToReturn;
        }

        @Override
        public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
            if (throwWriteError) {
                throw new IOException("Simulated save error");
            }
        }
    }

    @Test
    public void initPrefs_validPrefsFile_returnsLoadedPrefs() {
        UserPrefs expected = new UserPrefs();
        StubUserPrefsStorage stub = new StubUserPrefsStorage(Optional.of(expected), false, false);

        UserPrefs result = new MainApp().initPrefs(stub);
        assertEquals(expected, result);
    }

    @Test
    public void initPrefs_missingPrefs_createsDefaultPrefs() {
        StubUserPrefsStorage stub = new StubUserPrefsStorage(Optional.empty(), false, false);

        UserPrefs result = new MainApp().initPrefs(stub);
        assertNotNull(result);
        assertEquals(new UserPrefs(), result);
    }

    @Test
    public void initPrefs_readFails_usesDefaultPrefs() {
        StubUserPrefsStorage stub = new StubUserPrefsStorage(Optional.empty(), true, false);

        UserPrefs result = new MainApp().initPrefs(stub);

        assertNotNull(result);
        assertEquals(new UserPrefs(), result);
    }

    @Test
    public void initPrefs_saveFails_logsWarningButStillReturnsPrefs() {
        StubUserPrefsStorage stub = new StubUserPrefsStorage(Optional.of(new UserPrefs()), false, true);

        UserPrefs result = new MainApp().initPrefs(stub);
        assertNotNull(result);
    }
}
