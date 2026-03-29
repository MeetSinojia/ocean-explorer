package com.natwest.oceanexplorer.probe;

import com.natwest.oceanexplorer.model.Direction;
import com.natwest.oceanexplorer.model.Grid;
import com.natwest.oceanexplorer.model.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ProbeFleetController")
class ProbeFleetControllerTest {

    @Test
    @DisplayName("should prevent collision when one probe tries to move into another probe")
    void shouldPreventProbeCollision() {
        Grid grid = new Grid(5, 5);
        ProbeFleetController fleet = new ProbeFleetController(grid);

        fleet.registerProbe("A", new Probe(new Position(0, 0), Direction.NORTH));
        fleet.registerProbe("B", new Probe(new Position(0, 2), Direction.SOUTH));

        MissionResult resultA = fleet.executeMission("A", "F");
        assertThat(resultA.haltedEarly()).isFalse();
        assertThat(resultA.finalPosition()).isEqualTo(new Position(0, 1));

        MissionResult resultB = fleet.executeMission("B", "F");
        assertThat(resultB.haltedEarly()).isTrue();
        assertThat(resultB.haltReason()).contains("collision");
        assertThat(resultB.finalPosition()).isEqualTo(new Position(0, 2));
    }

    @Test
    @DisplayName("should support concurrent mission execution")
    void shouldSupportConcurrentMissionExecution() throws ExecutionException, InterruptedException {
        Grid grid = new Grid(10, 10);
        ProbeFleetController fleet = new ProbeFleetController(grid);

        fleet.registerProbe("A", new Probe(new Position(0, 0), Direction.NORTH));
        fleet.registerProbe("B", new Probe(new Position(2, 0), Direction.NORTH));

        var futureA = fleet.executeMissionAsync("A", "FF");
        var futureB = fleet.executeMissionAsync("B", "FF");

        MissionResult resultA = futureA.get();
        MissionResult resultB = futureB.get();

        assertThat(resultA.haltedEarly()).isFalse();
        assertThat(resultB.haltedEarly()).isFalse();
        assertThat(resultA.finalPosition()).isEqualTo(new Position(0, 2));
        assertThat(resultB.finalPosition()).isEqualTo(new Position(2, 2));
        fleet.shutdown();
    }

    @Test
    @DisplayName("should not allow registering two probes with the same id")
    void shouldRejectDuplicateProbeId() {
        Grid grid = new Grid(5, 5);
        ProbeFleetController fleet = new ProbeFleetController(grid);

        fleet.registerProbe("A", new Probe(new Position(0, 0), Direction.NORTH));

        assertThatThrownBy(() -> fleet.registerProbe("A", new Probe(new Position(1, 0), Direction.NORTH)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
