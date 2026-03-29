package com.natwest.oceanexplorer.model;

/**
 * Represents the set of valid commands that can be issued to the probe.
 */
public enum Command {
    FORWARD,
    BACKWARD,
    LEFT,
    RIGHT;

    /**
     * Parses a single character into a Command.
     *
     * @param ch the character to parse (F, B, L, R — case-insensitive)
     * @return the corresponding Command
     * @throws com.natwest.oceanexplorer.exception.InvalidCommandException if the character is not recognised
     */
    public static Command fromChar(char ch) {
        return switch (Character.toUpperCase(ch)) {
            case 'F' -> FORWARD;
            case 'B' -> BACKWARD;
            case 'L' -> LEFT;
            case 'R' -> RIGHT;
            default -> throw new com.natwest.oceanexplorer.exception.InvalidCommandException(
                    "Unknown command character: '" + ch + "'. Valid commands are F, B, L, R.");
        };
    }
}
