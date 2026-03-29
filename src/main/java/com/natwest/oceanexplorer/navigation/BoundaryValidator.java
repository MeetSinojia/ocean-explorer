package com.natwest.oceanexplorer.navigation;

import com.natwest.oceanexplorer.exception.ObstacleEncounteredException;
import com.natwest.oceanexplorer.exception.OutOfBoundsException;
import com.natwest.oceanexplorer.model.Grid;
import com.natwest.oceanexplorer.model.Position;

/**
 * Validates whether a candidate position is legal on the given grid.
 * Throws descriptive exceptions if the position is out of bounds or obstructed.
 */
public class BoundaryValidator {

    private final Grid grid;

    public BoundaryValidator(Grid grid) {
        this.grid = grid;
    }

    /**
     * Validates the candidate position.
     * Checks boundary first, then obstacles — boundary violation is more fundamental.
     *
     * @param candidate the position the probe wants to move to
     * @throws OutOfBoundsException          if the position is outside the grid
     * @throws ObstacleEncounteredException  if the position contains an obstacle
     */
    public void validate(Position candidate) {
        if (!grid.isWithinBounds(candidate)) {
            throw new OutOfBoundsException(candidate);
        }
        if (grid.hasObstacleAt(candidate)) {
            throw new ObstacleEncounteredException(candidate);
        }
    }
}
