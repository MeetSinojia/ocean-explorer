package com.natwest.oceanexplorer;

import com.natwest.oceanexplorer.command.CommandHandlerFactory;
import com.natwest.oceanexplorer.model.Direction;
import com.natwest.oceanexplorer.model.Grid;
import com.natwest.oceanexplorer.model.Position;
import com.natwest.oceanexplorer.navigation.BoundaryValidator;
import com.natwest.oceanexplorer.navigation.Navigator;
import com.natwest.oceanexplorer.probe.MissionResult;
import com.natwest.oceanexplorer.probe.Probe;
import com.natwest.oceanexplorer.probe.ProbeController;
import com.natwest.oceanexplorer.report.MissionReporter;

import java.util.Set;

/**
 * Entry point and demo runner for the Ocean Explorer application.
 *
 * Demonstrates three scenarios:
 *  1. A clean run with no obstacles
 *  2. A run that encounters an obstacle
 *  3. A run that hits the grid boundary
 */
public class OceanExplorerApp {

    public static void main(String[] args) {
        MissionReporter reporter = new MissionReporter();

        System.out.println("=== OCEAN EXPLORER KATA ===\n");

        // ── Scenario 1: Clean run ────────────────────────────────────────────
        System.out.println("Scenario 1: Clean run on a 10x10 grid");
        Grid grid1 = new Grid(10, 10);
        Probe probe1 = new Probe(new Position(0, 0), Direction.NORTH);
        ProbeController controller1 = buildController(probe1, grid1);

        MissionResult result1 = controller1.execute("FFRFF");
        reporter.printReport(result1);

        // ── Scenario 2: Obstacle in path ─────────────────────────────────────
        System.out.println("Scenario 2: Obstacle at (0, 2)");
        Set<Position> obstacles = Set.of(new Position(0, 2));
        Grid grid2 = new Grid(10, 10, obstacles);
        Probe probe2 = new Probe(new Position(0, 0), Direction.NORTH);
        ProbeController controller2 = buildController(probe2, grid2);

        MissionResult result2 = controller2.execute("FFFF");
        reporter.printReport(result2);

        // ── Scenario 3: Hits boundary ────────────────────────────────────────
        System.out.println("Scenario 3: Probe walks off the southern edge");
        Grid grid3 = new Grid(10, 10);
        Probe probe3 = new Probe(new Position(0, 0), Direction.SOUTH);
        ProbeController controller3 = buildController(probe3, grid3);

        MissionResult result3 = controller3.execute("FFF");
        reporter.printReport(result3);
    }

    /**
     * Wires up the full dependency graph for a ProbeController.
     */
    public static ProbeController buildController(Probe probe, Grid grid) {
        Navigator navigator = new Navigator();
        BoundaryValidator validator = new BoundaryValidator(grid);
        CommandHandlerFactory factory = new CommandHandlerFactory(navigator, validator);
        return new ProbeController(probe, factory);
    }
}
