package loteria.model;

import java.util.Objects;

/**
 * An immutable data class representing a 2D coordinate (row and column) on a board.
 * This class is a fundamental building block for defining winning patterns.
 */
public final class Position {

    private final int row;
    private final int col;

    /**
     * Constructs a new Position with the specified row and column.
     *
     * @param row The row coordinate.
     * @param col The column coordinate.
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return The row coordinate.
     */
    public int getRow() {
        return row;
    }

    /**
     * @return The column coordinate.
     */
    public int getCol() {
        return col;
    }

    /**
     * Returns a user-friendly string representation of the position.
     * Example: "Position{row=1, col=3}"
     *
     * @return A formatted string.
     */
    @Override
    public String toString() {
        return "Position{row=" + row + ", col=" + col + '}';
    }

    /**
     * Compares this Position to another object for equality.
     * Two Position objects are considered equal if they have the same row and column.
     *
     * @param o The object to compare against.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && col == position.col;
    }

    /**
     * Generates a hash code for the Position.
     * The hash code is based on both the row and column.
     *
     * @return The hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}