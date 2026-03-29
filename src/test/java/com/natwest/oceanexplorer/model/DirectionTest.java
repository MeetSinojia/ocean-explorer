package com.natwest.oceanexplorer.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Direction")
class DirectionTest {

    @ParameterizedTest(name = "{0} turnRight() = {1}")
    @CsvSource({
        "NORTH, EAST",
        "EAST,  SOUTH",
        "SOUTH, WEST",
        "WEST,  NORTH"
    })
    @DisplayName("should rotate 90 degrees clockwise when turning right")
    void shouldTurnRight(Direction from, Direction expected) {
        assertThat(from.turnRight()).isEqualTo(expected);
    }

    @ParameterizedTest(name = "{0} turnLeft() = {1}")
    @CsvSource({
        "NORTH, WEST",
        "WEST,  SOUTH",
        "SOUTH, EAST",
        "EAST,  NORTH"
    })
    @DisplayName("should rotate 90 degrees counter-clockwise when turning left")
    void shouldTurnLeft(Direction from, Direction expected) {
        assertThat(from.turnLeft()).isEqualTo(expected);
    }

    @Test
    @DisplayName("four right turns should return to original direction")
    void fourRightTurnsShouldReturnToStart() {
        Direction original = Direction.NORTH;
        Direction result = original.turnRight().turnRight().turnRight().turnRight();
        assertThat(result).isEqualTo(original);
    }

    @Test
    @DisplayName("four left turns should return to original direction")
    void fourLeftTurnsShouldReturnToStart() {
        Direction original = Direction.EAST;
        Direction result = original.turnLeft().turnLeft().turnLeft().turnLeft();
        assertThat(result).isEqualTo(original);
    }

    @Test
    @DisplayName("one left then one right should return to original direction")
    void leftThenRightShouldCancelOut() {
        Direction original = Direction.SOUTH;
        assertThat(original.turnLeft().turnRight()).isEqualTo(original);
    }
}
