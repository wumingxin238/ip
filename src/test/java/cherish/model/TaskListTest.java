package cherish.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import cherish.CherishException;

class TaskListTest {

    @Test
    void addAndRemoveTask_updatesSize() throws Exception {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));

        assertEquals(1, tasks.size());

        tasks.remove(0);

        assertEquals(0, tasks.size());
    }

    @Test
    void addByIndex_insertsCorrectly() throws Exception {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("a"));
        tasks.add(new Todo("c"));

        tasks.addByIndex(1, new Todo("b"));

        assertEquals("[T][ ] b", tasks.getByIndex(1).toString());
    }

    @Test
    void pop_emptyList_throwsException() {
        TaskList tasks = new TaskList();

        assertThrows(CherishException.class, tasks::pop);
    }

    @Test
    void findTasksByKeyword_returnsMatches() throws Exception {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));
        tasks.add(new Todo("write code"));

        String result = tasks.findTasksByKeyword("read");

        assertEquals("Here are the matching tasks in your list:\n1.[T][ ] read book",
                result);
    }
}
