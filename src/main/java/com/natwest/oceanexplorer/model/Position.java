package com.natwest.oceanexplorer.model;

/**
 * Immutable value object representing a position on the ocean floor grid.
 * Using Java 21 record for concise, immutable data carrier.
 */
public record Position(int x, int y) {

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
