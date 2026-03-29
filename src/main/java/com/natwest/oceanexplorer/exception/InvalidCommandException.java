package com.natwest.oceanexplorer.exception;

/**
 * Thrown when an unrecognised command character is provided to the probe.
 */
public class InvalidCommandException extends RuntimeException {

    public InvalidCommandException(String message) {
        super(message);
    }
}
