// src/test/java/cherish/model/TaskTest.java
package cherish.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import cherish.CherishException;



class TaskTest {

    @Test
    void testTodoCreationAndGetters() {
        Todo todo = null;
        try {
            todo = new Todo("Buy milk");
        } catch (CherishException e) {
            throw new RuntimeException(e);
        }
        assertEquals("Buy milk", todo.getDescription(), "Description should match");
        assertFalse(todo.isDone(), "New Todo should not be done by default");
        assertEquals("[T][ ] Buy milk", todo.toString(), "toString format should be correct");
    }

    @Test
    void testTodoMarkAsDone() throws CherishException {
        Todo todo = new Todo("Buy milk");
        todo.markAsDone();
        assertTrue(todo.isDone(), "Todo should be marked as done");
        assertEquals("[T][X] Buy milk", todo.toString(), "toString should reflect done status");
    }

    @Test
    void testDeadlineCreationAndGetters() throws CherishException {
        LocalDateTime byTime = LocalDateTime.of(2026, 2, 1, 12, 0);
        Deadline deadline = new Deadline("Submit report", byTime);
        assertEquals("Submit report", deadline.getDescription(), "Description should match");
        assertEquals(byTime, deadline.getBy(), "By time should match");
        assertFalse(deadline.isDone(), "New Deadline should not be done by default");
        assertEquals("[D][ ] Submit report (by: Feb 01 2026 1200)",
                deadline.toString(), "toString format should be correct");
    }

    @Test
    void testDeadlineMarkAsNotDone() throws CherishException {
        LocalDateTime byTime = LocalDateTime.of(2026, 2, 1, 12, 0);
        Deadline deadline = new Deadline("Submit report", byTime);
        deadline.markAsDone();
        assertTrue(deadline.isDone(), "Deadline should be marked as done");
        deadline.markAsNotDone();
        assertFalse(deadline.isDone(), "Deadline should be marked as not done");
        assertEquals("[D][ ] Submit report (by: Feb 01 2026 1200)", deadline.toString(),
                "toString should reflect not done status");
    }

    @Test
    void testEventCreationAndGetters() throws CherishException {
        LocalDateTime fromTime = LocalDateTime.of(2026, 2, 1, 10, 0);
        LocalDateTime toTime = LocalDateTime.of(2026, 2, 1, 11, 0);
        Event event = new Event("Meeting", fromTime, toTime);
        assertEquals("Meeting", event.getDescription(), "Description should match");
        assertEquals(fromTime, event.getFrom(), "From time should match");
        assertEquals(toTime, event.getTo(), "To time should match");
        assertFalse(event.isDone(), "New Event should not be done by default");
        assertEquals("[E][ ] Meeting (from: Feb 01 2026 1000 to: Feb 01 2026 1100)",
                event.toString(), "toString format should be correct");
    }
}
