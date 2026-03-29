package com.natwest.oceanexplorer.exception;

import com.natwest.oceanexplorer.model.Position;

/**
 * Thrown when the probe attempts to move outside the grid boundaries.
 */
public class OutOfBoundsException extends RuntimeException {

    private final Position attemptedPosition;

    public OutOfBoundsException(Position attemptedPosition) {
        super("Position " + attemptedPosition + " is outside the grid boundaries. Probe has stopped.");
        this.attemptedPosition = attemptedPosition;
    }

    public Position getAttemptedPosition() {
        return attemptedPosition;
    }
}
