class Deadline extends Task {
    protected String by;

    public Deadline(String description, String by) throws CherishException {
        super(description);
        if (by == null || by.trim().isEmpty()) {
            throw new CherishException("Deadline time cannot be empty! Please specify when the task is due using '/by TIME'.");
        }
        this.by = by.trim();
    }

    @Override
    public String getType() {
        return "D";
    }

    @Override
    public String toString() {
        return "[" + getType() + "][" + getStatusIcon() + "] " + description + " (by: " + by + ")";
    }
}