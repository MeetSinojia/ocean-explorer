package com.natwest.oceanexplorer.command;

import com.natwest.oceanexplorer.model.Command;
import com.natwest.oceanexplorer.navigation.BoundaryValidator;
import com.natwest.oceanexplorer.navigation.Navigator;

import java.util.Map;

/**
 * Maps each Command enum to its corresponding handler.
 * Centralises handler wiring so the ProbeController stays clean.
 */
public class CommandHandlerFactory {

    private final Map<Command, CommandHandler> handlers;

    public CommandHandlerFactory(Navigator navigator, BoundaryValidator validator) {
        handlers = Map.of(
                Command.FORWARD,  new MoveForwardHandler(navigator, validator),
                Command.BACKWARD, new MoveBackwardHandler(navigator, validator),
                Command.LEFT,     new TurnLeftHandler(),
                Command.RIGHT,    new TurnRightHandler()
        );
    }

    /**
     * Returns the handler for the given command.
     *
     * @param command the command to look up
     * @return the corresponding CommandHandler (never null)
     */
    public CommandHandler getHandler(Command command) {
        return handlers.get(command);
    }
}
