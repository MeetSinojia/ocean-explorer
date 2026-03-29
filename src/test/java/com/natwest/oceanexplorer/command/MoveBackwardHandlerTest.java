package com.natwest.oceanexplorer.command;

import com.natwest.oceanexplorer.exception.OutOfBoundsException;
import com.natwest.oceanexplorer.model.Direction;
import com.natwest.oceanexplorer.model.Position;
import com.natwest.oceanexplorer.navigation.BoundaryValidator;
import com.natwest.oceanexplorer.navigation.Navigator;
import com.natwest.oceanexplorer.probe.MovementHistory;
import com.natwest.oceanexplorer.probe.Probe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MoveBackwardHandler")
class MoveBackwardHandlerTest {

    @Mock private Navigator navigator;
    @Mock private BoundaryValidator validator;

    private MoveBackwardHandler handler;
    private Probe probe;
    private MovementHistory history;

    @BeforeEach
    void setUp() {
        handler = new MoveBackwardHandler(navigator, validator);
        probe = new Probe(new Position(2, 2), Direction.NORTH);
        history = new MovementHistory();
    }

    @Test
    @DisplayName("should move probe to calculated backward position")
    void shouldMoveProbeBackward() {
        Position backPosition = new Position(2, 1);
        when(navigator.calculateBackward(new Position(2, 2), Direction.NORTH)).thenReturn(backPosition);

        handler.execute(probe, history);

        assertThat(probe.getPosition()).isEqualTo(backPosition);
    }

    @Test
    @DisplayName("should record the new position in history")
    void shouldRecordNewPositionInHistory() {
        Position backPosition = new Position(2, 1);
        when(navigator.calculateBackward(any(), any())).thenReturn(backPosition);

        handler.execute(probe, history);

        assertThat(history.getVisitedPositions()).contains(backPosition);
    }

    @Test
    @DisplayName("should not change probe position when out of bounds")
    void shouldNotChangePositionOnOutOfBounds() {
        Position oob = new Position(2, -1);
        when(navigator.calculateBackward(any(), any())).thenReturn(oob);
        doThrow(new OutOfBoundsException(oob)).when(validator).validate(oob);

        assertThatThrownBy(() -> handler.execute(probe, history))
                .isInstanceOf(OutOfBoundsException.class);

        assertThat(probe.getPosition()).isEqualTo(new Position(2, 2));
    }

    @Test
    @DisplayName("should not change facing direction when moving backward")
    void shouldNotChangeFacingDirection() {
        Position backPosition = new Position(2, 1);
        when(navigator.calculateBackward(any(), any())).thenReturn(backPosition);

        handler.execute(probe, history);

        assertThat(probe.getDirection()).isEqualTo(Direction.NORTH);
    }
}
