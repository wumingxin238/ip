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

    public abstract String getType();

    @Override
    public String toString() {
        return "[" + getType() + "][" + getStatusIcon() + "] " + description;
    }
}