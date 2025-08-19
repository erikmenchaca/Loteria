package loteria.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    /**
     * Constructs a new Lotería game with a specified maximum number of players.
     *
     * @param maxPlayers The maximum number of players that can join this game.
     */
    public LoteriaGame(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        this.deck = new Deck();
        this.caller = new Caller(deck);
        this.players = new ArrayList<>();
        this.patterns = new ArrayList<>(); // Winning patterns can be added later
        this.calledCards = new ArrayList<>();
        this.gameState = GameState.WAITING_FOR_PLAYERS;
        this.winner = null;
        this.currentCard = null;
    }

    /**
     * Adds a player to the game.
     *
     * @param player The player to add.
     * @throws LoteriaException if the game is not in the WAITING_FOR_PLAYERS state or is full.
     */
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
        
        deck.reset(); // Ensure deck is fresh and shuffled
        // Generate a unique board for each player
        for (Player player : players) {
            for (PlayerBoard board : player.getBoards()) {
                board.generateBoard(deck);
            }
        }
        
        this.gameState = GameState.IN_PROGRESS;
    }

    /**
     * The caller draws the next card from the deck.
     * The card is added to the list of called cards, and all players are notified to mark their boards.
     *
     * @return The card that was just called.
     * @throws LoteriaException if the game is not in progress or the deck is empty.
     */
    public LoteriaCard callNextCard() throws LoteriaException {
        if (gameState != GameState.IN_PROGRESS) {
            throw new LoteriaException("Game is not currently in progress.");
        }
        if (!caller.hasMoreCards()) {
            throw new LoteriaException("The deck is empty!");
        }

        this.currentCard = caller.callCard();
        this.calledCards.add(currentCard);

        // Notify all players of the new card
        for (Player player : players) {
            player.markCard(currentCard);
        }
        
        return currentCard;
    }

    /**
     * Validates a player's claim to have won with a specific pattern.
     *
     * @param player  The player claiming the win.
     * @param pattern The winning pattern they are claiming.
     * @return true if the win is valid, false otherwise.
     */
    public boolean validateWin(Player player, WinningPattern pattern) {
        if (gameState != GameState.IN_PROGRESS) {
            return false; // Cannot win if game is not running
        }

        // A win is valid if any of the player's boards match the pattern
        // The PlayerBoard's checkPattern method should verify against the *called cards*.
        // For simplicity, we assume checkPattern is implemented correctly.
        for (PlayerBoard board : player.getBoards()) {
            if (board.checkPattern(pattern, this.calledCards)) {
                this.winner = player;
                this.gameState = GameState.FINISHED;
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Checks if the game has concluded.
     *
     * @return true if the game state is FINISHED or CANCELLED, false otherwise.
     */
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
}