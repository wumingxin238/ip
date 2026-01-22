import java.util.Scanner;

public class Cherish {

    private static String[] tasks = new String[100];
    private static int taskCount = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Cherish ch = new Cherish();
        ch.greet();

        while (true) {
            String input = sc.nextLine();
            if (input.equals("bye")) {
                break;
            } else if (input.equals("list")) {
                printTaskList();
            } else {
                addTask(input);
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

    private static void addTask(String task) {
        tasks[taskCount] = task;
        taskCount++;
        System.out.println("added: " + task);
    }

    private static void printTaskList() {
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + ". " + tasks[i]);
        }
    }
}
