package cherish.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Locale;

import cherish.CherishException;

/**
 * Manages a list of tasks in the Cherish application.
 * Provides methods for adding, retrieving, removing, marking,
 * and searching tasks.
 */
public class TaskList {

    public static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd yyyy HHmm", Locale.ENGLISH);

    private static final DateTimeFormatter INPUT_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final ArrayList<Task> tasks;

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a TaskList from an existing array of tasks.
     *
     * @param loadedTasks Tasks loaded from storage.
     */
    public TaskList(Task[] loadedTasks) {
        this();
        if (loadedTasks == null) {
            return;
        }

        for (Task task : loadedTasks) {
            if (task != null) {
                tasks.add(task);
            }
        }
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    public int size() {
        return tasks.size();
    }

    public void remove(int index) {
        tasks.remove(index);
    }

    public void markAsDone(int index) {
        tasks.get(index).markAsDone();
    }

    public void markAsNotDone(int index) {
        tasks.get(index).markAsNotDone();
    }

    /**
     * Finds tasks that occur on the given date.
     *
     * @param dateString Date in yyyy-MM-dd format.
     * @return Formatted list of matching tasks.
     * @throws CherishException If date format is invalid.
     */
    public String findTasksOnDate(String dateString) throws CherishException {
        LocalDate targetDate = parseDate(dateString);

        StringBuilder result = new StringBuilder();
        int matchCount = 0;

        for (Task task : tasks) {
            if (!occursOnDate(task, targetDate)) {
                continue;
            }

            if (matchCount == 0) {
                result.append("Here are the tasks on ")
                        .append(dateString)
                        .append(":\n");
            }

            result.append(++matchCount)
                    .append(".")
                    .append(task)
                    .append("\n");
        }

        if (matchCount == 0) {
            return "No tasks found on " + dateString + ".";
        }

        return result.toString().trim();
    }

    /**
     * Finds tasks whose description contains the given keyword.
     *
     * @param keyword Keyword to search for.
     * @return Formatted list of matching tasks.
     */
    public String findTasksByKeyword(String keyword) {
        StringBuilder result = new StringBuilder();
        int matchCount = 0;

        String lowerKeyword = keyword.toLowerCase();

        for (Task task : tasks) {
            if (!task.getDescription().toLowerCase().contains(lowerKeyword)) {
                continue;
            }

            if (matchCount == 0) {
                result.append("Here are the matching tasks in your list:\n");
            }

            result.append(++matchCount)
                    .append(".")
                    .append(task)
                    .append("\n");
        }

        if (matchCount == 0) {
            return "No tasks found containing '" + keyword + "'.";
        }

        return result.toString().trim();
    }

    /**
     * Converts the task list to an array.
     */
    public Task[] toArray() {
        return tasks.toArray(new Task[0]);
    }

    /**
     * Returns a formatted string of all tasks.
     */
    public String getListString() {
        if (tasks.isEmpty()) {
            return "Your task list is empty! Add some tasks with 'todo', 'deadline', or 'event'.";
        }

        StringBuilder result = new StringBuilder("Here are the tasks in your list:\n");

        for (int i = 0; i < tasks.size(); i++) {
            result.append(i + 1)
                    .append(".")
                    .append(tasks.get(i))
                    .append("\n");
        }

        return result.toString().trim();
    }

    /* =========================
       Helper methods
       ========================= */

    private LocalDate parseDate(String dateString) throws CherishException {
        try {
            return LocalDate.parse(dateString, INPUT_DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new CherishException(
                    "Invalid date format! Please use yyyy-MM-dd (e.g., 2026-01-31)."
            );
        }
    }

    private boolean occursOnDate(Task task, LocalDate targetDate) {
        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return deadline.getBy().toLocalDate().equals(targetDate);
        }

        if (task instanceof Event) {
            Event event = (Event) task;
            LocalDate from = event.getFrom().toLocalDate();
            LocalDate to = event.getTo().toLocalDate();
            return !from.isAfter(targetDate) && !to.isBefore(targetDate);
        }

        return false;
    }
}
