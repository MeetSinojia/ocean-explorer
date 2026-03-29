package com.natwest.oceanexplorer.integration;

import com.natwest.oceanexplorer.OceanExplorerApp;
import com.natwest.oceanexplorer.model.Direction;
import com.natwest.oceanexplorer.model.Grid;
import com.natwest.oceanexplorer.model.Position;
import com.natwest.oceanexplorer.probe.MissionResult;
import com.natwest.oceanexplorer.probe.Probe;
import com.natwest.oceanexplorer.probe.ProbeController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * End-to-end integration tests covering full probe mission scenarios.
 * These tests exercise the entire stack from command string to final state.
 */
@DisplayName("Ocean Explorer Integration Tests")
class OceanExplorerIntegrationTest {

    private ProbeController buildController(Probe probe, Grid grid) {
        return OceanExplorerApp.buildController(probe, grid);
    }

    // ── Movement ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("probe starting at origin facing NORTH should move north on FORWARD")
    void shouldMoveNorthFromOrigin() {
        Probe probe = new Probe(new Position(0, 0), Direction.NORTH);
        ProbeController controller = buildController(probe, new Grid(10, 10));

        MissionResult result = controller.execute("FFF");

        assertThat(result.finalPosition()).isEqualTo(new Position(0, 3));
        assertThat(result.haltedEarly()).isFalse();
    }

    @Test
    @DisplayName("probe should move south when facing SOUTH")
    void shouldMoveSouth() {
        Probe probe = new Probe(new Position(5, 5), Direction.SOUTH);
        ProbeController controller = buildController(probe, new Grid(10, 10));

        MissionResult result = controller.execute("FF");

        assertThat(result.finalPosition()).isEqualTo(new Position(5, 3));
    }

    @Test
    @DisplayName("probe should move east when facing EAST")
    void shouldMoveEast() {
        Probe probe = new Probe(new Position(0, 0), Direction.EAST);
        ProbeController controller = buildController(probe, new Grid(10, 10));

        MissionResult result = controller.execute("FFF");

        assertThat(result.finalPosition()).isEqualTo(new Position(3, 0));
    }

    @Test
    @DisplayName("probe should move west when facing WEST")
    void shouldMoveWest() {
        Probe probe = new Probe(new Position(5, 0), Direction.WEST);
        ProbeController controller = buildController(probe, new Grid(10, 10));

        MissionResult result = controller.execute("FF");

        assertThat(result.finalPosition()).isEqualTo(new Position(3, 0));
    }

    @Test
    @DisplayName("probe should return to start after moving forward and then backward")
    void shouldReturnToStartAfterForwardAndBackward() {
        Position start = new Position(3, 3);
        Probe probe = new Probe(start, Direction.NORTH);
        ProbeController controller = buildController(probe, new Grid(10, 10));

        MissionResult result = controller.execute("FFB");

        assertThat(result.finalPosition()).isEqualTo(new Position(3, 4));
    }

    // ── Turning ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("four right turns should result in same facing direction")
    void fourRightTurnsShouldReturnToOriginalFacing() {
        Probe probe = new Probe(new Position(5, 5), Direction.NORTH);
        ProbeController controller = buildController(probe, new Grid(10, 10));

        MissionResult result = controller.execute("RRRR");

        assertThat(result.finalDirection()).isEqualTo(Direction.NORTH);
        assertThat(result.finalPosition()).isEqualTo(new Position(5, 5));
    }

    @Test
    @DisplayName("four left turns should result in same facing direction")
    void fourLeftTurnsShouldReturnToOriginalFacing() {
        Probe probe = new Probe(new Position(5, 5), Direction.EAST);
        ProbeController controller = buildController(probe, new Grid(10, 10));

        MissionResult result = controller.execute("LLLL");

        assertThat(result.finalDirection()).isEqualTo(Direction.EAST);
    }

    // ── Boundary behaviour ────────────────────────────────────────────────────

    @Test
    @DisplayName("probe should halt when trying to move below y=0")
    void shouldHaltAtSouthernBoundary() {
        Probe probe = new Probe(new Position(0, 0), Direction.SOUTH);
        ProbeController controller = buildController(probe, new Grid(5, 5));

        MissionResult result = controller.execute("F");

        assertThat(result.haltedEarly()).isTrue();
        assertThat(result.finalPosition()).isEqualTo(new Position(0, 0));
    }

    @Test
    @DisplayName("probe should halt when trying to move past northern boundary")
    void shouldHaltAtNorthernBoundary() {
        Probe probe = new Probe(new Position(0, 4), Direction.NORTH);
        ProbeController controller = buildController(probe, new Grid(5, 5));

        MissionResult result = controller.execute("FF");

        assertThat(result.haltedEarly()).isTrue();
        assertThat(result.finalPosition()).isEqualTo(new Position(0, 4));
    }

