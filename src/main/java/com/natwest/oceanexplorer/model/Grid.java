package com.natwest.oceanexplorer.model;

import lombok.Getter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents the bounded ocean floor grid and any obstacles within it.
 * The grid spans from (0, 0) to (width-1, height-1).
 */
@Getter
public class Grid {

    private final int width;
    private final int height;
    private final Set<Position> obstacles;

    /**
     * Creates a grid of the given dimensions with no obstacles.
     *
     * @param width  number of columns (must be > 0)
     * @param height number of rows (must be > 0)
     */
    public Grid(int width, int height) {
        this(width, height, Collections.emptySet());
    }

    /**
     * Creates a grid of the given dimensions with the specified obstacles.
     *
     * @param width     number of columns (must be > 0)
     * @param height    number of rows (must be > 0)
     * @param obstacles set of positions that are blocked
     */
    public Grid(int width, int height, Set<Position> obstacles) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException(
                    "Grid dimensions must be positive. Got width=" + width + ", height=" + height);
        }
        this.width = width;
        this.height = height;
        this.obstacles = Collections.unmodifiableSet(new HashSet<>(obstacles));
    }

    /**
     * Returns true if the given position lies within the grid boundaries.
     */
    public boolean isWithinBounds(Position position) {
        return position.x() >= 0
                && position.x() < width
                && position.y() >= 0
                && position.y() < height;
    }

    /**
     * Returns true if the given position is occupied by an obstacle.
     */
    public boolean hasObstacleAt(Position position) {
        return obstacles.contains(position);
    }
}
