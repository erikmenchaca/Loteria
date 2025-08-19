package loteria.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a player in the Lotería game.
 * Each player has a unique name, a score, and one or more boards.
 */
public class Player {

    private final String name;
    private final List<PlayerBoard> boards;
    private int score;
    private boolean isReady;

    /**
     * Constructs a new Player with a given name.
     *
     * @param name The name of the player. Cannot be null or empty.
     */
    public Player(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be null or empty.");
        }
        this.name = name;
        this.boards = new ArrayList<>();
        this.score = 0;
        this.isReady = false;
    }

    /**
     * @return The player's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Adds a PlayerBoard for the player to use in the game.
     *
     * @param board The board to add.
     */
    public void addBoard(PlayerBoard board) {
        this.boards.add(board);
    }

    /**
     * @return An unmodifiable list of the player's boards.
     */
    public List<PlayerBoard> getBoards() {
        return Collections.unmodifiableList(boards);
    }

    /**
     * Marks a given card on all of the player's boards.
     * This method is typically called by the LoteriaGame after a card is drawn.
     *
     * @param card The card to mark.
     */
    public void markCard(LoteriaCard card) {
        for (PlayerBoard board : boards) {
            board.markCard(card);
        }
    }

    /**
     * Checks if any of the player's boards have completed the given pattern.
     * Note: Final win validation should be performed by the LoteriaGame class
     * to verify against the official list of called cards.
     *
     * @param pattern The winning pattern to check for.
     * @param calledCards The official list of cards called in the game.
     * @return true if at least one board has the pattern, false otherwise.
     */
    public boolean hasWinningPattern(WinningPattern pattern, List<LoteriaCard> calledCards) {
        for (PlayerBoard board : boards) {
            if (board.checkPattern(pattern, calledCards)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return The player's current score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Adds the specified number of points to the player's score.
     *
     * @param points The points to add.
     */
    public void addScore(int points) {
        this.score += points;
    }

    /**
     * Sets the player's ready status.
     *
     * @param ready true if the player is ready to start, false otherwise.
     */
    public void setReady(boolean ready) {
        isReady = ready;
    }

    /**
     * @return true if the player is ready, false otherwise.
     */
    public boolean isReady() {
        return isReady;
    }

    /**
     * Two players are considered equal if they have the same name (case-insensitive).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return name.equalsIgnoreCase(player.name);
    }

    /**
     * The hash code is based on the player's name (converted to lowercase).
     */
    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }
}