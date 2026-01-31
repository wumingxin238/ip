package cherish.model;

import cherish.CherishException;

/**
 * Represents a simple Todo task in the Cherish application.
 * A Todo task has only a description and no specific date or time.
 */
public class Todo extends Task {
    /**
     * Constructs a Todo task with the given description.
     *
     * @param description The description of the todo task.
     * @throws CherishException If the description is null or empty.
     */
    public Todo(String description) throws CherishException {
        super(description);
    }

    @Override
    public TaskType getType() {
        return TaskType.TODO;
    }

    @Override
    public String toFileString() {
        return "T | " + (isDone ? "1" : "0") + " | " + description;
    }
}