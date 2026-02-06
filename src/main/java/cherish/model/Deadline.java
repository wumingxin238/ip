package cherish.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import cherish.CherishException;

/**
 * Represents a Deadline task in the Cherish application.
 * A Deadline has a description and a specific date and time by which it must be completed.
 */
public class Deadline extends Task {
    protected LocalDateTime by;

    /**
     * Constructs a Deadline task from a description string and a date/time string.
     * This constructor is typically used when parsing user input.
     *
     * @param description The description of the deadline task.
     * @param byStr The deadline date and time string in the format "yyyy-MM-dd HHmm".
     * @throws CherishException If the date/time string is in an invalid format.
     */
    public Deadline(String description, String byStr) throws CherishException {
        super(description);
        try {
            this.by = LocalDateTime.parse(byStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        } catch (DateTimeParseException e) {
            throw new CherishException("Invalid date/time format!"
                    + " Please use 'yyyy-MM-dd HHmm'"
                    + " (e.g., 2019-12-02 1800).");
        }
    }

    /**
     * Constructs a Deadline task from a description string and a LocalDateTime object.
     * This constructor is typically used when loading tasks from a file.
     *
     * @param description The description of the deadline task.
     * @param by The LocalDateTime object representing the deadline.
     * @throws CherishException If any required parameters are missing or invalid (not applicable here).
     */
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
        return super.toString() + " (by: " + formattedDate + ")";
    }

    @Override
    public String toFileString() {
        String savedDate = by.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + savedDate;
    }

    /**
     * Gets the deadline date and time.
     *
     * @return The LocalDateTime object representing the deadline.
     */
    public LocalDateTime getBy() {
        return by;
    }
}
