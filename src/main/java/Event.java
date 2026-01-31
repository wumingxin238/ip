import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

    // Constructor for user input
    public Event(String description, String fromStr, String toStr) throws CherishException {
        super(description);
        try {
            this.from = LocalDateTime.parse(fromStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            this.to = LocalDateTime.parse(toStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        } catch (DateTimeParseException e) {
            throw new CherishException("Invalid date/time format! Please use 'yyyy-MM-dd HHmm' for both start and end times.");
        }
    }

    // Constructor for loading from file
    public Event(String description, LocalDateTime from, LocalDateTime to) throws CherishException {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public TaskType getType() {
        return TaskType.EVENT;
    }

    @Override
    public String toString() {
        String formattedFrom = from.format(DateTimeFormatter.ofPattern("MMM dd yyyy HHmm"));
        String formattedTo = to.format(DateTimeFormatter.ofPattern("MMM dd yyyy HHmm"));
        return "[" + getType().getSymbol() + "][" + getStatusIcon() + "] " + description + " (from: " + formattedFrom + " to: " + formattedTo + ")";
    }

    @Override
    public String toFileString() {
        String savedFrom = from.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        String savedTo = to.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        return "E | " + (isDone ? "1" : "0") + " | " + description + " | " + savedFrom + " | " + savedTo;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public LocalDateTime getTo() {
        return to;
    }
}
