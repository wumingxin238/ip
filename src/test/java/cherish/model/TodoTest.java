package cherish.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TodoTest {

    @Test
    void constructor_validDescription_createsTodo() throws Exception {
        Todo todo = new Todo("read book");

        assertEquals("read book", todo.getDescription());
        assertFalse(todo.isDone());
        assertEquals("[T][ ] read book", todo.toString());
    }

    @Test
    void markAsDone_updatesStatus() throws Exception {
        Todo todo = new Todo("read book");

        todo.markAsDone();

        assertTrue(todo.isDone());
        assertEquals("[T][X] read book", todo.toString());
    }

    @Test
    void toFileString_formatsCorrectly() throws Exception {
        Todo todo = new Todo("read book");

        assertEquals("T | 0 | read book", todo.toFileString());
    }
}
