package com.natwest.oceanexplorer.command;

import com.natwest.oceanexplorer.probe.MovementHistory;
import com.natwest.oceanexplorer.probe.Probe;

/**
 * Strategy interface for all probe command handlers.
 *
 * Each concrete handler is responsible for one command only.
 * New commands can be added without modifying existing handlers (Open/Closed Principle).
 */
public interface CommandHandler {

    /**
     * Executes this command against the given probe, recording any position change.
     *
     * @param probe   the probe whose state will be mutated
     * @param history the movement log to update if the probe changes position
     */
    void execute(Probe probe, MovementHistory history);
}
