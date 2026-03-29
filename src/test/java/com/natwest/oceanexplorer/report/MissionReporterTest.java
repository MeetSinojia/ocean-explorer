package com.natwest.oceanexplorer.report;

import com.natwest.oceanexplorer.model.Direction;
import com.natwest.oceanexplorer.model.Position;
import com.natwest.oceanexplorer.probe.MissionResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MissionReporter")
class MissionReporterTest {

    private MissionReporter reporter;

    @BeforeEach
    void setUp() {
        reporter = new MissionReporter();
    }

    @Test
    @DisplayName("should include COMPLETED status for successful mission")
    void shouldShowCompletedStatusForSuccess() {
        MissionResult result = MissionResult.successful(
                new Position(2, 3), Direction.EAST,
                List.of(new Position(0, 0), new Position(2, 3)));

        String report = reporter.buildReport(result);

        assertThat(report).contains("COMPLETED");
        assertThat(report).doesNotContain("HALTED");
    }

    @Test
    @DisplayName("should include HALTED status and reason for early-halted mission")
    void shouldShowHaltedStatusWithReason() {
        MissionResult result = MissionResult.halted(
                new Position(0, 0), Direction.NORTH,
                List.of(new Position(0, 0)),
                "Obstacle encountered at (0, 1)");

        String report = reporter.buildReport(result);

        assertThat(report).contains("HALTED");
        assertThat(report).contains("Obstacle encountered at (0, 1)");
    }

    @Test
    @DisplayName("should include final position and direction in report")
    void shouldIncludeFinalPositionAndDirection() {
        MissionResult result = MissionResult.successful(
                new Position(3, 4), Direction.WEST,
                List.of(new Position(3, 4)));

        String report = reporter.buildReport(result);

        assertThat(report).contains("3, 4");
        assertThat(report).contains("WEST");
    }

    @Test
    @DisplayName("should list all visited positions")
    void shouldListAllVisitedPositions() {
        List<Position> visited = List.of(
                new Position(0, 0), new Position(0, 1), new Position(0, 2));
        MissionResult result = MissionResult.successful(
                new Position(0, 2), Direction.NORTH, visited);

        String report = reporter.buildReport(result);

        assertThat(report).contains("(0, 0)");
        assertThat(report).contains("(0, 1)");
        assertThat(report).contains("(0, 2)");
    }

    @Test
    @DisplayName("should show the count of visited positions")
    void shouldShowVisitedCount() {
        List<Position> visited = List.of(
                new Position(0, 0), new Position(1, 0), new Position(2, 0));
        MissionResult result = MissionResult.successful(
                new Position(2, 0), Direction.EAST, visited);

        String report = reporter.buildReport(result);

        assertThat(report).contains("3");
    }
}
