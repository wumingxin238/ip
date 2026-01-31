package cherish.model;

import cherish.CherishException;

public class Todo extends Task {
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