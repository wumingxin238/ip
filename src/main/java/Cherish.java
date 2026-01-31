import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Cherish {
    private static final String DATA_DIR = "data";
    private static final String DATA_FILE = "cherish.txt";
    private static final String FILE_PATH = DATA_DIR + File.separator + DATA_FILE;

    // Formatters
    private static final DateTimeFormatter SAVE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy HHmm");

    private static Task[] tasks = new Task[100];
    private static int taskCount = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Cherish ch = new Cherish();

        // Load existing tasks on startup
        ch.loadTasksFromFile();
        ch.greet();

        while (true) {
            try {
                String input = sc.nextLine();

                if (input.equals("bye")) {
                    break;
                } else if (input.equals("list")) {
                    ch.printTaskList();
                } else if (input.startsWith("mark ")) {
                    ch.handleMarkCommand(input);
                    ch.saveTasksToFile();
                } else if (input.startsWith("unmark ")) {
                    ch.handleUnmarkCommand(input);
                    ch.saveTasksToFile();
                } else if (input.startsWith("delete ")) {
                    ch.handleDeleteCommand(input);
                    ch.saveTasksToFile();
                } else if (input.startsWith("todo ")) {
                    ch.handleTodoCommand(input);
                    ch.saveTasksToFile();
                } else if (input.startsWith("deadline ")) {
                    ch.handleDeadlineCommand(input);
                    ch.saveTasksToFile();
                } else if (input.startsWith("event ")) {
                    ch.handleEventCommand(input);
                    ch.saveTasksToFile();
                } else if (input.startsWith("finddate ")) {
                    ch.handleFindDateCommand(input);
                } else if (input.trim().isEmpty()) {
                    System.out.println("Hmm... you didn't type anything! Try a command like 'todo read book'.\n");
                } else {
                    System.out.println("I don't recognize that command! Try 'todo', 'deadline', 'event', 'list', 'mark', 'unmark', 'delete', or 'finddate'.\n");
                }
            } catch (CherishException e) {
                System.out.println("Oops! " + e.getMessage() + "\n");
            } catch (NumberFormatException e) {
                System.out.println("Invalid task number! Please enter a valid number (e.g., 'delete 1').\n");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Task list is full! You can only have up to 100 tasks.\n");
            } catch (Exception e) {
                System.out.println("Something unexpected happened! Please try again.\n");
            }
        }
        ch.exit();
        sc.close();
    }

    public void greet() {
        System.out.println("Hello! I'm Cherish\n" +
                "What can I do for you?\n");
    }

    public void exit() {
        System.out.println(" Bye. Hope to see you again soon!\n");
    }

    // Helper method to validate and get task index
    private int getValidTaskIndex(String input, String commandName) throws CherishException {
        try {
            int index = Integer.parseInt(input.substring(commandName.length()).trim()) - 1;
            if (index < 0 || index >= taskCount) {
                throw new CherishException("Task number out of range! You have " + taskCount + " tasks. Please choose a number between 1 and " + taskCount + ".");
            }
            return index;
        } catch (NumberFormatException e) {
            throw new CherishException("Invalid task number! Please enter a valid number after '" + commandName + "' (e.g., '" + commandName + " 1').");
        }
    }

    // ==================== FILE PERSISTENCE METHODS ====================

    private void loadTasksFromFile() {
        Path dataDir = Paths.get(DATA_DIR);
        Path filePath = Paths.get(FILE_PATH);

        if (!Files.exists(dataDir)) {
            try {
                Files.createDirectories(dataDir);
                return;
            } catch (IOException e) {
                System.out.println("Warning: Could not create data directory. Tasks will not be saved.\n");
                return;
            }
        }

        if (!Files.exists(filePath)) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                parseAndAddTask(line);
            }
        } catch (IOException e) {
            System.out.println("Warning: Could not read task data file. Starting with empty list.\n");
        } catch (CherishException e) {
            System.out.println("Warning: Corrupted data in file. Starting with empty list.\n");
        }
    }

    private void parseAndAddTask(String line) throws CherishException {
        String[] parts = line.split(" \\| ", -1);

        if (parts.length < 3) {
            throw new CherishException("Corrupted data format");
        }

        String typeSymbol = parts[0];
        boolean isDone = "1".equals(parts[1]);
        String description = parts[2];

        try {
            TaskType type = TaskType.fromSymbol(typeSymbol);

            switch (type) {
                case TODO:
                    if (taskCount < tasks.length) {
                        tasks[taskCount] = new Todo(description);
                        if (isDone) tasks[taskCount].markAsDone();
                        taskCount++;
                    }
                    break;

                case DEADLINE:
                    if (parts.length < 4) {
                        throw new CherishException("Corrupted deadline data");
                    }
                    String byStr = parts[3];
                    try {
                        LocalDateTime by = LocalDateTime.parse(byStr, SAVE_FORMATTER);
                        if (taskCount < tasks.length) {
                            tasks[taskCount] = new Deadline(description, by);
                            if (isDone) tasks[taskCount].markAsDone();
                            taskCount++;
                        }
                    } catch (DateTimeParseException e) {
                        throw new CherishException("Invalid date in deadline data");
                    }
                    break;

                case EVENT:
                    if (parts.length < 5) {
                        throw new CherishException("Corrupted event data");
                    }
                    String fromStr = parts[3];
                    String toStr = parts[4];
                    try {
                        LocalDateTime from = LocalDateTime.parse(fromStr, SAVE_FORMATTER);
                        LocalDateTime to = LocalDateTime.parse(toStr, SAVE_FORMATTER);
                        if (taskCount < tasks.length) {
                            tasks[taskCount] = new Event(description, from, to);
                            if (isDone) tasks[taskCount].markAsDone();
                            taskCount++;
                        }
                    } catch (DateTimeParseException e) {
                        throw new CherishException("Invalid date in event data");
                    }
                    break;
            }
        } catch (IllegalArgumentException e) {
            throw new CherishException("Unknown task type in data file");
        }
    }

    private void saveTasksToFile() {
        Path dataDir = Paths.get(DATA_DIR);
        Path filePath = Paths.get(FILE_PATH);

        if (!Files.exists(dataDir)) {
            try {
                Files.createDirectories(dataDir);
            } catch (IOException e) {
                System.out.println("Warning: Could not create data directory. Tasks not saved.\n");
                return;
            }
        }

        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (int i = 0; i < taskCount; i++) {
                writer.write(tasks[i].toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Warning: Failed to save tasks to file.\n");
        }
    }

    // ==================== FIND DATE COMMAND ====================

    private void handleFindDateCommand(String input) throws CherishException {
        String dateStr = input.substring(9).trim();
        if (dateStr.isEmpty()) {
            throw new CherishException("Please specify a date to search! Usage: finddate yyyy-MM-dd");
        }

        LocalDateTime targetDate;
        try {
            // Parse as date-only (assume time 00:00)
            targetDate = LocalDateTime.parse(dateStr + " 0000", SAVE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new CherishException("Invalid date format! Please use 'yyyy-MM-dd' (e.g., 2019-12-02).");
        }

        // Find matching tasks
        java.util.ArrayList<Task> matchingTasks = new java.util.ArrayList<>();
        for (int i = 0; i < taskCount; i++) {
            Task task = tasks[i];
            if (task instanceof Deadline) {
                Deadline d = (Deadline) task;
                if (d.getBy().toLocalDate().equals(targetDate.toLocalDate())) {
                    matchingTasks.add(task);
                }
            } else if (task instanceof Event) {
                Event e = (Event) task;
                // Check if event occurs on the target date (overlaps)
                if (!e.getFrom().toLocalDate().isAfter(targetDate.toLocalDate()) &&
                        !e.getTo().toLocalDate().isBefore(targetDate.toLocalDate())) {
                    matchingTasks.add(task);
                }
            }
        }

        // Display results
        if (matchingTasks.isEmpty()) {
            System.out.println("No deadlines or events found for " +
                    targetDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ".\n");
        } else {
            System.out.println("Here are the deadlines/events on " +
                    targetDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ":");
            for (int i = 0; i < matchingTasks.size(); i++) {
                System.out.println((i + 1) + "." + matchingTasks.get(i));
            }
            System.out.println();
        }
    }

    // ==================== COMMAND HANDLERS ====================

    private void handleTodoCommand(String input) throws CherishException {
        String description = input.substring(5).trim();
        if (taskCount >= tasks.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        tasks[taskCount] = new Todo(description);
        System.out.println("Got it! I've added this task:");
        System.out.println("  " + tasks[taskCount]);
        taskCount++;
        System.out.println("Now you have " + taskCount + (taskCount == 1 ? " task" : " tasks") + " in your list.\n");
    }

    private void handleDeadlineCommand(String input) throws CherishException {
        String[] parts = input.split(" /by ", 2);
        if (parts.length != 2) {
            throw new CherishException("Invalid deadline format! Please use: 'deadline DESCRIPTION /by yyyy-MM-dd HHmm'");
        }
        String description = parts[0].substring(9).trim();
        String by = parts[1].trim();

        if (taskCount >= tasks.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        tasks[taskCount] = new Deadline(description, by);
        System.out.println("Got it! I've added this task:");
        System.out.println("  " + tasks[taskCount]);
        taskCount++;
        System.out.println("Now you have " + taskCount + (taskCount == 1 ? " task" : " tasks") + " in your list.\n");
    }

    private void handleEventCommand(String input) throws CherishException {
        String[] parts = input.split(" /from ", 2);
        if (parts.length != 2) {
            throw new CherishException("Invalid event format! Please use: 'event DESCRIPTION /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm'");
        }
        String description = parts[0].substring(6).trim();
        String[] fromTo = parts[1].split(" /to ", 2);
        if (fromTo.length != 2) {
            throw new CherishException("Invalid event format! Please use: 'event DESCRIPTION /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm'");
        }
        String from = fromTo[0].trim();
        String to = fromTo[1].trim();

        if (taskCount >= tasks.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        tasks[taskCount] = new Event(description, from, to);
        System.out.println("Got it! I've added this task:");
        System.out.println("  " + tasks[taskCount]);
        taskCount++;
        System.out.println("Now you have " + taskCount + (taskCount == 1 ? " task" : " tasks") + " in your list.\n");
    }

    private void printTaskList() {
        if (taskCount == 0) {
            System.out.println("Your task list is empty! Add some tasks with 'todo', 'deadline', or 'event'.\n");
            return;
        }
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + "." + tasks[i]);
        }
        System.out.println();
    }

    private void handleMarkCommand(String input) throws CherishException {
        int index = getValidTaskIndex(input, "mark");
        tasks[index].markAsDone();
        System.out.println("Great! I've marked this task as done:");
        System.out.println("  " + tasks[index] + "\n");
    }

    private void handleUnmarkCommand(String input) throws CherishException {
        int index = getValidTaskIndex(input, "unmark");
        tasks[index].markAsNotDone();
        System.out.println("Okay! I've marked this task as not done yet:");
        System.out.println("  " + tasks[index] + "\n");
    }

    private void handleDeleteCommand(String input) throws CherishException {
        int index = getValidTaskIndex(input, "delete");
        Task deletedTask = tasks[index];

        for (int i = index; i < taskCount - 1; i++) {
            tasks[i] = tasks[i + 1];
        }
        tasks[taskCount - 1] = null;
        taskCount--;

        System.out.println("Noted! I've removed this task:");
        System.out.println("  " + deletedTask);
        System.out.println("Now you have " + taskCount + (taskCount == 1 ? " task" : " tasks") + " in your list.\n");
    }
}