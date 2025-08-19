package loteria.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A utility class that serves as a factory for creating standard, common WinningPattern objects.
 * This class cannot be instantiated.
 */
public final class StandardPatterns {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private StandardPatterns() {}

    /**
     * Creates a pattern for a "Full Card" win (tabla llena).
     *
     * @param boardSize The dimension of the board (e.g., 4 for a 4x4 board).
     * @return A WinningPattern object for a full card.
     */
    public static WinningPattern createFullCard(int boardSize) {
        List<Position> positions = new ArrayList<>(boardSize * boardSize);
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                positions.add(new Position(row, col));
            }
        }
        return new WinningPattern("Full Card", PatternType.FULL_CARD, positions, 25);
    }

    /**
     * Creates a pattern for a "Four Corners" win.
     *
     * @param boardSize The dimension of the board.
     * @return A WinningPattern object for the four corners.
     */
    public static WinningPattern createFourCorners(int boardSize) {
        List<Position> positions = new ArrayList<>(4);
        int last = boardSize - 1;
        positions.add(new Position(0, 0));
        positions.add(new Position(0, last));
        positions.add(new Position(last, 0));
        positions.add(new Position(last, last));
        return new WinningPattern("Four Corners", PatternType.FOUR_CORNERS, positions, 10);
    }

    /**
     * Creates a pattern for a single horizontal line.
     *
     * @param boardSize The dimension of the board.
     * @param row       The row index (0-based) for the line.
     * @return A WinningPattern object for the specified horizontal line.
     */
    public static WinningPattern createHorizontalLine(int boardSize, int row) {
        List<Position> positions = new ArrayList<>(boardSize);
        for (int col = 0; col < boardSize; col++) {
            positions.add(new Position(row, col));
        }
        return new WinningPattern("Horizontal Line", PatternType.HORIZONTAL_LINE, positions, 5);
    }

    /**
     * Creates a pattern for a single vertical line.
     *
     * @param boardSize The dimension of the board.
     * @param col       The column index (0-based) for the line.
     * @return A WinningPattern object for the specified vertical line.
     */
    public static WinningPattern createVerticalLine(int boardSize, int col) {
        List<Position> positions = new ArrayList<>(boardSize);
        for (int row = 0; row < boardSize; row++) {
            positions.add(new Position(row, col));
        }
        return new WinningPattern("Vertical Line", PatternType.VERTICAL_LINE, positions, 5);
    }

    /**
     * Creates a pattern for one of the two main diagonals.
     *
     * @param boardSize   The dimension of the board.
     * @param leftToRight true for top-left to bottom-right, false for top-right to bottom-left.
     * @return A WinningPattern object for the specified diagonal.
     */
    public static WinningPattern createDiagonalLine(int boardSize, boolean leftToRight) {
        List<Position> positions = new ArrayList<>(boardSize);
        String name = leftToRight ? "Diagonal (L-R)" : "Diagonal (R-L)";
        for (int i = 0; i < boardSize; i++) {
            if (leftToRight) {
                positions.add(new Position(i, i));
            } else {
                positions.add(new Position(i, boardSize - 1 - i));
            }
        }
        return new WinningPattern(name, PatternType.DIAGONAL_LINE, positions, 8);
    }

    /**
     * Gathers all standard, applicable patterns for a given board size.
     *
     * @param boardSize The dimension of the board.
     * @return A List of all standard WinningPattern objects.
     */
    public static List<WinningPattern> getAllStandardPatterns(int boardSize) {
        List<WinningPattern> allPatterns = new ArrayList<>();

        // Add all horizontal and vertical lines
        for (int i = 0; i < boardSize; i++) {
            allPatterns.add(createHorizontalLine(boardSize, i));
            allPatterns.add(createVerticalLine(boardSize, i));
        }

        // Add both diagonals
        allPatterns.add(createDiagonalLine(boardSize, true));
        allPatterns.add(createDiagonalLine(boardSize, false));

        // Add corners and full card
        allPatterns.add(createFourCorners(boardSize));
        allPatterns.add(createFullCard(boardSize));

        return allPatterns;
    }
}