    @Test
    @DisplayName("probe should halt when trying to move past eastern boundary")
    void shouldHaltAtEasternBoundary() {
        Probe probe = new Probe(new Position(4, 0), Direction.EAST);
        ProbeController controller = buildController(probe, new Grid(5, 5));

        MissionResult result = controller.execute("F");

        assertThat(result.haltedEarly()).isTrue();
    }

    @Test
    @DisplayName("probe should halt when trying to move past western boundary")
    void shouldHaltAtWesternBoundary() {
        Probe probe = new Probe(new Position(0, 0), Direction.WEST);
        ProbeController controller = buildController(probe, new Grid(5, 5));

        MissionResult result = controller.execute("F");

        assertThat(result.haltedEarly()).isTrue();
        assertThat(result.finalPosition()).isEqualTo(new Position(0, 0));
    }

    // ── Obstacle behaviour ────────────────────────────────────────────────────

    @Test
    @DisplayName("probe should halt at obstacle and stay in last safe position")
    void shouldHaltAtObstacleAndStayInLastSafePosition() {
        Set<Position> obstacles = Set.of(new Position(0, 2));
        Grid grid = new Grid(10, 10, obstacles);
        Probe probe = new Probe(new Position(0, 0), Direction.NORTH);
        ProbeController controller = buildController(probe, grid);

        MissionResult result = controller.execute("FFFF");

        assertThat(result.haltedEarly()).isTrue();
        assertThat(result.finalPosition()).isEqualTo(new Position(0, 1));
    }

    @Test
    @DisplayName("probe should be unblocked if it turns away from obstacle")
    void shouldContinueIfTurningAwayFromObstacle() {
        Set<Position> obstacles = Set.of(new Position(0, 2));
        Grid grid = new Grid(10, 10, obstacles);
        Probe probe = new Probe(new Position(0, 1), Direction.NORTH);
        ProbeController controller = buildController(probe, grid);

        // obstacle is north at (0,2) — turn east, then move forward freely
        MissionResult result = controller.execute("RF");

        assertThat(result.haltedEarly()).isFalse();
        assertThat(result.finalPosition()).isEqualTo(new Position(1, 1));
    }

    // ── History ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("visited positions should include start and all movement steps")
    void shouldRecordAllVisitedPositions() {
        Probe probe = new Probe(new Position(0, 0), Direction.NORTH);
        ProbeController controller = buildController(probe, new Grid(10, 10));

        MissionResult result = controller.execute("FF");

        assertThat(result.visitedPositions())
                .containsExactly(
                        new Position(0, 0),
                        new Position(0, 1),
                        new Position(0, 2));
    }

    @Test
    @DisplayName("turns should not add new positions to visited list")
    void turnsShouldNotAddToVisitedPositions() {
        Probe probe = new Probe(new Position(0, 0), Direction.NORTH);
        ProbeController controller = buildController(probe, new Grid(10, 10));

        MissionResult result = controller.execute("LRLR"); // all turns, no movement

        assertThat(result.visitedPositions())
                .containsExactly(new Position(0, 0)); // only the start position
    }

    // ── Complex scenarios ─────────────────────────────────────────────────────

    @Test
    @DisplayName("complex navigation: move, turn, move — should reach correct final position")
    void complexNavigationShouldReachCorrectFinalPosition() {
        Probe probe = new Probe(new Position(0, 0), Direction.NORTH);
        ProbeController controller = buildController(probe, new Grid(10, 10));

        // F(0,1) F(0,2) R(face EAST) F(1,2) F(2,2) L(face NORTH) F(2,3)
        MissionResult result = controller.execute("FFRFFLF");

        assertThat(result.finalPosition()).isEqualTo(new Position(2, 3));
        assertThat(result.finalDirection()).isEqualTo(Direction.NORTH);
        assertThat(result.haltedEarly()).isFalse();
    }

    @Test
    @DisplayName("probe should complete mission with case-insensitive command string")
    void shouldHandleMixedCaseCommandString() {
        Probe probe = new Probe(new Position(0, 0), Direction.NORTH);
        ProbeController controller = buildController(probe, new Grid(10, 10));

        MissionResult result = controller.execute("fFrF");

        assertThat(result.finalPosition()).isEqualTo(new Position(1, 2));
        assertThat(result.haltedEarly()).isFalse();
    }

    @Test
    @DisplayName("probe should handle commands with spaces in the string")
    void shouldHandleCommandsWithSpaces() {
        Probe probe = new Probe(new Position(0, 0), Direction.NORTH);
        ProbeController controller = buildController(probe, new Grid(10, 10));

        MissionResult result = controller.execute("F F F");

        assertThat(result.finalPosition()).isEqualTo(new Position(0, 3));
    }
}
