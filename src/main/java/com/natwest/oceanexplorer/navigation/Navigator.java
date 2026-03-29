package com.natwest.oceanexplorer.navigation;

import com.natwest.oceanexplorer.model.Direction;
import com.natwest.oceanexplorer.model.Position;

/**
 * Pure calculation class: given a position and a facing direction,
 * computes the next candidate position for a forward or backward move.
 *
 * Does NOT perform any validation — that is BoundaryValidator's responsibility.
 */
public class Navigator {

    /**
     * Calculates the position one step forward from the given position and direction.
     *
     * @param current   the current position
     * @param direction the direction the probe is facing
     * @return the position directly ahead
     */
    public Position calculateForward(Position current, Direction direction) {
        return switch (direction) {
            case NORTH -> new Position(current.x(), current.y() + 1);
            case SOUTH -> new Position(current.x(), current.y() - 1);
            case EAST  -> new Position(current.x() + 1, current.y());
            case WEST  -> new Position(current.x() - 1, current.y());
        };
    }

    /**
     * Calculates the position one step backward from the given position and direction.
     *
     * @param current   the current position
     * @param direction the direction the probe is facing
     * @return the position directly behind
     */
    public Position calculateBackward(Position current, Direction direction) {
        // Moving backward is the same as moving forward in the opposite direction
        return calculateForward(current, direction.turnRight().turnRight());
    }
}
