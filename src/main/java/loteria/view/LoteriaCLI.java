package loteria.view;

import loteria.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Implements a command-line interface (CLI) for the Lotería game.
 * Handles user input, interacts with the game model, and displays output to the console.
 */
public class LoteriaCLI {

    private static final int MAX_PLAYERS = 4;
    private static final int BOARD_SIZE = 4;

    private LoteriaGame game;
    private final Scanner scanner;
    private final Map<String, WinningPattern> standardPatterns;

    /**
     * Constructs the CLI and initializes the first game instance.
     */
    public LoteriaCLI() {
        this.scanner = new Scanner(System.in);
        this.game = new LoteriaGame(MAX_PLAYERS);
        this.standardPatterns = initializePatterns();
    }

    public static void main(String[] args) {
        LoteriaCLI cli = new LoteriaCLI();
        cli.run();
    }

    /**
     * Main application loop.
     */
    public void run() {
        System.out.println("--- ¡Bienvenido a Lotería! ---");
        displayHelp();

        boolean running = true;
        while (running) {
            System.out.print("\nCommand: ");
            String input = scanner.nextLine().trim().toLowerCase();
            String[] parts = input.split("\\s+");
            String command = parts[0];

            try {
                switch (command) {
                    case "help":
                        displayHelp();
                        break;
                    case "join":
                        handleJoin(parts);
                        break;
                    case "start":
                        handleStart();
                        break;
                    case "call":
                        handleCall();
                        break;
                    case "board":
                        handleBoard(parts);
                        break;
                    case "win":
                        handleWin(parts);
                        break;
                    case "new":
                        handleNew();
                        break;
                    case "stats":
                        displayStatistics();
                        break;
                    case "quit":
                        running = false;
                        break;
                    default:
                        System.out.println("Unknown command. Type 'help' for a list of commands.");
                }
            } catch (LoteriaException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
        System.out.println("¡Gracias por jugar! Goodbye!");
        scanner.close();
    }

    /**
     * Join a player to the game with optional number of boards.
     */
    private void handleJoin(String[] parts) throws LoteriaException {
        if (parts.length < 3) {
            System.out.println("Usage: join <player_name> <num_boards>");
            return;
        }
        String playerName = parts[1];
        int numBoards;
        try {
            numBoards = Integer.parseInt(parts[2]);
            if (numBoards < 1 || numBoards > 4) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("Number of boards must be 1-4.");
            return;
        }

        Player newPlayer = new Player(playerName);
        for (int i = 0; i < numBoards; i++) {
            newPlayer.addBoard(new PlayerBoard(BOARD_SIZE, newPlayer));
        }
        game.addPlayer(newPlayer);
        System.out.println(playerName + " has joined with " + numBoards + " board(s).");
    }

    private void handleStart() throws LoteriaException {
        game.startGame();
        System.out.println("--- Game Started! ---");
        System.out.println("Boards have been generated for " + game.getPlayers().size() + " players. Good luck!");
    }

    /**
     * Call the next card and automatically mark it on all players' boards.
     */
    private void handleCall() throws LoteriaException {
        LoteriaCard card = game.callNextCard();
        System.out.println("Card called: " + card.getSpanishName());
        System.out.println("   \"" + card.getRiddle() + "\"");

        for (Player player : game.getPlayers()) {
            for (PlayerBoard board : player.getBoards()) {
                board.markCard(card);
            }
        }
    }

    /**
     * Display a player's board; optionally select a specific board.
     */
    private void handleBoard(String[] parts) {
        if (parts.length < 2) {
            System.out.println("Usage: board <player_name> [board_number]");
            return;
        }
        String playerName = parts[1];
        int boardIndex = 0; // default first board
        if (parts.length >= 3) {
            try {
                boardIndex = Integer.parseInt(parts[2]) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Invalid board number. Defaulting to 1.");
            }
        }

        Player player = game.getPlayers().stream()
                .filter(p -> p.getName().equalsIgnoreCase(playerName))
                .findFirst().orElse(null);

        if (player == null) {
            System.out.println("Player '" + playerName + "' not found.");
            return;
        }

        if (boardIndex < 0 || boardIndex >= player.getBoards().size()) {
            System.out.println("Invalid board number. Player has " + player.getBoards().size() + " board(s).");
            return;
        }

        displayBoard(player.getBoards().get(boardIndex));
    }

    /**
     * Allow a player to claim a win on any of their boards.
     */
    private void handleWin(String[] parts) {
        if (parts.length < 3) {
            System.out.println("Usage: win <player_name> <pattern_name>");
            System.out.println("Available patterns: fourcorners, horizontal, vertical, diagonal, fullcard");
            return;
        }
        String playerName = parts[1];
        String patternName = parts[2];

        Player player = game.getPlayers().stream()
                .filter(p -> p.getName().equalsIgnoreCase(playerName))
                .findFirst().orElse(null);

        WinningPattern pattern = standardPatterns.get(patternName);

        if (player == null) {
            System.out.println("Player '" + playerName + "' not found.");
            return;
        }
        if (pattern == null) {
            System.out.println("Pattern '" + patternName + "' is not valid.");
            return;
        }

        boolean winFound = false;
        int winningBoardIndex = -1;
        for (int i = 0; i < player.getBoards().size(); i++) {
            if (game.validateWin(player, pattern)) { // validate specific board
                    System.out.println("¡LOTERÍA! The win is valid! " + player.getName() + " is the winner!");

                winFound = true;
                winningBoardIndex = i;
                break;
            }
        }

        if (winFound) {
            System.out.println("¡LOTERÍA! " + player.getName() + " wins on Board #" + (winningBoardIndex + 1) + " with pattern " + patternName + "!");
            displayBoard(player.getBoards().get(winningBoardIndex));
        } else {
            System.out.println("Invalid claim. The game continues!");
        }
    }

    private void handleNew() {
        System.out.println("Starting a new game...");
        this.game = new LoteriaGame(MAX_PLAYERS);
    }

    /**
     * Display simple statistics for the game.
     */
    private void displayStatistics() {
    System.out.println("--- Game Statistics ---");
    Map<String, Object> stats = game.getStatistics();

    // Called cards
    List<LoteriaCard> calledCards = (List<LoteriaCard>) stats.get("calledCards");
    System.out.println("Total cards called: " + calledCards.size());

    // Remaining cards
    System.out.println("Cards remaining: " + stats.get("remainingCards"));

    // Marks per player
    System.out.println("Marks per player:");
    Map<String, Integer> playerMarks = (Map<String, Integer>) stats.get("playerMarks");
    for (Map.Entry<String, Integer> entry : playerMarks.entrySet()) {
        System.out.println("  " + entry.getKey() + ": " + entry.getValue());
    }

    // Winner
    String winner = (String) stats.get("winner");
    System.out.println("Winner: " + (winner != null ? winner : "None yet"));
}


    private void displayHelp() {
        System.out.println("Available Commands:");
        System.out.println("  join <name> <num_boards> - Player joins with 1-4 boards.");
        System.out.println("  start                     - Starts the game with the joined players.");
        System.out.println("  call                      - The announcer calls the next card.");
        System.out.println("  board <name> [num]        - Display a player's current board (optional board number).");
        System.out.println("  win <name> <pattern>      - Player claims a win with a pattern.");
        System.out.println("  stats                     - Display current game statistics.");
        System.out.println("  new                       - Resets for a new game.");
        System.out.println("  help                      - Shows this help menu.");
        System.out.println("  quit                      - Exits the application.");
    }

    private void displayBoard(PlayerBoard board) {
        System.out.println("\n--- " + board.getOwner().getName() + "'s Board ---");
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                LoteriaCard card = board.getCard(row, col);
                boolean isMarked = board.isMarked(row, col);
                String paddedName = String.format("%-15s", card.getSpanishName());
                System.out.print("[" + (isMarked ? "*" : " ") + paddedName + "]");
            }
            System.out.println();
        }
        System.out.println("--------------------");
    }

    private Map<String, WinningPattern> initializePatterns() {
        Map<String, WinningPattern> patterns = new HashMap<>();
        patterns.put("fourcorners", StandardPatterns.createFourCorners(BOARD_SIZE));
        patterns.put("horizontal", StandardPatterns.createHorizontalLine(BOARD_SIZE, 0));
        patterns.put("vertical", StandardPatterns.createVerticalLine(BOARD_SIZE, 0));
        patterns.put("diagonal", StandardPatterns.createDiagonalLine(BOARD_SIZE, true));
        patterns.put("fullcard", StandardPatterns.createFullCard(BOARD_SIZE));
        return patterns;
    }
}
