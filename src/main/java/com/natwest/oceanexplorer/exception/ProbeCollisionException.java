package com.natwest.oceanexplorer.exception;

import com.natwest.oceanexplorer.model.Position;

/**
 * A probe attempted to move into a cell occupied by another probe.
 */
public class ProbeCollisionException extends RuntimeException {

    public ProbeCollisionException(Position position) {
        super("Probe collision at position " + position + ". Another probe is already occupying that position.");
    }
}
