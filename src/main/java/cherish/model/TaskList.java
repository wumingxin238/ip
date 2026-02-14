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
     * Constructor for a new taskList
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
        assert tasks != null : "TaskList constructor argument 'tasks' cannot be null";
        if (loadedTasks != null) {
            for (Task task : loadedTasks) {
                if (task != null) {
                    tasks.add(task);
                }
            }
        }
    }

    /**
     * Add a task into the taskList
     */
    public void add(Task task) {
        int oldSize = tasks.size();
        tasks.add(task);
        assert tasks.size() == oldSize + 1 : "TaskList size did not increase by 1 after adding a task. Old size: "
                        + oldSize + ", New size: " + tasks.size();
    }

    /**
     * Adds a task at the specified index.
     *
     * @param index Position to insert the task (0 to size inclusive).
     * @param task Task to be added.
     * @throws CherishException If index is out of bounds.
     */
    public void addByIndex(int index, Task task) throws CherishException {
        if (index < 0 || index > tasks.size()) {
            throw new CherishException(
                    "Index out of bounds: " + index + ". Valid range is 0 to " + tasks.size()
            );
        }

        rebuildListWithInsertedTask(index, task);
    }

    /**
     * Removes and returns the last task from the task list.
     * @return The removed Task.
     * @throws CherishException If the task list is empty.
     */
    public Task pop() throws CherishException {
        if (tasks.isEmpty()) {
            throw new CherishException("Cannot pop from an empty task list.");
        }
        Task removedTask = tasks.remove(tasks.size() - 1);
        return removedTask;
    }

    public Task getByIndex(int index) {
        assert index >= 0 && index < tasks.size() : "Index out of bounds in TaskList.get: "
                + index + ". Size is: " + tasks.size();
        return tasks.get(index);
    }

    public int size() {
        return tasks.size();
    }

    /**
     * Delete a task of specified index
     */
    public void remove(int index) {
        assert index >= 0 && index < tasks.size() : "Index out of bounds in TaskList.remove: "
                + index + ". Size is: " + tasks.size();
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

    private void rebuildListWithInsertedTask(int index, Task task) {
        List<Task> updatedTasks =
                java.util.stream.IntStream.range(0, tasks.size() + 1)
                        .mapToObj(i -> {
                            if (i == index) {
                                return task;
                            }
                            return tasks.get(i < index ? i : i - 1);
                        })
                        .toList();

        tasks.clear();
        tasks.addAll(updatedTasks);
    }

}
