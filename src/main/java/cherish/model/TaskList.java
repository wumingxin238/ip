package cherish.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cherish.CherishException;

/**
 * Manages a list of tasks in the Cherish application.
 */
public class TaskList {
    public static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd yyyy HHmm", Locale.ENGLISH);

    private final List<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Load existing tasks
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
     * Finds tasks occurring on a given date.
     */
    public String findTasksOnDate(String dateString) throws CherishException {
        LocalDate targetDate;
        try {
            targetDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            throw new CherishException("Invalid date format! Please use yyyy-MM-dd (e.g., 2026-01-31).");
        }

        List<Task> matchingTasks = tasks.stream()
                .filter(task -> occursOnDate(task, targetDate))
                .toList();

        if (matchingTasks.isEmpty()) {
            return "No tasks found on " + dateString + ".";
        }

        StringBuilder result = new StringBuilder(
                "Here are the tasks on " + dateString + ":\n");

        for (int i = 0; i < matchingTasks.size(); i++) {
            result.append(i + 1)
                    .append(".")
                    .append(matchingTasks.get(i))
                    .append("\n");
        }

        return result.toString().trim();
    }

    /**
     * Finds tasks whose description contains the given keyword (case-insensitive).
     */
    public String findTasksByKeyword(String keyword) {
        String lowerKeyword = keyword.toLowerCase();

        List<Task> matchingTasks = tasks.stream()
                .filter(task -> task.getDescription()
                        .toLowerCase()
                        .contains(lowerKeyword))
                .toList();

        if (matchingTasks.isEmpty()) {
            return "No tasks found containing '" + keyword + "'.";
        }

        StringBuilder result =
                new StringBuilder("Here are the matching tasks in your list:\n");

        for (int i = 0; i < matchingTasks.size(); i++) {
            result.append(i + 1)
                    .append(".")
                    .append(matchingTasks.get(i))
                    .append("\n");
        }

        return result.toString().trim();
    }

    public Task[] toArray() {
        return tasks.toArray(new Task[0]);
    }

    public String getListString() {
        if (tasks.isEmpty()) {
            return "Your task list is empty! Add some tasks with 'todo', 'deadline', or 'event'.";
        }

        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(i + 1)
                    .append(".")
                    .append(tasks.get(i))
                    .append("\n");
        }
        return sb.toString().trim();
    }

    /**
     * Checks whether a task occurs on the given date.
     */
    private boolean occursOnDate(Task task, LocalDate targetDate) {
        if (task instanceof Deadline d) {
            return d.getBy().toLocalDate().equals(targetDate);
        }

        if (task instanceof Event e) {
            LocalDate from = e.getFrom().toLocalDate();
            LocalDate to = e.getTo().toLocalDate();
            return !from.isAfter(targetDate) && !to.isBefore(targetDate);
        }

        return false;
    }
}
