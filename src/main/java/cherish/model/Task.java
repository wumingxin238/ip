package cherish.model;

import cherish.CherishException;

/**
 * Abstract base class for different types of tasks in the Cherish application.
 * Contains common properties like description and completion status.
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Constructs a Task with the given description.
     * Initializes the task as not done.
     *
     * @param description The description of the task. Cannot be null or empty.
     * @throws CherishException If the description is null or blank.
     */
    public Task(String description) throws CherishException {
        if (description == null || description.trim().isEmpty()) {
            throw new CherishException("Task description cannot be empty! Please provide a valid description.");
        }
        this.description = description.trim();
        this.isDone = false;
    }

    /**
     * Gets the description of the task.
     *
     * @return The task's description string.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Marks the task as completed.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks the task as not completed.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Gets the icon representing the task's completion status.
     *
     * @return "X" if the task is done, " " (space) otherwise.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Checks if the task is completed.
     *
     * @return True if the task is done, false otherwise.
     */
    public boolean isDone() {
        return isDone;
    }

    // For saving to file
    /**
     * Returns a string representation of the task suitable for saving to a file.
     *
     * @return A formatted string containing the task type, status, and details.
     */
    public abstract String toFileString();

    // Return the enum instead of a string
    /**
     * Returns the type of this task.
     *
     * @return The TaskType enum value representing the task's type (TODO, DEADLINE, EVENT).
     */
    public abstract TaskType getType();

    @Override
    public String toString() {
        return "[" + getType().getSymbol() + "][" + getStatusIcon() + "] " + description;
    }
}
