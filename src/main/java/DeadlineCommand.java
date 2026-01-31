import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DeadlineCommand extends Command {
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private String description;
    private String byString;

    public DeadlineCommand(String description, String byString) {
        this.description = description;
        this.byString = byString;
    }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws CherishException {
        LocalDateTime by;
        try {
            by = LocalDateTime.parse(byString, INPUT_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new CherishException("Invalid date/time format for deadline! Use: yyyy-MM-dd HHmm (e.g., 2026-01-31 1800)");
        }

        Deadline deadline = new Deadline(description, by);
        tasks.add(deadline);

        // Save to file immediately after adding
        try {
            storage.save(tasks.toArray());
        } catch (CherishException e) {
            // Optional: notify user save failed, but don't break flow
            // For now, we'll let it fail silently or rethrow if critical
            // You may choose to show a warning in UI later
        }

        return "Got it! I've added this task:\n  " + deadline +
                "\nNow you have " + tasks.size() + (tasks.size() == 1 ? " task" : " tasks") + " in your list.";
    }
}