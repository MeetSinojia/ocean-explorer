package com.natwest.oceanexplorer.probe;

import com.natwest.oceanexplorer.model.Direction;
import com.natwest.oceanexplorer.model.Position;
import lombok.Getter;

/**
 * Represents the physical state of the submersible probe:
 * where it is and which way it is facing.
 *
 * This class is intentionally kept simple — it holds state only.
 * All movement logic lives in the command handlers and navigator.
 */
@Getter
public class Probe {

    private Position position;
    private Direction direction;

    /**
     * Creates a probe at the given starting position, facing the given direction.
     *
     * @param startPosition  initial grid position
     * @param startDirection initial facing direction
     */
    public Probe(Position startPosition, Direction startDirection) {
        this.position = startPosition;
        this.direction = startDirection;
    }

    /**
     * Updates the probe's position. Called by movement handlers after validation.
     */
    public void moveTo(Position newPosition) {
        this.position = newPosition;
    }

    /**
     * Updates the probe's facing direction. Called by turn handlers.
     */
    public void face(Direction newDirection) {
        this.direction = newDirection;
    }

    @Override
    public String toString() {
        return "Probe{position=" + position + ", direction=" + direction + "}";
    }
}
