package com.natwest.oceanexplorer.command;

import com.natwest.oceanexplorer.model.Position;
import com.natwest.oceanexplorer.navigation.BoundaryValidator;
import com.natwest.oceanexplorer.navigation.Navigator;
import com.natwest.oceanexplorer.probe.MovementHistory;
import com.natwest.oceanexplorer.probe.Probe;
import lombok.extern.slf4j.Slf4j;

/**
 * Moves the probe one step forward in the direction it is currently facing.
 */
@Slf4j
public class MoveForwardHandler implements CommandHandler {

    private final Navigator navigator;
    private final BoundaryValidator validator;

    public MoveForwardHandler(Navigator navigator, BoundaryValidator validator) {
        this.navigator = navigator;
        this.validator = validator;
    }

    @Override
    public void execute(Probe probe, MovementHistory history) {
        Position next = navigator.calculateForward(probe.getPosition(), probe.getDirection());
        log.debug("FORWARD: {} facing {} -> candidate {}", probe.getPosition(), probe.getDirection(), next);
        validator.validate(next);
        probe.moveTo(next);
        history.record(next);
        log.info("Probe moved forward to {}", next);
    }
}
