package com.natwest.oceanexplorer.command;

import com.natwest.oceanexplorer.exception.ObstacleEncounteredException;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MoveForwardHandler")
class MoveForwardHandlerTest {

    @Mock private Navigator navigator;
    @Mock private BoundaryValidator validator;

    private MoveForwardHandler handler;
    private Probe probe;
    private MovementHistory history;

    @BeforeEach
    void setUp() {
        handler = new MoveForwardHandler(navigator, validator);
        probe = new Probe(new Position(1, 1), Direction.NORTH);
        history = new MovementHistory();
    }

    @Test
    @DisplayName("should move probe to calculated forward position")
    void shouldMoveProbeForward() {
        Position nextPosition = new Position(1, 2);
        when(navigator.calculateForward(new Position(1, 1), Direction.NORTH)).thenReturn(nextPosition);

        handler.execute(probe, history);

        assertThat(probe.getPosition()).isEqualTo(nextPosition);
    }

    @Test
    @DisplayName("should record the new position in history")
    void shouldRecordNewPositionInHistory() {
        Position nextPosition = new Position(1, 2);
        when(navigator.calculateForward(any(), any())).thenReturn(nextPosition);

        handler.execute(probe, history);

        assertThat(history.getVisitedPositions()).contains(nextPosition);
    }

    @Test
    @DisplayName("should call validator with the candidate position")
    void shouldCallValidatorWithCandidatePosition() {
        Position nextPosition = new Position(1, 2);
        when(navigator.calculateForward(any(), any())).thenReturn(nextPosition);

        handler.execute(probe, history);

        verify(validator).validate(nextPosition);
    }

    @Test
    @DisplayName("should propagate OutOfBoundsException from validator")
    void shouldPropagateOutOfBoundsException() {
        Position outOfBounds = new Position(1, 10);
        when(navigator.calculateForward(any(), any())).thenReturn(outOfBounds);
        doThrow(new OutOfBoundsException(outOfBounds)).when(validator).validate(outOfBounds);

        assertThatThrownBy(() -> handler.execute(probe, history))
                .isInstanceOf(OutOfBoundsException.class);
        assertThat(probe.getPosition()).isEqualTo(new Position(1, 1)); // unchanged
    }

    @Test
    @DisplayName("should propagate ObstacleEncounteredException from validator")
    void shouldPropagateObstacleException() {
        Position obstacle = new Position(1, 2);
        when(navigator.calculateForward(any(), any())).thenReturn(obstacle);
        doThrow(new ObstacleEncounteredException(obstacle)).when(validator).validate(obstacle);

        assertThatThrownBy(() -> handler.execute(probe, history))
                .isInstanceOf(ObstacleEncounteredException.class);
        assertThat(probe.getPosition()).isEqualTo(new Position(1, 1)); // unchanged
    }

    @Test
    @DisplayName("should not modify history when movement fails")
    void shouldNotRecordHistoryOnFailure() {
        Position obstacle = new Position(1, 2);
        when(navigator.calculateForward(any(), any())).thenReturn(obstacle);
        doThrow(new ObstacleEncounteredException(obstacle)).when(validator).validate(obstacle);

        try { handler.execute(probe, history); } catch (ObstacleEncounteredException ignored) {}

        assertThat(history.getVisitedPositions()).isEmpty();
    }
}
