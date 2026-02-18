package cherish.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import cherish.CherishException;
import cherish.model.Deadline;
import cherish.model.Event;
import cherish.model.Task;
import cherish.model.TaskType;
import cherish.model.Todo;

/**
 * Handles loading and saving tasks to a local storage file.
 * Responsible for creating the data directory, reading persisted data,
 * and writing tasks back to disk.
 *
 * This class detects file I/O issues and corrupted storage data,
 * and reports them using CherishException.
 */
public class Storage {

    /** Date-time format used for saving and loading task data. */
    private static final DateTimeFormatter SAVE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    /** Path to the storage file. */
    private final String filePath;

    /**
     * Constructs a Storage object with the given file path.
     *
     * @param filePath Path to the file used for persistence.
     */
    public Storage(String filePath) {
        assert filePath != null && !filePath.trim().isEmpty()
                : "Storage filePath must not be null or empty";
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the storage file.
     *
     * If the data directory does not exist, it will be created.
     * If the storage file does not exist, an empty task list is returned.
     *
     * @return An array of loaded Task objects.
     * @throws CherishException If the directory cannot be created,
     *                          the file cannot be read,
     *                          or the file contents are corrupted.
     */
    public Task[] load() throws CherishException {
        Path dataDir = Paths.get("data");
        Path file = Paths.get(filePath);

        ensureDataDirectoryExists(dataDir);

        if (!Files.exists(file)) {
            return new Task[0];
        }

        ArrayList<Task> tasks = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                tasks.add(parseTask(line));
            }
        } catch (IOException e) {
            throw new CherishException("Failed to read tasks from storage file.");
        }

        return tasks.toArray(new Task[0]);
    }

    /**
     * Saves all tasks to the storage file, overwriting existing content.
     *
     * @param tasks Array of Task objects to be saved.
     * @throws CherishException If writing to the file fails.
     */
    public void save(Task[] tasks) throws CherishException {
        Path file = Paths.get(filePath);

        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            for (Task task : tasks) {
                writer.write(task.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new CherishException("Failed to save tasks to storage file.");
        }
    }

    /**
     * Ensures the data directory exists.
     *
     * @param dataDir Path to the data directory.
     * @throws CherishException If the directory cannot be created.
     */
    private void ensureDataDirectoryExists(Path dataDir) throws CherishException {
        if (!Files.exists(dataDir)) {
            try {
                Files.createDirectories(dataDir);
            } catch (IOException e) {
                throw new CherishException("Could not create data directory.");
            }
        }
    }

    /**
     * Parses a single line from the storage file into a Task object.
     *
     * Expected formats:
     * Todo:     T | 0/1 | description
     * Deadline: D | 0/1 | description | yyyy-MM-dd HHmm
     * Event:    E | 0/1 | description | yyyy-MM-dd HHmm | yyyy-MM-dd HHmm
     *
     * @param line A line from the storage file.
     * @return The parsed Task object.
     * @throws CherishException If the data format is invalid or corrupted.
     */
    private Task parseTask(String line) throws CherishException {
        String[] parts = line.split(" \\| ", -1);

        if (parts.length < 3) {
            throw new CherishException("Corrupted data format in storage file.");
        }

        String typeSymbol = parts[0];
        boolean isDone = "1".equals(parts[1]);
        String description = parts[2];

        Task task;

        try {
            TaskType type = TaskType.fromSymbol(typeSymbol);

            switch (type) {
            case TODO:
                task = new Todo(description);
                break;

            case DEADLINE:
                if (parts.length < 4) {
                    throw new CherishException("Corrupted deadline data.");
                }
                LocalDateTime by = parseDateTime(parts[3]);
                task = new Deadline(description, by);
                break;

            case EVENT:
                if (parts.length < 5) {
                    throw new CherishException("Corrupted event data.");
                }
                LocalDateTime from = parseDateTime(parts[3]);
                LocalDateTime to = parseDateTime(parts[4]);
                task = new Event(description, from, to);
                break;

            default:
                throw new CherishException("Unknown task type in storage file.");
            }
        } catch (DateTimeParseException e) {
            throw new CherishException("Invalid date/time format in storage file.");
        }

        if (isDone) {
            task.markAsDone();
        }

        return task;
    }

    /**
     * Parses a date-time string using the storage formatter.
     *
     * @param dateTimeStr Date-time string from file.
     * @return Parsed LocalDateTime.
     * @throws DateTimeParseException If the format is invalid.
     */
    private LocalDateTime parseDateTime(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr, SAVE_FORMATTER);
    }
}
