package cherish.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import cherish.CherishException;
import cherish.model.Deadline;
import cherish.model.Event;
import cherish.model.Task;
import cherish.model.Todo;

public class StorageTest {

    private static final String TEST_FILE = "data/test-storage.txt";

    @AfterEach
    void cleanup() throws IOException {
        Files.deleteIfExists(Path.of(TEST_FILE));
    }

    /* =====================
       Save & Load
       ===================== */

    @Test
    void load_fileDoesNotExist_returnsEmptyArray() throws Exception {
        Storage storage = new Storage(TEST_FILE);

        Task[] tasks = storage.load();

        assertEquals(0, tasks.length);
    }

    @Test
    void saveAndLoad_todoTask_success() throws Exception {
        Storage storage = new Storage(TEST_FILE);

        Task[] original = {
            new Todo("read book")
        };

        storage.save(original);
        Task[] loaded = storage.load();

        assertEquals(1, loaded.length);
        assertInstanceOf(Todo.class, loaded[0]);
        assertEquals("read book", loaded[0].getDescription());
        assertTrue(!loaded[0].isDone());
    }

    @Test
    void saveAndLoad_deadlineTask_success() throws Exception {
        Storage storage = new Storage(TEST_FILE);

        LocalDateTime by = LocalDateTime.of(2026, 2, 1, 18, 0);
        Task[] original = {
            new Deadline("submit report", by)
        };

        storage.save(original);
        Task[] loaded = storage.load();

        assertEquals(1, loaded.length);
        assertInstanceOf(Deadline.class, loaded[0]);
        assertEquals("submit report", loaded[0].getDescription());
    }

    @Test
    void saveAndLoad_eventTask_success() throws Exception {
        Storage storage = new Storage(TEST_FILE);

        LocalDateTime from = LocalDateTime.of(2026, 2, 1, 14, 0);
        LocalDateTime to = LocalDateTime.of(2026, 2, 1, 16, 0);

        Task[] original = {
            new Event("meeting", from, to)
        };

        storage.save(original);
        Task[] loaded = storage.load();

        assertEquals(1, loaded.length);
        assertInstanceOf(Event.class, loaded[0]);
        assertEquals("meeting", loaded[0].getDescription());
    }

    @Test
    void saveAndLoad_doneTask_preservesDoneStatus() throws Exception {
        Storage storage = new Storage(TEST_FILE);

        Todo todo = new Todo("read book");
        todo.markAsDone();

        storage.save(new Task[]{ todo });
        Task[] loaded = storage.load();

        assertTrue(loaded[0].isDone());
    }

    /* =====================
       Corrupted data
       ===================== */

    @Test
    void load_corruptedLine_throwsException() throws Exception {
        Files.createDirectories(Path.of("data"));
        Files.writeString(
                Path.of(TEST_FILE),
                "INVALID DATA FORMAT"
        );

        Storage storage = new Storage(TEST_FILE);

        assertThrows(
                CherishException.class,
                storage::load
        );
    }

    @Test
    void load_invalidDateFormat_throwsException() throws Exception {
        Files.createDirectories(Path.of("data"));
        Files.writeString(
                Path.of(TEST_FILE),
                "D | 0 | deadline | invalid-date"
        );

        Storage storage = new Storage(TEST_FILE);

        assertThrows(
                CherishException.class,
                storage::load
        );
    }
}
