package com.natwest.oceanexplorer.command;

import com.natwest.oceanexplorer.model.Command;

import java.util.Arrays;
import java.util.List;

/**
 * Parses a raw command string (e.g. "FFLRB") into an ordered list of Command enums.
 * Whitespace is stripped; parsing is case-insensitive.
 */
public class CommandParser {

    /**
     * Parses the given string into a list of commands.
     *
     * @param input raw command string such as "FFLRF" or "f f l r b"
     * @return ordered list of Commands
     * @throws com.natwest.oceanexplorer.exception.InvalidCommandException if any character is unrecognised
     * @throws IllegalArgumentException if input is null or blank
     */
    public List<Command> parse(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Command input must not be null or blank.");
        }

        String sanitised = input.replaceAll("\\s", "");

        return Arrays.stream(sanitised.split(""))
                .map(s -> Command.fromChar(s.charAt(0)))
                .toList();
    }
}
