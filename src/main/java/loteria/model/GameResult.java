package loteria.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents the outcome and statistics of a single, completed Lotería game.
 * This class is immutable; its state is set at creation and does not change.
 */
public class GameResult {

    private final Player winner;
    private final List<Player> participants;
    private final WinningPattern winningPattern;
    private final int totalCardsPlayed;
    private final long gameDurationMs;
    private final LocalDateTime gameDate;

    /**
     * Constructs a new GameResult.
     * The game date is automatically set to the moment of creation.
     *
     * @param winner           The player who won the game. Can be null in case of a draw or cancelled game.
     * @param participants     The list of all players who participated in the game.
     * @param winningPattern   The pattern that the winner achieved.
     * @param totalCardsPlayed The number of cards called before a winner was declared.
     * @param gameDurationMs   The total duration of the game in milliseconds.
     */
    public GameResult(Player winner, List<Player> participants, WinningPattern winningPattern, int totalCardsPlayed, long gameDurationMs) {
        this.winner = winner;
        this.participants = participants;
        this.winningPattern = winningPattern;
        this.totalCardsPlayed = totalCardsPlayed;
        this.gameDurationMs = gameDurationMs;
        this.gameDate = LocalDateTime.now(); // Set the timestamp upon creation.
    }

    /**
     * Gets the winning player.
     *
     * @return The Player who won.
     */
    public Player getWinner() {
        return winner;
    }

    /**
     * Gets the list of all participants.
     *
     * @return A List of all Players in the game.
     */
    public List<Player> getParticipants() {
        return participants;
    }

    /**
     * Gets the winning pattern.
     *
     * @return The WinningPattern that ended the game.
     */
    public WinningPattern getWinningPattern() {
        return winningPattern;
    }

    /**
     * Gets the total number of cards called during the game.
     *
     * @return The count of cards played.
     */
    public int getTotalCardsPlayed() {
        return totalCardsPlayed;
    }

    /**
     * Gets the duration of the game in milliseconds.
     *
     * @return The game's duration as a long.
     */
    public long getGameDuration() {
        return gameDurationMs;
    }

    /**
     * Gets the date and time when the game concluded.
     *
     * @return A LocalDateTime object representing the end of the game.
     */
    public LocalDateTime getGameDate() {
        return gameDate;
    }
}