package cherish.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import cherish.CherishException;

class TaskTypeTest {

    @Test
    void fromSymbol_validSymbol_returnsType() throws Exception {
        assertEquals(TaskType.TODO, TaskType.fromSymbol("T"));
        assertEquals(TaskType.DEADLINE, TaskType.fromSymbol("D"));
        assertEquals(TaskType.EVENT, TaskType.fromSymbol("E"));
    }

    @Test
    void fromSymbol_invalidSymbol_throwsException() {
        assertThrows(CherishException.class, () -> TaskType.fromSymbol("X"));
    }
}
