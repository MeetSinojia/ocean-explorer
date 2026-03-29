package com.natwest.oceanexplorer.probe;

import com.natwest.oceanexplorer.command.CommandHandlerFactory;
import com.natwest.oceanexplorer.model.Grid;
import com.natwest.oceanexplorer.model.Position;
import com.natwest.oceanexplorer.navigation.MultiProbeBoundaryValidator;
import com.natwest.oceanexplorer.navigation.Navigator;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Manages multiple probes operating on the same shared grid.
 *
 * Supports concurrent mission execution with runtime collision detection.
 */
public class ProbeFleetController {

    private final Grid grid;
    private final Navigator navigator;
    private final Map<String, ProbeController> probeControllers = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public ProbeFleetController(Grid grid) {
        this.grid = grid;
        this.navigator = new Navigator();
    }

    public void registerProbe(String probeId, Probe probe) {
        if (probeControllers.containsKey(probeId)) {
            throw new IllegalArgumentException("Probe with id " + probeId + " is already registered");
        }

        if (positionOccupiedByOtherProbe(probe.getPosition(), null)) {
            throw new IllegalStateException("Cannot place probe " + probeId + " at occupied position " + probe.getPosition());
        }

        MultiProbeBoundaryValidator validator = new MultiProbeBoundaryValidator(grid, () ->
                getOtherProbePositions(probeId)
        );

        CommandHandlerFactory factory = new CommandHandlerFactory(navigator, validator);
        probeControllers.put(probeId, new ProbeController(probe, factory));
    }

    public ProbeController getProbeController(String probeId) {
        ProbeController controller = probeControllers.get(probeId);
        if (controller == null) {
            throw new IllegalArgumentException("Probe with id " + probeId + " is not registered");
        }
        return controller;
    }

    public MissionResult executeMission(String probeId, String commandString) {
        return getProbeController(probeId).execute(commandString);
    }

    public CompletableFuture<MissionResult> executeMissionAsync(String probeId, String commandString) {
        return CompletableFuture.supplyAsync(() -> executeMission(probeId, commandString), executor);
    }

    public Set<Position> getOtherProbePositions(String probeId) {
        return probeControllers.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(probeId))
                .map(entry -> entry.getValue().getProbe().getPosition())
                .collect(Collectors.toUnmodifiableSet());
    }

    private boolean positionOccupiedByOtherProbe(Position position, String excludingProbeId) {
        return probeControllers.entrySet().stream()
                .filter(entry -> excludingProbeId == null || !entry.getKey().equals(excludingProbeId))
                .anyMatch(entry -> entry.getValue().getProbe().getPosition().equals(position));
    }

    public void shutdown() {
        executor.shutdownNow();
    }
}
