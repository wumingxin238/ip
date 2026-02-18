package cherish.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import cherish.CherishException;

class DeadlineTest {

    @Test
    void constructor_validInput_parsesDate() throws Exception {
        Deadline deadline =
                new Deadline("submit report", "2026-01-31 1800");

        assertEquals("submit report", deadline.getDescription());
        assertEquals(TaskType.DEADLINE, deadline.getType());
        assertEquals("[D][ ] submit report (by: Jan 31 2026 1800)",
                deadline.toString());
    }

    @Test
    void constructor_invalidDate_throwsException() {
        assertThrows(CherishException.class, () -> new Deadline("submit report",
                "bad-date"));
    }

    @Test
    void toFileString_formatsCorrectly() throws Exception {
        Deadline deadline =
                new Deadline("submit report", "2026-01-31 1800");

        assertEquals("D | 0 | submit report | 2026-01-31 1800",
                deadline.toFileString());
    }
}
