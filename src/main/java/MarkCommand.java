public class MarkCommand extends Command {
    private int index;

    public MarkCommand(int index) {
        this.index = index;
    }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage s) throws CherishException {
        if (index >= tasks.size()) {
            throw new CherishException("Task number out of range! You have " + tasks.size() + " tasks.");
        }
        tasks.markAsDone(index);
        return "Great! I've marked this task as done:\n  " + tasks.get(index);
    }

}