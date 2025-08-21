package loteria.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The main orchestrator for the Lotería game.
 * This class manages the game state, players, deck, and caller, and enforces the game rules.
 */
public class LoteriaGame {

    private final Deck deck;
    private final List<Player> players;
    private final Caller caller;
    private GameState gameState;
    private final List<WinningPattern> patterns;
    private LoteriaCard currentCard;
    private final List<LoteriaCard> calledCards;
    private final int maxPlayers;
    private Player winner;

    public LoteriaGame(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        this.deck = new Deck();
        this.caller = new Caller(deck);
        this.players = new ArrayList<>();
        this.patterns = new ArrayList<>();
        this.calledCards = new ArrayList<>();
        this.gameState = GameState.WAITING_FOR_PLAYERS;
        this.winner = null;
        this.currentCard = null;
    }

    public Deck getDeck() {
        return deck;
    }

    public void addPlayer(Player player) throws LoteriaException {
        if (gameState != GameState.WAITING_FOR_PLAYERS) {
            throw new LoteriaException("Cannot add players after the game has started.");
        }
        if (players.size() >= maxPlayers) {
            throw new LoteriaException("The game is full. Cannot add more players.");
        }
        players.add(player);
    }

    /**
     * Starts the game.
     * This shuffles the deck, generates boards for all players, and moves the state to IN_PROGRESS.
     *
     * @throws LoteriaException if there are no players or the game has already started.
     */
    public void startGame() throws LoteriaException {
        if (gameState != GameState.WAITING_FOR_PLAYERS) {
            throw new LoteriaException("The game has already started or is finished.");
        }
        if (players.isEmpty()) {
            throw new LoteriaException("Cannot start a game with no players.");
        }

        // Reset the deck to ensure it's fresh
        deck.reset();

        // Generate boards for all players
        for (Player player : players) {
            for (PlayerBoard board : player.getBoards()) {
                board.generateBoard(this.deck);
            }
        }

        // Reset the deck again for the caller to use during the game
        deck.reset();

        this.gameState = GameState.IN_PROGRESS;
    }

    public LoteriaCard callNextCard() throws LoteriaException {
        if (gameState != GameState.IN_PROGRESS) {
            throw new LoteriaException("Game is not currently in progress.");
        }
        if (!caller.hasMoreCards()) {
            throw new LoteriaException("The deck is empty!");
        }

        this.currentCard = caller.callCard();
        this.calledCards.add(currentCard);

        for (Player player : players) {
            player.markCard(currentCard);
        }

        return currentCard;
    }

    public boolean validateWin(Player player, WinningPattern pattern) {
        if (gameState != GameState.IN_PROGRESS) {
            return false;
        }

        for (PlayerBoard board : player.getBoards()) {
            if (board.checkPattern(pattern, this.calledCards)) {
                this.winner = player;
                this.gameState = GameState.FINISHED;
                return true;
            }
        }

        return false;
    }

    public boolean isGameOver() {
        return gameState == GameState.FINISHED || gameState == GameState.CANCELLED;
    }

    // --- GETTERS ---

    public Player getWinner() {
        return winner;
    }

    public LoteriaCard getCurrentCard() {
        return currentCard;
    }

    public List<LoteriaCard> getCalledCards() {
        return Collections.unmodifiableList(calledCards);
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public GameState getGameState() {
        return gameState;
    }

    public int remainingCards() {
        return deck.getRemainingCount();
    }

    /**
     * Returns the number of marked cards for a specific player.
     */
    public int countMarked(Player player) {
        int total = 0;
        for (PlayerBoard board : player.getBoards()) {
            total += board.getMarkedCount();  // PlayerBoard has countMarked()
        }
        return total;
    }

    /**
     * Returns statistics about the current game state.
     * 
     * @return a map containing useful info like called cards, remaining cards, and player marks
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();

        // Called cards
        stats.put("calledCards", List.copyOf(calledCards));

        // Remaining cards in deck
        stats.put("remainingCards", deck.getRemainingCount());

        // Marks per player
        Map<String, Integer> playerMarks = new HashMap<>();
        for (Player player : players) {
            playerMarks.put(player.getName(), countMarked(player));
        }
        stats.put("playerMarks", playerMarks);

        // Winner info
        stats.put("winner", winner != null ? winner.getName() : null);

        return stats;
    }
}
