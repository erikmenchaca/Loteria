package loteria.model;

/**
 * An enumeration representing the different states of a Lotería game session.
 * This is used by the LoteriaGame class to control the flow and allowed actions
 * at any given moment.
 */
public enum GameState {
    /**
     * The initial state when a game is created but not yet ready to start.
     * In this state, players can join the game.
     */
    WAITING_FOR_PLAYERS,

    /**
     * The state when enough players have joined and the game is ready to begin,
     * but the first card has not yet been called.
     */
    READY_TO_START,

    /**
     * The main state of the game where cards are being actively called and
     * players are marking their boards.
     */
    IN_PROGRESS,

    /**
     * The state when the game is temporarily stopped. No cards can be called,
     * but the game can be resumed.
     */
    PAUSED,

    /**
     * The final state after a player has won the game. No further actions
     * can be taken until the game is reset.
     */
    FINISHED,

    /**
     * A terminal state if the game was ended prematurely before a winner
     * was declared.
     */
    CANCELLED
}