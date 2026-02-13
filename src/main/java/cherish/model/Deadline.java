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

    private static final DateTimeFormatter INPUT_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    private static final DateTimeFormatter DISPLAY_FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd yyyy HHmm");

    private static final String FILE_DATE_PATTERN = "yyyy-MM-dd HHmm";

    protected final LocalDateTime by;

    /**
     * Constructs a Deadline task from user input.
     *
     * @param description Description of the deadline task.
     * @param byStr Deadline date and time in yyyy-MM-dd HHmm format.
     * @throws CherishException If the date/time string is invalid.
     */
    public Deadline(String description, String byStr) throws CherishException {
        super(description);
        this.by = parseDateTime(byStr);
    }

    /**
     * Constructs a Deadline task from stored data.
     *
     * @param description Description of the deadline task.
     * @param by Deadline date and time.
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
        return super.toString()
                + " (by: "
                + by.format(DISPLAY_FORMATTER)
                + ")";
    }

    @Override
    public String toFileString() {
        return "D | "
                + (isDone ? "1" : "0")
                + " | "
                + description
                + " | "
                + by.format(INPUT_FORMATTER);
    }

    /**
     * Returns the deadline date and time.
     */
    public LocalDateTime getBy() {
        return by;
    }

    /* =========================
       Helper methods
       ========================= */

    private static LocalDateTime parseDateTime(String byStr) throws CherishException {
        try {
            return LocalDateTime.parse(byStr, INPUT_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new CherishException(
                    "Invalid date/time format! Please use 'yyyy-MM-dd HHmm' "
                            + "(e.g., 2019-12-02 1800)."
            );
        }
    }
}
