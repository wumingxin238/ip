import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

class Deadline extends Task {
    protected LocalDateTime by;

    // Constructor for user input (parses string)
    public Deadline(String description, String byStr) throws CherishException {
        super(description);
        try {
            this.by = LocalDateTime.parse(byStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        } catch (DateTimeParseException e) {
            throw new CherishException("Invalid date/time format! Please use 'yyyy-MM-dd HHmm' (e.g., 2019-12-02 1800).");
        }
    }

    // Constructor for loading from file (uses stored string)
    public Deadline(String description, LocalDateTime by) throws CherishException {
        super(description);
        this.by = by;
    }

    @Override
    public TaskType getType() {
        return TaskType.DEADLINE;
    }

    @Override
    public String toString() {
        String formattedDate = by.format(DateTimeFormatter.ofPattern("MMM dd yyyy HHmm"));
        return "[" + getType().getSymbol() + "][" + getStatusIcon() + "] " + description + " (by: " + formattedDate + ")";
    }

    @Override
    public String toFileString() {
        String savedDate = by.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + savedDate;
    }

    public LocalDateTime getBy() {
        return by;
    }
}