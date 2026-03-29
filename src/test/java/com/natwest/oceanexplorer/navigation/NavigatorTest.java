package com.natwest.oceanexplorer.navigation;

import com.natwest.oceanexplorer.model.Direction;
import com.natwest.oceanexplorer.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Navigator")
class NavigatorTest {

    private Navigator navigator;
    private final Position origin = new Position(2, 2);

    @BeforeEach
    void setUp() {
        navigator = new Navigator();
    }

    @ParameterizedTest(name = "FORWARD facing {0} -> ({1},{2})")
    @CsvSource({
        "NORTH, 2, 3",
        "SOUTH, 2, 1",
        "EAST,  3, 2",
        "WEST,  1, 2"
    })
    @DisplayName("should calculate correct position when moving forward")
    void shouldCalculateForwardPosition(Direction direction, int expectedX, int expectedY) {
        Position result = navigator.calculateForward(origin, direction);
        assertThat(result).isEqualTo(new Position(expectedX, expectedY));
    }

    @ParameterizedTest(name = "BACKWARD facing {0} -> ({1},{2})")
    @CsvSource({
        "NORTH, 2, 1",
        "SOUTH, 2, 3",
        "EAST,  1, 2",
        "WEST,  3, 2"
    })
    @DisplayName("should calculate correct position when moving backward")
    void shouldCalculateBackwardPosition(Direction direction, int expectedX, int expectedY) {
        Position result = navigator.calculateBackward(origin, direction);
        assertThat(result).isEqualTo(new Position(expectedX, expectedY));
    }

    @ParameterizedTest(name = "forward then backward facing {0} should return to origin")
    @CsvSource({"NORTH", "SOUTH", "EAST", "WEST"})
    @DisplayName("moving forward then backward should return to original position")
    void forwardThenBackwardShouldReturnToOrigin(Direction direction) {
        Position forward = navigator.calculateForward(origin, direction);
        Position backToOrigin = navigator.calculateBackward(forward, direction);
        assertThat(backToOrigin).isEqualTo(origin);
    }
}
