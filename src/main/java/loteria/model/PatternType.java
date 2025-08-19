package loteria.model;

/**
 * An enumeration representing the different types of winning patterns in Lotería.
 * This helps to categorize and manage the various ways a player can win.
 */
public enum PatternType {
    /** A win by marking every single card on the board (tabla llena). */
    FULL_CARD,

    /** A win by marking the four cards in each corner of the board. */
    FOUR_CORNERS,

    /** A win by marking all cards in a single horizontal row. */
    HORIZONTAL_LINE,

    /** A win by marking all cards in a single vertical column. */
    VERTICAL_LINE,

    /** A win by marking all cards along one of the two main diagonals. */
    DIAGONAL_LINE,

    /** A win by marking the center row and center column, forming a plus sign. */
    CROSS,

    /** A win by marking both main diagonals, forming an 'X'. */
    X_PATTERN,

    /** A win by marking all cards along the outer edge of the board. */
    BORDER,
    
    /** A win by marking only the single card in the center of the board. */
    CENTER_SQUARE,

    /** A category for any other user-defined or non-standard pattern. */
    CUSTOM
}