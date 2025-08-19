package loteria.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a single Lotería board (tabla) for a player.
 * A board consists of a grid of cards and a parallel grid of markers to track called cards.
 */
public class PlayerBoard {

    private final LoteriaCard[][] grid;
    private final boolean[][] markers;
    private final int size;
    private final Player owner;

    /**
     * Constructs a new PlayerBoard for a given owner and size.
     *
     * @param size  The dimension of the square board (e.g., 4 for a 4x4 grid).
     * @param owner The Player who owns this board.
     */
    public PlayerBoard(int size, Player owner) {
        if (size <= 0) {
            throw new IllegalArgumentException("Board size must be positive.");
        }
        if (owner == null) {
            throw new IllegalArgumentException("Board must have an owner.");
        }
        this.size = size;
        this.owner = owner;
        this.grid = new LoteriaCard[size][size];
        this.markers = new boolean[size][size];
    }

    /**
     * Populates the board's grid with a random, unique selection of cards from the deck.
     *
     * @param deck The game deck to draw cards from.
     * @throws LoteriaException if the deck does not have enough unique cards to fill the board.
     */
    public void generateBoard(Deck deck) throws LoteriaException {
        int boardCardCount = size * size;
        if (deck.getAllCards().size() < boardCardCount) {
            throw new LoteriaException("Deck does not have enough cards to generate a board of size " + size);
        }

        List<LoteriaCard> cardPool = new ArrayList<>(deck.getAllCards());
        Collections.shuffle(cardPool);

        int cardIndex = 0;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                grid[row][col] = cardPool.get(cardIndex++);
            }
        }
    }

    /**
     * Marks the spot on the board corresponding to the given card, if it exists.
     *
     * @param card The card to mark.
     * @return true if the card was found and marked, false otherwise.
     */
    public boolean markCard(LoteriaCard card) {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (grid[row][col] != null && grid[row][col].equals(card)) {
                    markers[row][col] = true;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Unmarks the spot on the board corresponding to the given card.
     *
     * @param card The card to unmark.
     * @return true if the card was found and unmarked, false otherwise.
     */
    public boolean unmarkCard(LoteriaCard card) {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (grid[row][col] != null && grid[row][col].equals(card)) {
                    markers[row][col] = false;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if this board contains a specific card.
     *
     * @param card The card to search for.
     * @return true if the card is on this board, false otherwise.
     */
    public boolean hasCard(LoteriaCard card) {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (grid[row][col] != null && grid[row][col].equals(card)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if a specific position on the board is marked.
     *
     * @param row The row of the position.
     * @param col The column of the position.
     * @return true if the spot is marked, false otherwise.
     */
    public boolean isMarked(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            return false;
        }
        return markers[row][col];
    }

    /**
     * Gets the card at a specific position on the board.
     *
     * @param row The row of the card.
     * @param col The column of the card.
     * @return The LoteriaCard at the given position, or null if coordinates are invalid.
     */
    public LoteriaCard getCard(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            return null;
        }
        return grid[row][col];
    }

    /**
     * Validates if the board has achieved a winning pattern against the official list of called cards.
     *
     * @param pattern     The winning pattern to check for.
     * @param calledCards The official list of cards that have been called in the game.
     * @return true if all cards required by the pattern have been called, false otherwise.
     */
    public boolean checkPattern(WinningPattern pattern, List<LoteriaCard> calledCards) {
        Set<LoteriaCard> calledCardsSet = new HashSet<>(calledCards); // For efficient lookups
        List<Position> requiredPositions = pattern.getRequiredPositions();

        for (Position pos : requiredPositions) {
            LoteriaCard cardOnBoard = getCard(pos.getRow(), pos.getCol());
            if (cardOnBoard == null || !calledCardsSet.contains(cardOnBoard)) {
                return false; // A required card has not been called yet
            }
        }
        return true; // All required cards have been called
    }

    /**
     * @return The total number of marked cards on the board.
     */
    public int getMarkedCount() {
        int count = 0;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (markers[row][col]) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * @return The size (dimension) of the board.
     */
    public int getSize() {
        return size;
    }

    /**
     * @return The Player who owns this board.
     */
    public Player getOwner() {
        return owner;
    }
}