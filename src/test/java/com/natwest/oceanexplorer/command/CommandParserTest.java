package com.natwest.oceanexplorer.command;

import com.natwest.oceanexplorer.exception.InvalidCommandException;
import com.natwest.oceanexplorer.model.Command;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("CommandParser")
class CommandParserTest {

    private CommandParser parser;

    @BeforeEach
    void setUp() {
        parser = new CommandParser();
    }

    @Test
    @DisplayName("should parse a valid uppercase command string")
    void shouldParseUppercaseCommands() {
        List<Command> result = parser.parse("FFLRB");
        assertThat(result).containsExactly(
                Command.FORWARD, Command.FORWARD, Command.LEFT,
                Command.RIGHT, Command.BACKWARD);
    }

    @Test
    @DisplayName("should parse a valid lowercase command string")
    void shouldParseLowercaseCommands() {
        List<Command> result = parser.parse("fflrb");
        assertThat(result).containsExactly(
                Command.FORWARD, Command.FORWARD, Command.LEFT,
                Command.RIGHT, Command.BACKWARD);
    }

    @Test
    @DisplayName("should strip whitespace from command string")
    void shouldStripWhitespace() {
        List<Command> result = parser.parse("F F L R");
        assertThat(result).containsExactly(
                Command.FORWARD, Command.FORWARD, Command.LEFT, Command.RIGHT);
    }

    @Test
    @DisplayName("should handle a single command")
    void shouldHandleSingleCommand() {
        assertThat(parser.parse("F")).containsExactly(Command.FORWARD);
    }

    @Test
    @DisplayName("should throw InvalidCommandException for unknown character in string")
    void shouldThrowForUnknownCharacter() {
        assertThatThrownBy(() -> parser.parse("FFXL"))
                .isInstanceOf(InvalidCommandException.class)
                .hasMessageContaining("X");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException for null input")
    void shouldThrowForNullInput() {
        assertThatThrownBy(() -> parser.parse(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("should throw IllegalArgumentException for blank input")
    void shouldThrowForBlankInput() {
        assertThatThrownBy(() -> parser.parse("   "))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
