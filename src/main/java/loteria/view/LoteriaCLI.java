package loteria.view;

import loteria.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Implements a command-line interface (CLI) for the Lotería game.
 * This class handles user input, interacts with the game model, and displays output to the console.
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

    /**
     * The main entry point for the application.
     */
    public static void main(String[] args) {
        LoteriaCLI cli = new LoteriaCLI();
        cli.run();
    }

    /**
     * The main application loop. Reads and processes user commands until 'quit' is entered.
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

    private void handleJoin(String[] parts) throws LoteriaException {
        if (parts.length < 2) {
            System.out.println("Usage: join <player_name>");
            return;
        }
        String playerName = parts[1];
        Player newPlayer = new Player(playerName);
        // Each player gets one board in this simple CLI
        newPlayer.addBoard(new PlayerBoard(BOARD_SIZE, newPlayer));
        game.addPlayer(newPlayer);
        System.out.println(playerName + " has joined the game.");
    }

    private void handleStart() throws LoteriaException {
        game.startGame();
        System.out.println("--- Game Started! ---");
        System.out.println("Boards have been generated for " + game.getPlayers().size() + " players. Good luck!");
    }



    private void handleCall() throws LoteriaException {
        LoteriaCard card = game.callNextCard();
        System.out.println("Card called: " + card.toString());
        System.out.println("   \"" + card.getRiddle() + "\"");
    }

    private void handleBoard(String[] parts) {
        if (parts.length < 2) {
            System.out.println("Usage: board <player_name>");
            return;
        }
        String playerName = parts[1];
        Player foundPlayer = null;
        for (Player p : game.getPlayers()) {
            if (p.getName().equalsIgnoreCase(playerName)) {
                foundPlayer = p;
                break;
            }
        }

        if (foundPlayer == null) {
            System.out.println("Player '" + playerName + "' not found.");
        } else {
            // Assuming each player has one board for this CLI
            displayBoard(foundPlayer.getBoards().get(0));
        }
    }

    private void handleWin(String[] parts) {
        if (parts.length < 3) {
            System.out.println("Usage: win <player_name> <pattern_name>");
            System.out.println("Available patterns: fourcorners, horizontal, vertical, diagonal, fullcard");
            return;
        }
        String playerName = parts[1];
        String patternName = parts[2];

        Player claimingPlayer = game.getPlayers().stream()
                .filter(p -> p.getName().equalsIgnoreCase(playerName))
                .findFirst().orElse(null);

        WinningPattern claimedPattern = standardPatterns.get(patternName);

        if (claimingPlayer == null) {
            System.out.println("Player '" + playerName + "' not found.");
            return;
        }
        if (claimedPattern == null) {
            System.out.println("Pattern '" + patternName + "' is not a valid pattern type.");
            return;
        }

        System.out.println(playerName + " claims a win with pattern '" + patternName + "'. Checking board...");

        if (game.validateWin(claimingPlayer, claimedPattern)) {
            System.out.println("¡LOTERÍA! The win is valid! " + claimingPlayer.getName() + " is the winner!");
            displayBoard(claimingPlayer.getBoards().get(0));
        } else {
            System.out.println("Invalid claim. The game continues!");
        }
    }

    private void handleNew() {
        System.out.println("Starting a new game...");
        this.game = new LoteriaGame(MAX_PLAYERS);
    }

    private void displayHelp() {
        System.out.println("Available Commands:");
        System.out.println("  join <name>      - A player joins the game (before it starts).");
        System.out.println("  start            - Starts the game with the joined players.");
        System.out.println("  call             - The announcer calls the next card.");
        System.out.println("  board <name>     - Display a player's current board.");
        System.out.println("  win <name> <pat> - A player claims a win with a pattern.");
        System.out.println("  new              - Resets for a new game.");
        System.out.println("  help             - Shows this help menu.");
        System.out.println("  quit             - Exits the application.");
    }

    private void displayBoard(PlayerBoard board) {
        System.out.println("\n--- " + board.getOwner().getName() + "'s Board ---");
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                LoteriaCard card = board.getCard(row, col);
                boolean isMarked = board.isMarked(row, col);
                String cardName = card.getSpanishName();
                // Pad the string to make columns align
                String paddedName = String.format("%-15s", cardName);
                System.out.print("[" + (isMarked ? "*" : " ") + paddedName + "]");
            }
            System.out.println();
        }
        System.out.println("--------------------");
    }

    private Map<String, WinningPattern> initializePatterns() {
        Map<String, WinningPattern> patterns = new HashMap<>();
        // Note: For simplicity, we create generic line/diagonal patterns.
        // A full implementation would let the player specify *which* line.
        patterns.put("fourcorners", StandardPatterns.createFourCorners(BOARD_SIZE));
        patterns.put("horizontal", StandardPatterns.createHorizontalLine(BOARD_SIZE, 0)); // Checks first line
        patterns.put("vertical", StandardPatterns.createVerticalLine(BOARD_SIZE, 0)); // Checks first column
        patterns.put("diagonal", StandardPatterns.createDiagonalLine(BOARD_SIZE, true)); // Checks L-R
        patterns.put("fullcard", StandardPatterns.createFullCard(BOARD_SIZE));
        return patterns;
    }
}