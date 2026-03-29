package com.natwest.oceanexplorer.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Grid")
class GridTest {

    private Grid grid;

    @BeforeEach
    void setUp() {
        grid = new Grid(5, 5, Set.of(new Position(2, 2)));
    }

    @Test
    @DisplayName("should accept positions within bounds")
    void shouldAcceptPositionsWithinBounds() {
        assertThat(grid.isWithinBounds(new Position(0, 0))).isTrue();
        assertThat(grid.isWithinBounds(new Position(4, 4))).isTrue();
        assertThat(grid.isWithinBounds(new Position(2, 3))).isTrue();
    }

    @Test
    @DisplayName("should reject positions outside bounds")
    void shouldRejectPositionsOutsideBounds() {
        assertThat(grid.isWithinBounds(new Position(-1, 0))).isFalse();
        assertThat(grid.isWithinBounds(new Position(0, -1))).isFalse();
        assertThat(grid.isWithinBounds(new Position(5, 0))).isFalse();
        assertThat(grid.isWithinBounds(new Position(0, 5))).isFalse();
    }

    @Test
    @DisplayName("should detect obstacle at registered position")
    void shouldDetectObstacleAtRegisteredPosition() {
        assertThat(grid.hasObstacleAt(new Position(2, 2))).isTrue();
    }

    @Test
    @DisplayName("should return false for position without obstacle")
    void shouldReturnFalseForPositionWithoutObstacle() {
        assertThat(grid.hasObstacleAt(new Position(1, 1))).isFalse();
    }

    @Test
    @DisplayName("should throw when constructed with zero width")
    void shouldThrowForZeroWidth() {
        assertThatThrownBy(() -> new Grid(0, 5))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("should throw when constructed with negative height")
    void shouldThrowForNegativeHeight() {
        assertThatThrownBy(() -> new Grid(5, -1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("should create grid with no obstacles when using simple constructor")
    void shouldCreateGridWithNoObstacles() {
        Grid simple = new Grid(3, 3);
        assertThat(simple.hasObstacleAt(new Position(1, 1))).isFalse();
    }
}
