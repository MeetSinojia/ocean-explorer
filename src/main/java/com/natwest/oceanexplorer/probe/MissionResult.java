package com.natwest.oceanexplorer.probe;

import com.natwest.oceanexplorer.model.Direction;
import com.natwest.oceanexplorer.model.Position;

import java.util.List;

/**
 * Immutable summary of a completed (or halted) mission.
 * Returned by ProbeController after executing a command sequence.
 */
public record MissionResult(
        Position finalPosition,
        Direction finalDirection,
        List<Position> visitedPositions,
        boolean haltedEarly,
        String haltReason
) {

    /**
     * Creates a result for a mission that completed all commands successfully.
     */
    public static MissionResult successful(Position finalPosition, Direction finalDirection,
                                           List<Position> visitedPositions) {
        return new MissionResult(finalPosition, finalDirection, visitedPositions, false, null);
    }

    /**
     * Creates a result for a mission that stopped before all commands were executed.
     */
    public static MissionResult halted(Position finalPosition, Direction finalDirection,
                                       List<Position> visitedPositions, String reason) {
        return new MissionResult(finalPosition, finalDirection, visitedPositions, true, reason);
    }
}
