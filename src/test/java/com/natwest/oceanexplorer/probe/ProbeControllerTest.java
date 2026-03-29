package com.natwest.oceanexplorer.probe;

import com.natwest.oceanexplorer.command.CommandHandlerFactory;
import com.natwest.oceanexplorer.model.Direction;
import com.natwest.oceanexplorer.model.Grid;
import com.natwest.oceanexplorer.model.Position;
import com.natwest.oceanexplorer.navigation.BoundaryValidator;
import com.natwest.oceanexplorer.navigation.Navigator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProbeController")
class ProbeControllerTest {

    private ProbeController buildController(Probe probe, Grid grid) {
        Navigator navigator = new Navigator();
        BoundaryValidator validator = new BoundaryValidator(grid);
        CommandHandlerFactory factory = new CommandHandlerFactory(navigator, validator);
        return new ProbeController(probe, factory);
    }

    @Test
    @DisplayName("should record starting position in history on construction")
    void shouldRecordStartingPosition() {
        Probe probe = new Probe(new Position(0, 0), Direction.NORTH);
        ProbeController controller = buildController(probe, new Grid(5, 5));

        assertThat(controller.getHistory().getVisitedPositions())
                .containsExactly(new Position(0, 0));
    }

    @Test
    @DisplayName("should return successful result after completing all commands")
    void shouldReturnSuccessfulResult() {
        Probe probe = new Probe(new Position(0, 0), Direction.NORTH);
        ProbeController controller = buildController(probe, new Grid(5, 5));

        MissionResult result = controller.execute("FF");

        assertThat(result.haltedEarly()).isFalse();
        assertThat(result.finalPosition()).isEqualTo(new Position(0, 2));
        assertThat(result.finalDirection()).isEqualTo(Direction.NORTH);
    }

    @Test
    @DisplayName("should halt early when probe hits boundary")
    void shouldHaltEarlyOnBoundary() {
        Probe probe = new Probe(new Position(0, 0), Direction.SOUTH);
        ProbeController controller = buildController(probe, new Grid(5, 5));

        MissionResult result = controller.execute("FF");

        assertThat(result.haltedEarly()).isTrue();
        assertThat(result.haltReason()).isNotBlank();
        assertThat(result.finalPosition()).isEqualTo(new Position(0, 0));
    }

    @Test
    @DisplayName("should halt early when probe hits obstacle")
    void shouldHaltEarlyOnObstacle() {
        Grid grid = new Grid(5, 5, Set.of(new Position(0, 1)));
        Probe probe = new Probe(new Position(0, 0), Direction.NORTH);
        ProbeController controller = buildController(probe, grid);

        MissionResult result = controller.execute("FF");

        assertThat(result.haltedEarly()).isTrue();
        assertThat(result.finalPosition()).isEqualTo(new Position(0, 0));
    }

    @Test
    @DisplayName("should accumulate history across multiple execute calls")
    void shouldAccumulateHistoryAcrossMultipleExecutions() {
        Probe probe = new Probe(new Position(0, 0), Direction.NORTH);
        ProbeController controller = buildController(probe, new Grid(10, 10));

        controller.execute("F");
        controller.execute("F");

        assertThat(controller.getHistory().getVisitedPositions())
                .containsExactly(new Position(0, 0), new Position(0, 1), new Position(0, 2));
    }

    @Test
    @DisplayName("should correctly handle a combined forward, turn, and forward sequence")
    void shouldHandleCombinedCommands() {
        Probe probe = new Probe(new Position(0, 0), Direction.NORTH);
        ProbeController controller = buildController(probe, new Grid(10, 10));

        MissionResult result = controller.execute("FFRFF");

        // NORTH: F->y=1, F->y=2, R->face EAST, F->x=1, F->x=2
        assertThat(result.finalPosition()).isEqualTo(new Position(2, 2));
        assertThat(result.finalDirection()).isEqualTo(Direction.EAST);
    }

    @Test
    @DisplayName("should correctly move backward without changing direction")
    void shouldMoveBackwardWithoutChangingDirection() {
        Probe probe = new Probe(new Position(2, 2), Direction.NORTH);
        ProbeController controller = buildController(probe, new Grid(10, 10));

        MissionResult result = controller.execute("B");

        assertThat(result.finalPosition()).isEqualTo(new Position(2, 1));
        assertThat(result.finalDirection()).isEqualTo(Direction.NORTH);
    }
}
