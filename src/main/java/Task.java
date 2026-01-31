abstract class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) throws CherishException {
        if (description == null || description.trim().isEmpty()) {
            throw new CherishException("Task description cannot be empty! Please provide a valid description.");
        }
        this.description = description.trim();
        this.isDone = false;
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsNotDone() {
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    // For saving to file
    public abstract String toFileString();

    // Return the enum instead of a string
    public abstract TaskType getType();

    @Override
    public String toString() {
        return "[" + getType().getSymbol() + "][" + getStatusIcon() + "] " + description;
    }
}