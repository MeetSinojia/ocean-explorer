package com.natwest.oceanexplorer.report;

import com.natwest.oceanexplorer.model.Position;
import com.natwest.oceanexplorer.probe.MissionResult;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Formats and prints a human-readable summary of a mission.
 * Kept separate from ProbeController so output format can evolve independently.
 */
public class MissionReporter {

    /**
     * Builds and returns the full mission report as a string.
     *
     * @param result the outcome of the mission
     * @return formatted report string
     */
    public String buildReport(MissionResult result) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n╔══════════════════════════════════════╗\n");
        sb.append(  "║         OCEAN EXPLORER REPORT        ║\n");
        sb.append(  "╚══════════════════════════════════════╝\n");

        sb.append(String.format("  Status        : %s%n",
                result.haltedEarly() ? "⚠ HALTED EARLY" : "✓ COMPLETED"));

        if (result.haltedEarly()) {
            sb.append(String.format("  Halt reason   : %s%n", result.haltReason()));
        }

        sb.append(String.format("  Final position: %s%n", result.finalPosition()));
        sb.append(String.format("  Facing        : %s%n", result.finalDirection()));
        sb.append(String.format("  Positions visited (%d):%n", result.visitedPositions().size()));

        List<Position> positions = result.visitedPositions();
        String positionList = positions.stream()
                .map(Position::toString)
                .collect(Collectors.joining(" → "));

        sb.append("  ").append(positionList).append("\n");
        sb.append("══════════════════════════════════════════\n");

        return sb.toString();
    }

    /**
     * Prints the mission report to standard output.
     *
     * @param result the outcome of the mission
     */
    public void printReport(MissionResult result) {
        System.out.println(buildReport(result));
    }
}
