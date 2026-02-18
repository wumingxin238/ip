package cherish.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import cherish.CherishException;

class EventTest {

    @Test
    void constructor_validInput_createsEvent() throws Exception {
        Event event =
                new Event("conference",
                        "2026-01-30 0900",
                        "2026-01-31 1700");

        assertEquals(TaskType.EVENT, event.getType());
        assertEquals("[E][ ] conference (from: Jan 30 2026 0900 to: Jan 31 2026 1700)",
                event.toString());
    }

    @Test
    void constructor_invalidDate_throwsException() {
        assertThrows(CherishException.class, () -> new Event("conference", "bad", "bad"));
    }
}
