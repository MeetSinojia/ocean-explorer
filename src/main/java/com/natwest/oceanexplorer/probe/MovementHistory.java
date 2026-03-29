package com.natwest.oceanexplorer.probe;

import com.natwest.oceanexplorer.model.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tracks all positions visited by the probe during a mission.
 *
 * Maintained as a separate concern from the probe itself — the probe's job
 * is to know where it is now; this class knows where it has been.
 */
public class MovementHistory {

    private final List<Position> visitedPositions = new ArrayList<>();

    /**
     * Records that the probe has visited or started at the given position.
     */
    public void record(Position position) {
        visitedPositions.add(position);
    }

    /**
     * Returns an unmodifiable view of all visited positions in visit order.
     */
    public List<Position> getVisitedPositions() {
        return Collections.unmodifiableList(visitedPositions);
    }

    /**
     * Returns the total number of positions visited (including the starting position).
     */
    public int size() {
        return visitedPositions.size();
    }
}
