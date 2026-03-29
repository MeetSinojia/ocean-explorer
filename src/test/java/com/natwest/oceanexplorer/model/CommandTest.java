package com.natwest.oceanexplorer.model;

import com.natwest.oceanexplorer.exception.InvalidCommandException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Command")
class CommandTest {

    @ParameterizedTest(name = "'{0}' -> {1}")
    @CsvSource({
        "F, FORWARD",
        "f, FORWARD",
        "B, BACKWARD",
        "b, BACKWARD",
        "L, LEFT",
        "l, LEFT",
        "R, RIGHT",
        "r, RIGHT"
    })
    @DisplayName("should parse valid characters case-insensitively")
    void shouldParseValidCharacters(char input, Command expected) {
        assertThat(Command.fromChar(input)).isEqualTo(expected);
    }

    @Test
    @DisplayName("should throw InvalidCommandException for unknown character")
    void shouldThrowForUnknownCharacter() {
        assertThatThrownBy(() -> Command.fromChar('X'))
                .isInstanceOf(InvalidCommandException.class)
                .hasMessageContaining("X");
    }

    @Test
    @DisplayName("should throw InvalidCommandException for digit character")
    void shouldThrowForDigitCharacter() {
        assertThatThrownBy(() -> Command.fromChar('1'))
                .isInstanceOf(InvalidCommandException.class);
    }
}
