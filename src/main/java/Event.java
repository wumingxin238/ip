class Event extends Task {
    protected String from;
    protected String to;

    public Event(String description, String from, String to) throws CherishException {
        super(description);
        if (from == null || from.trim().isEmpty()) {
            throw new CherishException("Event start time cannot be empty! Please specify when it starts using '/from TIME'.");
        }
        if (to == null || to.trim().isEmpty()) {
            throw new CherishException("Event end time cannot be empty! Please specify when it ends using '/to TIME'.");
        }
        this.from = from.trim();
        this.to = to.trim();
    }

    @Override
    public TaskType getType() {
        return TaskType.EVENT;
    }

    @Override
    public String toString() {
        return "[" + getType().getSymbol() + "][" + getStatusIcon() + "] " + description + " (from: " + from + " to: " + to + ")";
    }
}