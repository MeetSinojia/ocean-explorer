package com.natwest.oceanexplorer.command;

import com.natwest.oceanexplorer.model.Direction;
import com.natwest.oceanexplorer.probe.MovementHistory;
import com.natwest.oceanexplorer.probe.Probe;
import lombok.extern.slf4j.Slf4j;

/**
 * Rotates the probe 90 degrees to the right (clockwise).
 * The probe's position does not change.
 */
@Slf4j
public class TurnRightHandler implements CommandHandler {

    @Override
    public void execute(Probe probe, MovementHistory history) {
        Direction newDirection = probe.getDirection().turnRight();
        log.debug("RIGHT: {} -> {}", probe.getDirection(), newDirection);
        probe.face(newDirection);
        log.info("Probe turned right, now facing {}", newDirection);
    }
}
