package com.natwest.oceanexplorer.command;

import com.natwest.oceanexplorer.model.Position;
import com.natwest.oceanexplorer.navigation.BoundaryValidator;
import com.natwest.oceanexplorer.navigation.Navigator;
import com.natwest.oceanexplorer.probe.MovementHistory;
import com.natwest.oceanexplorer.probe.Probe;
import lombok.extern.slf4j.Slf4j;

/**
 * Moves the probe one step backward, opposite to the direction it is currently facing.
 * The probe does not change its facing direction when moving backward.
 */
@Slf4j
public class MoveBackwardHandler implements CommandHandler {

    private final Navigator navigator;
    private final BoundaryValidator validator;

    public MoveBackwardHandler(Navigator navigator, BoundaryValidator validator) {
        this.navigator = navigator;
        this.validator = validator;
    }

    @Override
    public void execute(Probe probe, MovementHistory history) {
        Position next = navigator.calculateBackward(probe.getPosition(), probe.getDirection());
        log.debug("BACKWARD: {} facing {} -> candidate {}", probe.getPosition(), probe.getDirection(), next);
        validator.validate(next);
        probe.moveTo(next);
        history.record(next);
        log.info("Probe moved backward to {}", next);
    }
}
