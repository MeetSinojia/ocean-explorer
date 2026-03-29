package com.natwest.oceanexplorer.probe;

import com.natwest.oceanexplorer.command.CommandHandler;
import com.natwest.oceanexplorer.command.CommandHandlerFactory;
import com.natwest.oceanexplorer.command.CommandParser;
import com.natwest.oceanexplorer.exception.ObstacleEncounteredException;
import com.natwest.oceanexplorer.exception.OutOfBoundsException;
import com.natwest.oceanexplorer.exception.ProbeCollisionException;
import com.natwest.oceanexplorer.model.Command;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Orchestrates the execution of a command sequence against the probe.
 *
 * Responsibilities:
 *  - Parse the raw command string
 *  - Dispatch each command to the appropriate handler
 *  - Halt gracefully on obstacle or boundary violation
 *  - Return a full MissionResult
 *
 * This class does not know about navigation maths or grid rules — it delegates entirely.
 */
@Slf4j
public class ProbeController {

    private final Probe probe;
    private final MovementHistory history;
    private final CommandParser parser;
    private final CommandHandlerFactory handlerFactory;

    public ProbeController(Probe probe, CommandHandlerFactory handlerFactory) {
        this.probe = probe;
        this.history = new MovementHistory();
        this.parser = new CommandParser();
        this.handlerFactory = handlerFactory;

        // Record the starting position in the history
        history.record(probe.getPosition());
        log.info("Mission started. Probe at {}, facing {}", probe.getPosition(), probe.getDirection());
    }

    /**
     * Executes the given command string against the probe.
     *
     * If the probe hits a boundary or obstacle, execution halts at that point
     * and the result reflects the final safe position.
     *
     * @param commandString raw string of commands e.g. "FFLRF"
     * @return a MissionResult describing the outcome
     */
    public MissionResult execute(String commandString) {
        List<Command> commands = parser.parse(commandString);
        log.info("Executing {} command(s): {}", commands.size(), commandString);

        for (Command command : commands) {
            try {
                CommandHandler handler = handlerFactory.getHandler(command);
                handler.execute(probe, history);
            } catch (OutOfBoundsException e) {
                log.warn("Mission halted — out of bounds: {}", e.getMessage());
                return MissionResult.halted(
                        probe.getPosition(), probe.getDirection(),
                        history.getVisitedPositions(), e.getMessage());
            } catch (ObstacleEncounteredException e) {
                log.warn("Mission halted — obstacle encountered: {}", e.getMessage());
                return MissionResult.halted(
                        probe.getPosition(), probe.getDirection(),
                        history.getVisitedPositions(), e.getMessage());
            } catch (ProbeCollisionException e) {
                log.warn("Mission halted — probe collision: {}", e.getMessage());
                return MissionResult.halted(
                        probe.getPosition(), probe.getDirection(),
                        history.getVisitedPositions(), e.getMessage());
            }
        }

        log.info("All commands completed. Final state: {}", probe);
        return MissionResult.successful(
                probe.getPosition(), probe.getDirection(),
                history.getVisitedPositions());
    }

    /**
     * Returns the probe's current state without executing any commands.
     */
    public Probe getProbe() {
        return probe;
    }

    /**
     * Returns the full movement history recorded so far.
     */
    public MovementHistory getHistory() {
        return history;
    }
}
