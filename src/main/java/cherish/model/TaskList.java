package cherish.model;

import cherish.CherishException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    // Constructor for loading from Storage
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

    // helper method to find tasks occurring on a given date
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
                // Include if the event spans the target date
                if (!from.isAfter(targetDate) && !to.isBefore(targetDate)) {
                    if (!found) {
                        result.append("Here are the tasks on ").append(dateString).append(":\n");
                        found = true;
                    }
                    result.append(++count).append(".").append(task).append("\n");
                }
            }
            // Todo has no date → skip
        }

        if (!found) {
            return "No tasks found on " + dateString + ".";
        }

        return result.toString().trim();
    }

    // For saving: convert to array
    public Task[] toArray() {
        return tasks.toArray(new Task[0]);
    }

    // For display
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