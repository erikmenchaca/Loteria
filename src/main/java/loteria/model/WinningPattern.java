package loteria.model;

import java.util.List;
import java.util.Collections;

/**
 * An immutable data class that defines a specific winning pattern.
 * A pattern consists of a name, a type, a score value, and a list of
 * required board positions that must be marked to win.
 */
public final class WinningPattern {

    private final String name;
    private final PatternType type;
    private final List<Position> requiredPositions;
    private final int pointValue;

    /**
     * Constructs a new WinningPattern.
     *
     * @param name             The display name of the pattern (e.g., "Four Corners").
     * @param type             The type of the pattern from the PatternType enum.
     * @param positions        A list of Position objects required to achieve the win.
     * @param pointValue       The score awarded for achieving this pattern.
     */
    public WinningPattern(String name, PatternType type, List<Position> positions, int pointValue) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Pattern name cannot be null or empty.");
        }
        if (type == null) {
            throw new IllegalArgumentException("PatternType cannot be null.");
        }
        if (positions == null || positions.isEmpty()) {
            throw new IllegalArgumentException("Required positions list cannot be null or empty.");
        }
        this.name = name;
        this.type = type;
        // Store a defensive copy to ensure immutability
        this.requiredPositions = List.copyOf(positions);
        this.pointValue = pointValue;
    }

    /**
     * @return The name of the pattern.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The type of the pattern.
     */
    public PatternType getType() {
        return type;
    }

    /**
     * @return An unmodifiable list of the positions required for this pattern.
     */
    public List<Position> getRequiredPositions() {
        return requiredPositions; // Already unmodifiable from List.copyOf()
    }

    /**
     * @return The point value awarded for completing this pattern.
     */
    public int getPointValue() {
        return pointValue;
    }

    /**
     * Checks if all positions in this pattern are valid for a board of a given size.
     *
     * @param boardSize The dimension of the board to check against.
     * @return true if the pattern fits on the board, false otherwise.
     */
    public boolean isValidForBoardSize(int boardSize) {
        for (Position pos : requiredPositions) {
            if (pos.getRow() < 0 || pos.getRow() >= boardSize ||
                pos.getCol() < 0 || pos.getCol() >= boardSize) {
                return false; // Position is out of bounds
            }
        }
        return true;
    }

    /**
     * Returns the name of the pattern.
     *
     * @return The pattern's name.
     */
    @Override
    public String toString() {
        return name;
    }
}