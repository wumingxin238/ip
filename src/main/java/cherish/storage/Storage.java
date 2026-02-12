package cherish.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import cherish.CherishException;
import cherish.model.Deadline;
import cherish.model.Event;
import cherish.model.Task;
import cherish.model.TaskType;
import cherish.model.Todo;

/**
 * Handles reading from and writing to the task storage file.
 * Ensures the data directory exists and manages the persistence of the task list.
 */
public class Storage {

    private static final DateTimeFormatter SAVE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private String filePath;

    /**
     * Constructs a Storage object with the path to the data file.
     *
     * @param filePath The path to the file where tasks will be saved and loaded from.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads the list of tasks from the storage file.
     * Creates the data directory if it doesn't exist.
     * Returns an empty array if the file does not exist.
     *
     * @return An array of Task objects loaded from the file.
     * @throws CherishException If there is an error creating the directory or reading the file.
     */
    public Task[] load() throws CherishException {
        Path dataDir = Paths.get("data");
        Path file = Paths.get(filePath);

        if (!Files.exists(dataDir)) {
            try {
                Files.createDirectories(dataDir);
            } catch (IOException e) {
                throw new CherishException("Could not create data directory.");
            }
        }

        if (!Files.exists(file)) {
            return new Task[0];
        }

        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            java.util.ArrayList<Task> loadedTasks = new java.util.ArrayList<>();
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                loadedTasks.add(parseTask(line));
            }
            return loadedTasks.toArray(new Task[0]);
        } catch (IOException e) {
            throw new CherishException("Failed to read from file.");
        }
    }

    /**
     * Saves the current list of tasks to the storage file, overwriting its contents.
     *
     * @param tasks An array of Task objects to be saved.
     * @throws CherishException If there is an error writing to the file.
     */
    public void save(Task[] tasks) throws CherishException {
        Path file = Paths.get(filePath);
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            for (Task task : tasks) {
                writer.write(task.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new CherishException("Failed to save tasks to file.");
        }
    }

    /**
     * Parses a single line from the storage file into a Task object.
     * The line is expected to be in the format: TYPE | DONE_STATUS | DESCRIPTION [| additional fields...]
     *
     * @param line The line from the file to be parsed.
     * @return A Task object (Todo, Deadline, or Event) corresponding to the line.
     * @throws CherishException If the line format is invalid or contains corrupted data.
     */
    private Task parseTask(String line) throws CherishException {
        String[] parts = line.split(" \\| ", -1); // Use -1 to keep empty trailing parts if any
        if (parts.length < 3) {
            throw new CherishException("Corrupted data format.");
        }

        String typeSymbol = parts[0];
        boolean isDone = "1".equals(parts[1]);
        String description = parts[2];

        Task task;
        TaskType type = TaskType.fromSymbol(typeSymbol);

        switch (type) {
        case TODO: {
            task = new Todo(description);
            break;
        }
        case DEADLINE: {
            if (parts.length < 4) {
                throw new CherishException("Corrupted deadline data.");
            }
            LocalDateTime by = LocalDateTime.parse(parts[3], SAVE_FORMATTER);
            task = new Deadline(description, by);
            break;
        }
        case EVENT: {
            if (parts.length < 5) {
                throw new CherishException("Corrupted event data.");
            }
            LocalDateTime from = LocalDateTime.parse(parts[3], SAVE_FORMATTER);
            LocalDateTime to = LocalDateTime.parse(parts[4], SAVE_FORMATTER);
            task = new Event(description, from, to);
            break;
        }
        default: {
            throw new CherishException("Unknown task type.");
        }
        }

        if (isDone) {
            task.markAsDone();
        }

        return task;
    }
}
