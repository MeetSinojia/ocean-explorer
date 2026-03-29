package com.natwest.oceanexplorer.model;

/**
 * Represents the cardinal direction the probe is currently facing.
 * Encapsulates all rotation logic — no other class needs to know how turning works.
 */
public enum Direction {
    NORTH, EAST, SOUTH, WEST;

    private static final Direction[] VALUES = values();

    /**
     * Returns the direction after turning 90 degrees clockwise (right).
     */
    public Direction turnRight() {
        return VALUES[(this.ordinal() + 1) % VALUES.length];
    }

    /**
     * Returns the direction after turning 90 degrees counter-clockwise (left).
     */
    public Direction turnLeft() {
        return VALUES[(this.ordinal() + VALUES.length - 1) % VALUES.length];
    }
}
