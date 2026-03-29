package com.natwest.oceanexplorer.navigation;

import com.natwest.oceanexplorer.exception.ObstacleEncounteredException;
import com.natwest.oceanexplorer.exception.OutOfBoundsException;
import com.natwest.oceanexplorer.model.Grid;
import com.natwest.oceanexplorer.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("BoundaryValidator")
class BoundaryValidatorTest {

    private BoundaryValidator validator;

    @BeforeEach
    void setUp() {
        Grid grid = new Grid(5, 5, Set.of(new Position(3, 3)));
        validator = new BoundaryValidator(grid);
    }

    @Test
    @DisplayName("should pass validation for a safe position")
    void shouldPassForSafePosition() {
        assertThatCode(() -> validator.validate(new Position(1, 1)))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("should throw OutOfBoundsException for negative x")
    void shouldThrowForNegativeX() {
        assertThatThrownBy(() -> validator.validate(new Position(-1, 2)))
                .isInstanceOf(OutOfBoundsException.class)
                .hasMessageContaining("-1");
    }

    @Test
    @DisplayName("should throw OutOfBoundsException for x equal to grid width")
    void shouldThrowForXEqualToWidth() {
        assertThatThrownBy(() -> validator.validate(new Position(5, 2)))
                .isInstanceOf(OutOfBoundsException.class);
    }

    @Test
    @DisplayName("should throw OutOfBoundsException for y beyond grid height")
    void shouldThrowForYBeyondHeight() {
        assertThatThrownBy(() -> validator.validate(new Position(2, 5)))
                .isInstanceOf(OutOfBoundsException.class);
    }

    @Test
    @DisplayName("should throw ObstacleEncounteredException for position with obstacle")
    void shouldThrowForObstaclePosition() {
        assertThatThrownBy(() -> validator.validate(new Position(3, 3)))
                .isInstanceOf(ObstacleEncounteredException.class)
                .hasMessageContaining("3, 3");
    }

    @Test
    @DisplayName("should throw OutOfBoundsException before ObstacleEncounteredException when out of bounds")
    void shouldPrioritiseBoundaryOverObstacle() {
        // even if somehow an obstacle is registered out of bounds, boundary check fires first
        assertThatThrownBy(() -> validator.validate(new Position(-1, -1)))
                .isInstanceOf(OutOfBoundsException.class);
    }

    @Test
    @DisplayName("should accept corner positions as valid")
    void shouldAcceptCornerPositions() {
        assertThatCode(() -> validator.validate(new Position(0, 0))).doesNotThrowAnyException();
        assertThatCode(() -> validator.validate(new Position(4, 4))).doesNotThrowAnyException();
        assertThatCode(() -> validator.validate(new Position(0, 4))).doesNotThrowAnyException();
        assertThatCode(() -> validator.validate(new Position(4, 0))).doesNotThrowAnyException();
    }
}
