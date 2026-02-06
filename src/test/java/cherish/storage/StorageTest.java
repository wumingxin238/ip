// src/test/java/cherish/storage/StorageTest.java
package cherish.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cherish.CherishException;
import cherish.model.Deadline;
import cherish.model.Task;
import cherish.model.Todo;

class StorageTest {

    private static final String TEST_FILE_PATH = "data/test_cherish.txt";
    private Storage storage;

    @BeforeEach
    void setUp() throws IOException {
        // Ensure the test directory exists
        Files.createDirectories(Paths.get("data"));
        // Create a fresh storage instance for each test
        storage = new Storage(TEST_FILE_PATH);
        // Clean up any existing test file before each test
        File testFile = new File(TEST_FILE_PATH);
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @AfterEach
    void tearDown() {
        // Clean up the test file after each test
        File testFile = new File(TEST_FILE_PATH);
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    void testSaveAndLoadSingleTodo() throws CherishException {
        Task[] tasksToSave = new Task[]{new Todo("Write unit test")};
        storage.save(tasksToSave);

        Task[] loadedTasks = storage.load();
        assertEquals(1, loadedTasks.length, "Should load one task");
        assertEquals("Write unit test", loadedTasks[0].getDescription(), "Description should match");
        assertTrue(loadedTasks[0] instanceof Todo, "Loaded task should be a Todo");
    }

    @Test
    void testSaveAndLoadMultipleTasks() throws CherishException {
        Todo todo = new Todo("Buy groceries");
        Deadline deadline = new Deadline("Submit assignment", LocalDateTime.of(2026, 2, 1, 23, 59));

        Task[] tasksToSave = new Task[]{todo, deadline};
        storage.save(tasksToSave);

        Task[] loadedTasks = storage.load();
        assertEquals(2, loadedTasks.length, "Should load two tasks");
        assertTrue(loadedTasks[0] instanceof Todo, "First task should be a Todo");
        assertTrue(loadedTasks[1] instanceof Deadline, "Second task should be a Deadline");
        assertEquals("Buy groceries", loadedTasks[0].getDescription(), "First description should match");
        assertEquals("Submit assignment", loadedTasks[1].getDescription(), "Second description should match");
    }

    @Test
    void testLoadNonExistentFile_returnsEmptyArray() throws CherishException {
        Task[] loadedTasks = storage.load();
        assertNotNull(loadedTasks, "Load result should not be null");
        assertEquals(0, loadedTasks.length, "Should return an empty array for a non-existent file");
    }

    @Test
    void testSaveToFile_thenLoadFromFile_contentMatches() throws CherishException {
        Todo originalTodo = new Todo("Original Task");
        originalTodo.markAsDone(); // Mark it to test persistence of status
        Deadline originalDeadline = new Deadline("DDL Task", LocalDateTime.of(2026, 1, 1, 0, 0));
        // Note: Don't mark deadline as done for this test, to mix states

        Task[] originalTasks = new Task[]{originalTodo, originalDeadline};
        storage.save(originalTasks);

        // Load from the same file
        Task[] reloadedTasks = storage.load();

        // Compare properties of the reloaded objects
        assertEquals(2, reloadedTasks.length);
        assertEquals(originalTodo.getDescription(), reloadedTasks[0].getDescription());
        assertEquals(originalTodo.isDone(), reloadedTasks[0].isDone()); // Check status too
        assertTrue(reloadedTasks[0] instanceof Todo);

        assertEquals(originalDeadline.getDescription(), reloadedTasks[1].getDescription());
        assertEquals(originalDeadline.getBy(), ((Deadline) reloadedTasks[1]).getBy());
        assertEquals(originalDeadline.isDone(), reloadedTasks[1].isDone()); // Should be false
        assertTrue(reloadedTasks[1] instanceof Deadline);
    }
}
