package com.natwest.oceanexplorer.command;

import com.natwest.oceanexplorer.model.Direction;
import com.natwest.oceanexplorer.model.Position;
import com.natwest.oceanexplorer.probe.MovementHistory;
import com.natwest.oceanexplorer.probe.Probe;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Turn Handlers")
class TurnHandlerTest {

    @ParameterizedTest(name = "TurnRight: facing {0} -> {1}")
    @CsvSource({
        "NORTH, EAST",
        "EAST,  SOUTH",
        "SOUTH, WEST",
        "WEST,  NORTH"
    })
    @DisplayName("TurnRightHandler should rotate probe clockwise")
    void turnRightShouldRotateClockwise(Direction from, Direction expected) {
        Probe probe = new Probe(new Position(1, 1), from);
        new TurnRightHandler().execute(probe, new MovementHistory());
        assertThat(probe.getDirection()).isEqualTo(expected);
    }

    @ParameterizedTest(name = "TurnLeft: facing {0} -> {1}")
    @CsvSource({
        "NORTH, WEST",
        "WEST,  SOUTH",
        "SOUTH, EAST",
        "EAST,  NORTH"
    })
    @DisplayName("TurnLeftHandler should rotate probe counter-clockwise")
    void turnLeftShouldRotateCounterClockwise(Direction from, Direction expected) {
        Probe probe = new Probe(new Position(1, 1), from);
        new TurnLeftHandler().execute(probe, new MovementHistory());
        assertThat(probe.getDirection()).isEqualTo(expected);
    }

    @ParameterizedTest(name = "Turn handlers should not change probe position when facing {0}")
    @CsvSource({"NORTH", "EAST", "SOUTH", "WEST"})
    @DisplayName("TurnRightHandler should not alter probe position")
    void turnRightShouldNotChangePosition(Direction direction) {
        Position original = new Position(3, 3);
        Probe probe = new Probe(original, direction);
        new TurnRightHandler().execute(probe, new MovementHistory());
        assertThat(probe.getPosition()).isEqualTo(original);
    }

    @ParameterizedTest(name = "TurnLeft should not change position when facing {0}")
    @CsvSource({"NORTH", "EAST", "SOUTH", "WEST"})
    @DisplayName("TurnLeftHandler should not alter probe position")
    void turnLeftShouldNotChangePosition(Direction direction) {
        Position original = new Position(3, 3);
        Probe probe = new Probe(original, direction);
        new TurnLeftHandler().execute(probe, new MovementHistory());
        assertThat(probe.getPosition()).isEqualTo(original);
    }
}
