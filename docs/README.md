# Cherish ChatBot User Guide

## Introduction

Welcome to **Cherish**, a simple and intuitive chatbot designed to help you manage your tasks efficiently. Cherish provides a conversational interface to add, list, mark, unmark, find, and delete tasks. It supports undo for reversible actions and offers both console and GUI modes.

## Getting Started

1. Ensure you have Java 17 installed. 
2. Launch the application by double-clicking on the jar file or use java -jar Cherish,jar. You should see the main window appear.
3. The chatbot will greet you with a welcome message: `Hello! I'm Cherish. What can I help you with today?`
4. Type your commands into the text field at the bottom of the window and press Enter or click the "Send" button.

---

## Features

### 1. View Welcome Message

*   **Command:** Start the application
*   **Description:** Displays the initial welcome message when the application starts.
*   **Example Output:**
    ```
    Hello! I'm Cherish. What can I help you with today?
    ```

### 2. Add a Task

You can add different types of tasks: generic tasks, deadlines, and events.

*   **Generic Task:**
    *   **Command:** `todo <task_description>`
    *   **Description:** Adds a basic task without a specific date/time.
    *   **Example Input:** `todo Read a book`
    *   **Example Output:**
        ```
        All set! I've added this task for you:)
          [T][ ] Read a book
        Now you have 1 task in your list.
        ```

*   **Deadline:**
    *   **Command:** `deadline <task_description> /by <yyyy-MM-dd HHmm>`
    *   **Description:** Adds a task with a due date and time.
    *   **Example Input:** `deadline Submit report /by 2024-09-15 1800`
    *   **Example Output:**
        ```
        All set! I've added this task for you:)
          [D][ ] Submit report (by: Sep 15 2024 1800)
        Now you have 2 tasks in your list.
        ```

*   **Event:**
    *   **Command:** `event <task_description> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>`
    *   **Description:** Adds a task with a start and end date/time. The end time must be after the start time.
    *   **Example Input:** `event Team meeting /from 2024-09-20 1400 /to 2024-09-20 1600`
    *   **Example Output:**
        ```
        Yay! I've added this event:)
          [E][ ] Team meeting (from: Sep 20 2024 1400 to: Sep 20 2024 1600)
        Now you have 3 tasks in your list.
        ```

### 3. List All Tasks

*   **Command:** `list`
*   **Description:** Shows all the tasks currently in your list, numbered and indicating their completion status.
*   **Example Input:** `list`
*   **Example Output:**
    ```
    1. [T][ ] Read a book
    2. [D][ ] Submit report (by: Sep 15 2024 1800)
    3. [E][ ] Team meeting (from: Sep 20 2024 1400 to: Sep 20 2024 1600)
    ```

### 4. Mark a Task as Done

*   **Command:** `mark <task_number>`
*   **Description:** Marks the task corresponding to the given number (as shown by the `list` command) as complete.
*   **Example Input:** `mark 2`
*   **Example Output:**
    ```
    Yay! This task is done:)
      [D][X] Submit report (by: Sep 15 2024 1800)
    ```

### 5. Mark a Task as Not Done

*   **Command:** `unmark <task_number>`
*   **Description:** Marks the task corresponding to the given number as not complete (reverses a previous `mark`).
*   **Example Input:** `unmark 2`
*   **Example Output:**
    ```
    No problem! This task is marked as not done
      [D][ ] Submit report (by: Sep 15 2024 1800)
    ```

### 6. Delete a Task

*   **Command:** `delete <task_number>`
*   **Description:** Removes the task corresponding to the given number from your list.
*   **Example Input:** `delete 1`
*   **Example Output:**
    ```
    Alright, I've removed this task for you
      [T][ ] Read a book
    Now you have 2 tasks in your list.
    ```

### 7. Find Tasks by Keyword

*   **Command:** `find <keyword>`
*   **Description:** Searches for tasks containing the specified keyword in their description (case-insensitive).
*   **Example Input:** `find report`
*   **Example Output:**
    ```
    Here are the matching tasks in your list:
    1. [D][X] Submit report (by: Sep 15 2024 1800)
    ```

### 8. Find Tasks by Date

*   **Command:** `finddate <yyyy-MM-dd>`
*   **Description:** Lists all deadlines and events that occur on the specified date.
*   **Example Input:** `finddate 2024-09-15`
*   **Example Output:**
    ```
    Here are the tasks on 2024-09-15:
    1. [D][ ] Submit report (by: Sep 15 2024 1800)
    ```

### 9. Undo

*   **Command:** `undo`
*   **Description:** Reverts the last undoable action (e.g., add, mark, unmark, delete).
*   **Example Input:** `undo`
*   **Example Output:** (varies depending on the undone action)
    ```
    (e.g., after undoing a mark: "Done! I've marked it as completed again ...")
    ```
*   **Note:** If there is nothing to undo, Cherish will respond with `Nothing to undo.` Supported undoable actions include adding a task (todo/deadline/event), marking/unmarking a task, and deleting a task.

### 10. Exit the ChatBot

*   **Command:** `bye`
*   **Description:** Saves your current tasks and exits the application.
*   **Example Input:** `bye`
*   **Example Output:**
    ```
    Bye. Hope to see you again soon!
    ```

---
## Troubleshooting

*   **Command not recognized:** If you enter a command that Cherish doesn't understand, you'll see an error message. Please double-check the command format listed above.
*   **Invalid date format:** When adding deadlines or events, use the format `yyyy-MM-dd HHmm` (e.g., `2024-09-15 1800`). For `finddate`, use `yyyy-MM-dd` (e.g., `2024-09-15`).
*   **Task number out of range:** When using `mark`, `unmark`, or `delete`, ensure the task number corresponds to an actual task shown by the `list` command.
*   **Unmark already not-done task:** If you try to `unmark` a task that is already not done, Cherish will inform you.
*   **Event time validation:** For events, the end time must be after the start time. Otherwise, an error will occur.

---

## Support

If you encounter any issues or have questions, please review the command formats provided in this guide. For further assistance, consult the project's source code or documentation.