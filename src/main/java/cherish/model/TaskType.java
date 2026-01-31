package cherish.model;

import cherish.CherishException;

/**
 * Enum representing the different types of tasks in the Cherish application.
 * Each type has a corresponding symbol used for display and file storage.
 */
public enum TaskType {
    TODO("T"),
    DEADLINE("D"),
    EVENT("E");

    private final String symbol;

    /**
     * Constructs a TaskType with the given symbol.
     *
     * @param symbol The single-character symbol representing this task type.
     */
    TaskType(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Gets the symbol associated with this task type.
     *
     * @return The symbol string (e.g., "T", "D", "E").
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Retrieves the TaskType enum constant based on its symbol.
     *
     * @param symbol The symbol string to look up.
     * @return The corresponding TaskType enum constant.
     * @throws CherishException If the symbol does not correspond to any known TaskType.
     */
    public static TaskType fromSymbol(String symbol) throws CherishException {
        return switch (symbol) {
            case "T" -> TODO;
            case "D" -> DEADLINE;
            case "E" -> EVENT;
            default -> throw new CherishException("Unknown task type: " + symbol);
        };
    }
}