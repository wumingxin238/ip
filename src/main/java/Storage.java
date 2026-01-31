import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Storage {
    private String filePath;
    private static final DateTimeFormatter SAVE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    // Load tasks from file
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
                if (line.trim().isEmpty()) continue;
                loadedTasks.add(parseTask(line));
            }
            return loadedTasks.toArray(new Task[0]);
        } catch (IOException e) {
            throw new CherishException("Failed to read from file.");
        }
    }

    // Save tasks to file
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

    // Helper: parse a single line into a Task
    private Task parseTask(String line) throws CherishException {
        String[] parts = line.split(" \\| ", -1);
        if (parts.length < 3) {
            throw new CherishException("Corrupted data format");
        }

        String typeSymbol = parts[0];
        boolean isDone = "1".equals(parts[1]);
        String description = parts[2];

        Task task = null;
        TaskType type = TaskType.fromSymbol(typeSymbol);

        switch (type) {
            case TODO:
                task = new Todo(description);
                break;
            case DEADLINE:
                if (parts.length < 4) throw new CherishException("Corrupted deadline data");
                LocalDateTime by = LocalDateTime.parse(parts[3], SAVE_FORMATTER);
                task = new Deadline(description, by);
                break;
            case EVENT:
                if (parts.length < 5) throw new CherishException("Corrupted event data");
                LocalDateTime from = LocalDateTime.parse(parts[3], SAVE_FORMATTER);
                LocalDateTime to = LocalDateTime.parse(parts[4], SAVE_FORMATTER);
                task = new Event(description, from, to);
                break;
        }

        if (isDone && task != null) {
            task.markAsDone();
        }
        return task;
    }
}