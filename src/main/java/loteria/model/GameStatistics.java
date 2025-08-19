package loteria.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tracks and manages statistics across multiple Lotería game sessions.
 * This class is not thread-safe.
 */
public class GameStatistics {

    private int totalGames;
    private final Map<Player, Integer> playerWins;
    private final Map<LoteriaCard, Integer> cardFrequency;
    private final List<GameResult> gameHistory;

    /**
     * Constructs a new, empty GameStatistics object.
     */
    public GameStatistics() {
        this.totalGames = 0;
        this.playerWins = new HashMap<>();
        this.cardFrequency = new HashMap<>();
        this.gameHistory = new ArrayList<>();
    }

    /**
     * Records the result of a completed game and updates all relevant statistics.
     *
     * @param result      The GameResult object from the finished game.
     * @param calledCards The list of cards that were called during that game.
     */
    public void recordGame(GameResult result, List<LoteriaCard> calledCards) {
        if (result == null || calledCards == null) {
            return; // Do not record null results
        }

        this.totalGames++;
        this.gameHistory.add(result);

        // Update winner's score
        Player winner = result.getWinner();
        if (winner != null) {
            playerWins.put(winner, playerWins.getOrDefault(winner, 0) + 1);
        }

        // Update card frequency count for all cards called in the game
        for (LoteriaCard card : calledCards) {
            cardFrequency.put(card, cardFrequency.getOrDefault(card, 0) + 1);
        }
    }

    /**
     * Gets the total number of wins for a specific player.
     *
     * @param player The player to check.
     * @return The number of times the player has won.
     */
    public int getPlayerWins(Player player) {
        return playerWins.getOrDefault(player, 0);
    }

    /**
     * Gets the total number of games that have been recorded.
     *
     * @return The total number of games played.
     */
    public int getTotalGames() {
        return totalGames;
    }

    /**
     * Finds the card that has been called most frequently across all games.
     *
     * @return The most frequently called LoteriaCard, or null if no games have been played.
     */
    public LoteriaCard getMostCalledCard() {
        if (cardFrequency.isEmpty()) {
            return null;
        }

        return Collections.max(cardFrequency.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    /**
     * Calculates the win rate for a specific player.
     *
     * @param player The player whose win rate is to be calculated.
     * @return The player's win rate as a double (e.g., 0.5 for 50%).
     */
    public double getWinRate(Player player) {
        if (totalGames == 0) {
            return 0.0;
        }
        double wins = getPlayerWins(player);
        return wins / totalGames;
    }

    /**
     * Exports a summary of the current statistics as a formatted string.
     *
     * @return A String containing the statistics report.
     */
    public String exportStatistics() {
        StringBuilder report = new StringBuilder();
        report.append("--- Lotería Statistics ---\n");
        report.append("Total Games Played: ").append(totalGames).append("\n\n");

        report.append("Player Win Counts:\n");
        if (playerWins.isEmpty()) {
            report.append("  No wins recorded yet.\n");
        } else {
            for (Map.Entry<Player, Integer> entry : playerWins.entrySet()) {
                report.append("  - ").append(entry.getKey().getName())
                      .append(": ").append(entry.getValue()).append(" wins\n");
            }
        }

        LoteriaCard mostCalled = getMostCalledCard();
        report.append("\nMost Frequently Called Card:\n");
        if (mostCalled != null) {
            report.append("  ").append(mostCalled.getName())
                  .append(" (called ").append(cardFrequency.get(mostCalled)).append(" times)\n");
        } else {
            report.append("  No cards have been called yet.\n");
        }
        
        report.append("--------------------------\n");
        return report.toString();
    }
}