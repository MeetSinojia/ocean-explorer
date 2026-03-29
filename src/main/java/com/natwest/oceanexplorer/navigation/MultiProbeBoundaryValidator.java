package com.natwest.oceanexplorer.navigation;

import com.natwest.oceanexplorer.exception.ProbeCollisionException;
import com.natwest.oceanexplorer.model.Grid;
import com.natwest.oceanexplorer.model.Position;

import java.util.Set;
import java.util.function.Supplier;

/**
 * Extends boundary validation with cross-probe collision checks.
 */
public class MultiProbeBoundaryValidator extends BoundaryValidator {

    private final Supplier<Set<Position>> occupiedProbePositionsSupplier;

    public MultiProbeBoundaryValidator(Grid grid, Supplier<Set<Position>> occupiedProbePositionsSupplier) {
        super(grid);
        this.occupiedProbePositionsSupplier = occupiedProbePositionsSupplier;
    }

    @Override
    public void validate(Position candidate) {
        super.validate(candidate);

        Set<Position> occupiedProbePositions = occupiedProbePositionsSupplier.get();
        if (occupiedProbePositions.contains(candidate)) {
            throw new ProbeCollisionException(candidate);
        }
    }
}
