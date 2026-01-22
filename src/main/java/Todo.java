class Todo extends Task {
    public Todo(String description) throws CherishException {
        super(description);
    }

    @Override
    public String getType() {
        return "T";
    }
}