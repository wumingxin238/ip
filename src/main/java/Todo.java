class Todo extends Task {
    public Todo(String description) throws CherishException {
        super(description);
    }

    @Override
    public TaskType getType() {
        return TaskType.TODO;
    }
}