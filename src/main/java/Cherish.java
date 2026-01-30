import java.util.Scanner;

public class Cherish {
    private static Task[] tasks = new Task[100];
    private static int taskCount = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Cherish ch = new Cherish();
        ch.greet();

        while (true) {
            try {
                String input = sc.nextLine();

                if (input.equals("bye")) {
                    break;
                } else if (input.equals("list")) {
                    ch.printTaskList();
                } else if (input.startsWith("mark ")) {
                    ch.handleMarkCommand(input);
                } else if (input.startsWith("unmark ")) {
                    ch.handleUnmarkCommand(input);
                } else if (input.startsWith("delete ")) {
                    ch.handleDeleteCommand(input);
                } else if (input.startsWith("todo ")) {
                    ch.handleTodoCommand(input);
                } else if (input.startsWith("deadline ")) {
                    ch.handleDeadlineCommand(input);
                } else if (input.startsWith("event ")) {
                    ch.handleEventCommand(input);
                } else if (input.trim().isEmpty()) {
                    System.out.println("Hmm... you didn't type anything! Try a command like 'todo read book'.\n");
                } else {
                    System.out.println("I don't recognize that command! Try 'todo', 'deadline', 'event', 'list', 'mark', 'unmark', or 'delete'.\n");
                }
            } catch (CherishException e) {
                System.out.println("Oops! " + e.getMessage() + "\n");
            } catch (NumberFormatException e) {
                System.out.println("Invalid task number! Please enter a valid number (e.g., 'delete 1').\n");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Task list is full! You can only have up to 100 tasks.\n");
            } catch (Exception e) {
                System.out.println("Something unexpected happened! Please try again.\n");
            }
        }
        ch.exit();
        sc.close();
    }

    public void greet() {
        System.out.println("Hello! I'm Cherish\n" +
                "What can I do for you?\n");
    }

    public void exit() {
        System.out.println(" Bye. Hope to see you again soon!\n");
    }

    // Helper method to validate and get task index
    private int getValidTaskIndex(String input, String commandName) throws CherishException {
        try {
            int index = Integer.parseInt(input.substring(commandName.length()).trim()) - 1;
            if (index < 0 || index >= taskCount) {
                throw new CherishException("Task number out of range! You have " + taskCount + " tasks. Please choose a number between 1 and " + taskCount + ".");
            }
            return index;
        } catch (NumberFormatException e) {
            throw new CherishException("Invalid task number! Please enter a valid number after '" + commandName + "' (e.g., '" + commandName + " 1').");
        }
    }

    private void handleTodoCommand(String input) throws CherishException {
        String description = input.substring(5).trim();
        if (taskCount >= tasks.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        tasks[taskCount] = new Todo(description);
        System.out.println("Got it! I've added this task:");
        System.out.println("  " + tasks[taskCount]);
        taskCount++;
        System.out.println("Now you have " + taskCount + (taskCount == 1 ? " task" : " tasks") + " in your list.\n");
    }

    private void handleDeadlineCommand(String input) throws CherishException {
        String[] parts = input.split(" /by ", 2);
        if (parts.length != 2) {
            throw new CherishException("Invalid deadline format! Please use: 'deadline DESCRIPTION /by DEADLINE'");
        }
        String description = parts[0].substring(9).trim();
        String by = parts[1].trim();

        if (taskCount >= tasks.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        tasks[taskCount] = new Deadline(description, by);
        System.out.println("Got it! I've added this task:");
        System.out.println("  " + tasks[taskCount]);
        taskCount++;
        System.out.println("Now you have " + taskCount + (taskCount == 1 ? " task" : " tasks") + " in your list.\n");
    }

    private void handleEventCommand(String input) throws CherishException {
        String[] parts = input.split(" /from ", 2);
        if (parts.length != 2) {
            throw new CherishException("Invalid event format! Please use: 'event DESCRIPTION /from START /to END'");
        }
        String description = parts[0].substring(6).trim();
        String[] fromTo = parts[1].split(" /to ", 2);
        if (fromTo.length != 2) {
            throw new CherishException("Invalid event format! Please use: 'event DESCRIPTION /from START /to END'");
        }
        String from = fromTo[0].trim();
        String to = fromTo[1].trim();

        if (taskCount >= tasks.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        tasks[taskCount] = new Event(description, from, to);
        System.out.println("Got it! I've added this task:");
        System.out.println("  " + tasks[taskCount]);
        taskCount++;
        System.out.println("Now you have " + taskCount + (taskCount == 1 ? " task" : " tasks") + " in your list.\n");
    }

    private void printTaskList() {
        if (taskCount == 0) {
            System.out.println("Your task list is empty! Add some tasks with 'todo', 'deadline', or 'event'.\n");
            return;
        }
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + "." + tasks[i]);
        }
        System.out.println();
    }

    private void handleMarkCommand(String input) throws CherishException {
        int index = getValidTaskIndex(input, "mark");
        tasks[index].markAsDone();
        System.out.println("Great! I've marked this task as done:");
        System.out.println("  " + tasks[index] + "\n");
    }

    private void handleUnmarkCommand(String input) throws CherishException {
        int index = getValidTaskIndex(input, "unmark");
        tasks[index].markAsNotDone();
        System.out.println("Okay! I've marked this task as not done yet:");
        System.out.println("  " + tasks[index] + "\n");
    }

    private void handleDeleteCommand(String input) throws CherishException {
        int index = getValidTaskIndex(input, "delete");
        Task deletedTask = tasks[index];

        // Shift remaining tasks left
        for (int i = index; i < taskCount - 1; i++) {
            tasks[i] = tasks[i + 1];
        }
        tasks[taskCount - 1] = null; // Clear last reference
        taskCount--;

        System.out.println("Noted! I've removed this task:");
        System.out.println("  " + deletedTask);
        System.out.println("Now you have " + taskCount + (taskCount == 1 ? " task" : " tasks") + " in your list.\n");
    }

}