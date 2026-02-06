package cherish.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Locale;

import cherish.CherishException;

/**
 * Manages a list of tasks in the Cherish application.
 * Provides methods for adding, retrieving, removing, and marking tasks,
 * as well as finding tasks by date and generating display strings.
 */
public class TaskList {
    public static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd yyyy HHmm", Locale.ENGLISH);
    private ArrayList<Task> tasks;
    // The formatter is correctly set with Locale.ENGLISH

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a TaskList from an existing array of tasks.
     * Used primarily when loading tasks from storage.
     *
     * @param loadedTasks An array of Task objects to initialize the list with.
     */
    public TaskList(Task[] loadedTasks) {
        this();
        if (loadedTasks != null) {
            for (Task task : loadedTasks) {
                if (task != null) {
                    tasks.add(task);
                }
            }
        }
    }

    /**
     * Adds a new task to the end of the list.
     *
     * @param task The Task object to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Retrieves the task at the specified index.
     *
     * @param index The zero-based index of the task to retrieve.
     * @return The Task object at the specified index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Gets the number of tasks in the list.
     *
     * @return The current size of the task list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Removes the task at the specified index.
     *
     * @param index The zero-based index of the task to remove.
     */
    public void remove(int index) {
        tasks.remove(index);
    }

    /**
     * Marks the task at the specified index as done.
     *
     * @param index The zero-based index of the task to mark.
     */
    public void markAsDone(int index) {
        tasks.get(index).markAsDone();
    }

    /**
     * Marks the task at the specified index as not done.
     *
     * @param index The zero-based index of the task to mark.
     */
    public void markAsNotDone(int index) {
        tasks.get(index).markAsNotDone();
    }

    // Helper method to find tasks occurring on a given date
    /**
     * Finds and returns a string listing all tasks (Deadlines and Events) that occur on a specific date.
     * For Events, it includes those that span the target date.
     *
     * @param dateString The date string in the format "yyyy-MM-dd" (e.g., "2026-02-01").
     * @return A formatted string listing the matching tasks, or a message indicating none were found.
     * @throws CherishException If the provided date string is in an invalid format.
     */
    public String findTasksOnDate(String dateString) throws CherishException {
        LocalDate targetDate;
        try {
            targetDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            throw new CherishException("Invalid date format! Please use yyyy-MM-dd (e.g., 2026-01-31).");
        }

        StringBuilder result = new StringBuilder();
        boolean found = false;
        int count = 0;

        for (Task task : tasks) {
            if (task instanceof Deadline) {
                Deadline d = (Deadline) task;
                // Check if the deadline date matches the target date
                if (d.getBy().toLocalDate().equals(targetDate)) {
                    if (!found) {
                        result.append("Here are the tasks on ").append(dateString).append(":\n");
                        found = true;
                    }
                    result.append(++count).append(".").append(task).append("\n");
                }
            } else if (task instanceof Event) {
                Event e = (Event) task;
                LocalDate from = e.getFrom().toLocalDate();
                LocalDate to = e.getTo().toLocalDate();
                // Include if the event spans the target date (inclusive)
                if (!from.isAfter(targetDate) && !to.isBefore(targetDate)) {
                    if (!found) {
                        result.append("Here are the tasks on ").append(dateString).append(":\n");
                        found = true;
                    }
                    result.append(++count).append(".").append(task).append("\n");
                }
            }
        }

        if (!found) {
            return "No tasks found on " + dateString + ".";
        }

        return result.toString().trim();
    }

    // Helper method to find tasks by keyword in description
    /**
     * Finds and returns a string listing all tasks whose description contains the given keyword.
     * The search is case-insensitive.
     *
     * @param keyword The keyword to search for within task descriptions.
     * @return A formatted string listing the matching tasks, or a message indicating none were found.
     */
    public String findTasksByKeyword(String keyword) {
        StringBuilder result = new StringBuilder();
        boolean found = false;
        int count = 0;

        // Convert keyword to lowercase for case-insensitive comparison
        String lowerKeyword = keyword.toLowerCase();

        for (Task task : tasks) {
            // Check if the task's description contains the keyword
            if (task.getDescription().toLowerCase().contains(lowerKeyword)) {
                if (!found) {
                    result.append("Here are the matching tasks in your list:\n");
                    found = true;
                }
                result.append(++count).append(".").append(task).append("\n");
            }
        }

        if (!found) {
            return "No tasks found containing '" + keyword + "'.";
        }

        return result.toString().trim();
    }

    // For saving: convert to array
    /**
     * Converts the internal list of tasks into an array.
     *
     * @return An array of Task objects.
     */
    public Task[] toArray() {
        return tasks.toArray(new Task[0]);
    }

    // For display
    /**
     * Generates a formatted string representation of the entire task list for display purposes.
     *
     * @return A string listing all tasks with their indices, or a message if the list is empty.
     */
    public String getListString() {
        if (tasks.isEmpty()) {
            return "Your task list is empty! Add some tasks with 'todo', 'deadline', or 'event'.";
        }
        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append((i + 1)).append(".").append(tasks.get(i)).append("\n");
        }
        return sb.toString().trim();
    }
}
