public abstract class Command {
    public abstract String execute(TaskList tasks, Ui ui, Storage storage) throws CherishException;
    public boolean isExit() {
        return false;
    }
}