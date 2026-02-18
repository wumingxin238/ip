package cherish.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UiTest {

    @Test
    void showWelcome_addsWelcomeMessage() {
        Ui ui = new Ui(true);

        ui.showWelcome();

        assertEquals(
                "Hello! I'm Cherish. What can I help you with today?",
                ui.peekMessagesForGui()
        );
    }

    @Test
    void showBye_addsByeMessage() {
        Ui ui = new Ui(true);

        ui.showBye();

        assertEquals(
                "Bye. Hope to see you again soon!",
                ui.peekMessagesForGui()
        );
    }

    @Test
    void showLoadingError_addsWarningMessage() {
        Ui ui = new Ui(true);

        ui.showLoadingError();

        assertEquals(
                "Warning: Could not load task data. Starting with empty list.",
                ui.peekMessagesForGui()
        );
    }

    @Test
    void showError_prefixesErrorMessage() {
        Ui ui = new Ui(true);

        ui.showError("Something went wrong");

        assertEquals(
                "Oops! Something went wrong",
                ui.peekMessagesForGui()
        );
    }

    @Test
    void showMessage_addsCustomMessage() {
        Ui ui = new Ui(true);

        ui.showMessage("Hello world");

        assertEquals(
                "Hello world",
                ui.peekMessagesForGui()
        );
    }

    @Test
    void getMessagesForGui_clearsStoredMessages() {
        Ui ui = new Ui(true);

        ui.showMessage("First");
        ui.showMessage("Second");

        String messages = ui.getMessagesForGui();

        assertEquals("First\nSecond", messages);
        assertEquals("", ui.peekMessagesForGui());
    }

    @Test
    void peekMessagesForGui_doesNotClearMessages() {
        Ui ui = new Ui(true);

        ui.showMessage("Test message");

        assertEquals("Test message", ui.peekMessagesForGui());
        assertEquals("Test message", ui.peekMessagesForGui());
    }
}
