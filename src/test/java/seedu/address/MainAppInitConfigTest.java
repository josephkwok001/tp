package seedu.address;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.core.Config;

/**
 * Tests MainApp.initConfig behavior with explicit file paths.
 */
public class MainAppInitConfigTest {

    @TempDir
    public Path tmp;

    @Test
    void initConfig_createsFileWhenMissing() {
        MainApp app = new MainApp();
        Path cfg = tmp.resolve("config.json");
        Config out = app.initConfig(cfg);
        assertNotNull(out);
        assertTrue(Files.exists(cfg));
    }

    @Test
    void initConfig_readsOrDefaultsWhenPresent() throws Exception {
        MainApp app = new MainApp();
        Path cfg = tmp.resolve("config.json");
        Files.writeString(cfg, "{}");
        Config out = app.initConfig(cfg);
        assertNotNull(out);
        assertTrue(Files.exists(cfg));
    }
}
