package loteria.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import loteria.model.*;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A complete Lotería game GUI built programmatically with JavaFX.
 * This class creates all UI components and handles all game events without FXML.
 */
public class LoteriaGUI extends Application {

    private static final int MAX_PLAYERS = 4;
    private static final int BOARD_SIZE = 4;

    private LoteriaGame game;
    private Player currentPlayer; // Tracks the player whose board is currently displayed

    // --- UI Components ---
    private VBox setupPanel;
    private VBox gamePanel;
    private ListView<String> playerListView;
    private ComboBox<Player> playerSelectorComboBox;
    private ComboBox<Integer> boardSelectorComboBox;
    private Button startGameButton;
    private Button callCardButton;
    private Button loteriaButton;
    private ImageView callerCardImageView;
    private Label callerCardNameLabel;
    private GridPane playerBoardGrid;
    private Label statusLabel;
    private int currentBoardIndex = 0;

    @Override
    public void start(Stage primaryStage) {
        game = new LoteriaGame(MAX_PLAYERS);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setPrefSize(800, 600);
        root.setStyle("-fx-background-color: #F5F5DC;");

        // --- Left Panel: Caller Display ---
        callerCardImageView = new ImageView();
        callerCardImageView.setFitHeight(180);
        callerCardImageView.setFitWidth(120);
        callerCardImageView.setPreserveRatio(true);
        callerCardNameLabel = new Label("Waiting to start...");
        callerCardNameLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        Label callerTitle = new Label("Card Called");
        callerTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        VBox leftPanel = new VBox(10, callerTitle, callerCardImageView, callerCardNameLabel);
        leftPanel.setAlignment(Pos.TOP_CENTER);
        leftPanel.setPadding(new Insets(10, 0, 0, 10));
        root.setLeft(leftPanel);

        // --- Center Panel: Player's Board ---
        playerBoardGrid = new GridPane();
        playerBoardGrid.setAlignment(Pos.CENTER);
        playerBoardGrid.setHgap(10);
        playerBoardGrid.setVgap(10);
        root.setCenter(playerBoardGrid);

        // --- Right Panel: Controls ---
        // 1. Setup Panel
        Label playersTitle = new Label("Players");
        playersTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        TextField playerNameField = new TextField();
        playerNameField.setPromptText("Enter your name");
        Button addPlayerButton = new Button("Add Player");
        addPlayerButton.setPrefWidth(150);
        playerListView = new ListView<>();
        startGameButton = new Button("Start Game");
        startGameButton.setPrefWidth(150);
        startGameButton.setDisable(true);
        setupPanel = new VBox(10, playersTitle, playerNameField, addPlayerButton, playerListView, startGameButton);
        setupPanel.setAlignment(Pos.TOP_CENTER);

        // 2. Game Panel
        Label boardViewLabel = new Label("Viewing Board For:");
        playerSelectorComboBox = new ComboBox<>();
        playerSelectorComboBox.setPrefWidth(150);
        
        Label boardNumberLabel = new Label("Board Number:");
        boardSelectorComboBox = new ComboBox<>();
        boardSelectorComboBox.setPrefWidth(150);
        
        callCardButton = new Button("Call Next Card");
        callCardButton.setPrefSize(150, 50);
        loteriaButton = new Button("¡Lotería!");
        loteriaButton.setPrefSize(150, 50);
        Button newGameButton = new Button("New Game");
        newGameButton.setPrefWidth(150);
        gamePanel = new VBox(10, boardViewLabel, playerSelectorComboBox, boardNumberLabel, boardSelectorComboBox, 
                            new Separator(), callCardButton, loteriaButton, newGameButton);
        gamePanel.setAlignment(Pos.TOP_CENTER);
        gamePanel.setSpacing(15);
        gamePanel.setVisible(false);

        VBox rightPanel = new VBox(10, setupPanel, gamePanel);
        rightPanel.setPadding(new Insets(10, 10, 0, 0));
        root.setRight(rightPanel);

        // --- Bottom Panel: Status Bar ---
        statusLabel = new Label("Welcome! Add players to begin.");
        statusLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        root.setBottom(statusLabel);
        BorderPane.setMargin(statusLabel, new Insets(10, 0, 5, 10));

        // --- Event Handlers ---
        addPlayerButton.setOnAction(event -> handleAddPlayer(playerNameField));
        startGameButton.setOnAction(event -> handleStartGame());
        callCardButton.setOnAction(event -> handleCallCard());
        loteriaButton.setOnAction(event -> handleLoteriaButton());
        newGameButton.setOnAction(event -> handleNewGame());
        playerSelectorComboBox.setOnAction(event -> handlePlayerSelection());
        boardSelectorComboBox.setOnAction(event -> handleBoardSelection());

        // --- Final Scene and Stage Setup ---
        Scene scene = new Scene(root);
        primaryStage.setTitle("¡Lotería!");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        
        // Initialize the board display with waiting message
        drawPlayerBoard();
        
        primaryStage.show();
    }

    private void handleAddPlayer(TextField nameField) {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            statusLabel.setText("Player name cannot be empty.");
            return;
        }
        try {
            Player newPlayer = new Player(name);
            
            // Ask how many boards the player wants
            TextInputDialog boardDialog = new TextInputDialog("1");
            boardDialog.setTitle("Number of Boards");
            boardDialog.setHeaderText("How many tablas for " + name + "?");
            boardDialog.setContentText("Enter number of boards (1-4):");
            
            Optional<String> result = boardDialog.showAndWait();
            int numBoards = 1; // Default to 1 board
            
            if (result.isPresent()) {
                try {
                    numBoards = Integer.parseInt(result.get());
                    if (numBoards < 1 || numBoards > 4) {
                        numBoards = 1; // Default to 1 if invalid input
                        statusLabel.setText("Invalid number of boards. Using 1 board for " + name + ".");
                    }
                } catch (NumberFormatException e) {
                    numBoards = 1;
                    statusLabel.setText("Invalid input. Using 1 board for " + name + ".");
                }
            }
            
            // Create the specified number of boards
            for (int i = 0; i < numBoards; i++) {
                newPlayer.addBoard(new PlayerBoard(BOARD_SIZE, newPlayer));
            }
            
            game.addPlayer(newPlayer);

            playerListView.getItems().add(name + " (" + numBoards + " board" + (numBoards > 1 ? "s" : "") + ")");
            playerSelectorComboBox.getItems().add(newPlayer);
            nameField.clear();
            statusLabel.setText(name + " has joined with " + numBoards + " board(s).");
            startGameButton.setDisable(false);
            
            // Only set current player if game hasn't started yet
            if (currentPlayer == null && game.getGameState() == GameState.WAITING_FOR_PLAYERS) {
                currentPlayer = newPlayer;
                playerSelectorComboBox.setValue(currentPlayer);
                // Don't draw board yet - wait until game starts
            }
        } catch (LoteriaException e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    private void handleStartGame() {
        try {
            // The model now handles all board generation internally
            game.startGame();

            setupPanel.setVisible(false);
            gamePanel.setVisible(true);
            statusLabel.setText("Game started! Click 'Call Next Card' to begin.");
            drawPlayerBoard();
        } catch (LoteriaException e) {
            statusLabel.setText("Error: " + e.getMessage());
            e.printStackTrace(); // For debugging
        }
    }

    private void handleCallCard() {
        try {
            LoteriaCard card = game.callNextCard();
            
            Image cardImage = loadImage(card.getCardNumber() + ".png");
            if (cardImage != null) {
                callerCardImageView.setImage(cardImage);
            } else {
                // Clear the image and rely on text
                callerCardImageView.setImage(null);
            }
            
            callerCardNameLabel.setText("#" + card.getCardNumber() + ": " + card.getSpanishName() + 
                                      (cardImage == null ? "\n(" + card.getName() + ")" : ""));
            statusLabel.setText("Called: " + card.getName());
            updateBoardHighlights();
        } catch (LoteriaException e) {
            statusLabel.setText("The deck is empty! Game over.");
            callCardButton.setDisable(true);
        }
    }

    private void handleLoteriaButton() {
        Player claimingPlayer = playerSelectorComboBox.getValue();
        if (claimingPlayer == null) {
            statusLabel.setText("Select a player before claiming a win.");
            return;
        }

        // Create all possible patterns for the board size
        List<WinningPattern> patterns = StandardPatterns.getAllStandardPatterns(BOARD_SIZE);
        
        // Check if the player has ANY valid winning pattern on ANY of their boards
        WinningPattern winningPattern = null;
        int winningBoardIndex = -1;
        
        for (int boardIndex = 0; boardIndex < claimingPlayer.getBoards().size(); boardIndex++) {
            PlayerBoard board = claimingPlayer.getBoards().get(boardIndex);
            for (WinningPattern pattern : patterns) {
                if (board.checkPattern(pattern, game.getCalledCards())) {
                    winningPattern = pattern;
                    winningBoardIndex = boardIndex;
                    break;
                }
            }
            if (winningPattern != null) break; // Found a win, stop searching
        }
        
        if (winningPattern != null) {
            // Now officially validate the win (this will change game state)
            game.validateWin(claimingPlayer, winningPattern);
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("¡Lotería!");
            alert.setHeaderText("WINNER!");
            alert.setContentText(claimingPlayer.getName() + " has won with the pattern: " + winningPattern.getName() + 
                               "\nWinning Board: #" + (winningBoardIndex + 1));
            alert.showAndWait();
            callCardButton.setDisable(true);
            loteriaButton.setDisable(true);
            statusLabel.setText("¡" + claimingPlayer.getName() + " WINS with " + winningPattern.getName() + 
                              " on Board #" + (winningBoardIndex + 1) + "!");
            
            // Switch to show the winning board
            currentBoardIndex = winningBoardIndex;
            boardSelectorComboBox.setValue(winningBoardIndex + 1);
            drawPlayerBoard();
            updateBoardHighlights();
        } else {
            statusLabel.setText(claimingPlayer.getName() + "'s claim was invalid. The game continues.");
        }
    }

    private void handleNewGame() {
        game = new LoteriaGame(MAX_PLAYERS);
        currentPlayer = null;
        currentBoardIndex = 0;
        playerListView.getItems().clear();
        playerSelectorComboBox.getItems().clear();
        boardSelectorComboBox.getItems().clear();
        playerBoardGrid.getChildren().clear();
        callerCardImageView.setImage(null);
        callerCardNameLabel.setText("Waiting to start...");
        statusLabel.setText("Welcome! Add players to begin.");
        gamePanel.setVisible(false);
        setupPanel.setVisible(true);
        callCardButton.setDisable(false);
        loteriaButton.setDisable(false);
        startGameButton.setDisable(true);
    }

    private void handlePlayerSelection() {
        Player selectedPlayer = playerSelectorComboBox.getValue();
        if (selectedPlayer != null) {
            currentPlayer = selectedPlayer;
            currentBoardIndex = 0; // Reset to first board
            
            // Update board selector with available boards
            boardSelectorComboBox.getItems().clear();
            for (int i = 0; i < selectedPlayer.getBoards().size(); i++) {
                boardSelectorComboBox.getItems().add(i + 1); // Display as 1-based
            }
            boardSelectorComboBox.setValue(1); // Select first board
            
            drawPlayerBoard();
            // Only update highlights if game is in progress
            if (game.getGameState() == GameState.IN_PROGRESS) {
                updateBoardHighlights();
            }
        }
    }
    
    private void handleBoardSelection() {
        Integer selectedBoardNumber = boardSelectorComboBox.getValue();
        if (selectedBoardNumber != null && currentPlayer != null) {
            currentBoardIndex = selectedBoardNumber - 1; // Convert to 0-based
            drawPlayerBoard();
            // Only update highlights if game is in progress
            if (game.getGameState() == GameState.IN_PROGRESS) {
                updateBoardHighlights();
            }
        }
    }

    private void drawPlayerBoard() {
        playerBoardGrid.getChildren().clear();
        
        // Don't draw board if game hasn't started yet
        if (game.getGameState() == GameState.WAITING_FOR_PLAYERS) {
            Label waitingLabel = new Label("Waiting for game to start...");
            waitingLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: gray;");
            StackPane placeholder = new StackPane(waitingLabel);
            playerBoardGrid.add(placeholder, 0, 0, 4, 4); // Span all columns and rows
            return;
        }
        
        if (currentPlayer == null) {
            statusLabel.setText("No player selected.");
            return;
        }
        
        List<PlayerBoard> boards = currentPlayer.getBoards();
        if (boards.isEmpty()) {
            statusLabel.setText("Player has no boards.");
            return;
        }
        
        // Use the selected board index
        if (currentBoardIndex >= boards.size()) {
            currentBoardIndex = 0; // Fallback to first board
        }
        
        PlayerBoard board = boards.get(currentBoardIndex);

        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                LoteriaCard card = board.getCard(row, col);
                if (card == null) {
                    System.err.println("Error: Card at (" + row + "," + col + ") is null. Board may not be generated.");
                    // Create a placeholder for null cards
                    Label placeholder = new Label("NULL");
                    placeholder.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                    StackPane cellPane = new StackPane(placeholder);
                    cellPane.setPrefSize(70, 100);
                    playerBoardGrid.add(cellPane, col, row);
                    continue;
                }
                
                Image cardImage = loadImage(card.getCardNumber() + ".png");
                StackPane cellPane;
                
                if (cardImage != null) {
                    // Use the actual card image
                    ImageView cardView = new ImageView(cardImage);
                    cardView.setFitHeight(100);
                    cardView.setFitWidth(70);
                    cardView.setPreserveRatio(true);
                    
                    // Create an X mark overlay (initially hidden)
                    Label xMark = new Label("X");
                    xMark.setStyle("-fx-text-fill: red; -fx-font-size: 48px; -fx-font-weight: bold; " +
                                  "-fx-background-color: rgba(255,255,255,0.8); -fx-padding: 5px; " +
                                  "-fx-border-color: red; -fx-border-width: 2px;");
                    xMark.setVisible(false); // Initially hidden
                    
                    cellPane = new StackPane(cardView, xMark);
                } else {
                    // Use text as fallback
                    Label cardLabel = new Label("#" + card.getCardNumber() + "\n" + card.getSpanishName());
                    cardLabel.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1px; " +
                                     "-fx-text-alignment: center; -fx-font-size: 10px; -fx-padding: 5px;");
                    cardLabel.setPrefSize(70, 100);
                    
                    // Create an X mark overlay for text cards too
                    Label xMark = new Label("X");
                    xMark.setStyle("-fx-text-fill: red; -fx-font-size: 24px; -fx-font-weight: bold;");
                    xMark.setVisible(false); // Initially hidden
                    
                    cellPane = new StackPane(cardLabel, xMark);
                }
                
                cellPane.setUserData(card);
                playerBoardGrid.add(cellPane, col, row);
            }
        }
    }

    private void updateBoardHighlights() {
        if (game.getGameState() != GameState.IN_PROGRESS) return;
        List<LoteriaCard> calledCards = game.getCalledCards();
        
        playerBoardGrid.getChildren().forEach(node -> {
            if (node instanceof StackPane) {
                StackPane stackPane = (StackPane) node;
                LoteriaCard card = (LoteriaCard) stackPane.getUserData();
                
                if (card != null && calledCards.contains(card)) {
                    // Show the X mark if this card has been called
                    stackPane.getChildren().forEach(child -> {
                        if (child instanceof Label && ((Label) child).getText().equals("X")) {
                            child.setVisible(true);
                        }
                    });
                } else {
                    // Hide the X mark if this card hasn't been called
                    stackPane.getChildren().forEach(child -> {
                        if (child instanceof Label && ((Label) child).getText().equals("X")) {
                            child.setVisible(false);
                        }
                    });
                }
            }
        });
    }

    private Image loadImage(String fileName) {
        String fullPath = "data/media/" + fileName;
        
        try {
            // Try method 1: Using getResourceAsStream from class loader
            InputStream imageStream = getClass().getClassLoader().getResourceAsStream(fullPath);
            if (imageStream != null) {
                return new Image(imageStream);
            }
            
            // Try method 2: Using getResourceAsStream from class
            imageStream = getClass().getResourceAsStream("/" + fullPath);
            if (imageStream != null) {
                return new Image(imageStream);
            }
            
            // Try method 3: Direct path without leading slash
            imageStream = getClass().getResourceAsStream(fullPath);
            if (imageStream != null) {
                return new Image(imageStream);
            }
            
            System.err.println("Failed to load image: " + fullPath + ". Tried multiple resource loading methods.");
            
        } catch (Exception e) {
            System.err.println("Exception loading image " + fullPath + ": " + e.getMessage());
        }
        
        // Return a placeholder image if loading fails
        return createPlaceholderImage(fileName);
    }
    
    private Image createPlaceholderImage(String fileName) {
        // Create a simple colored rectangle as placeholder
        // We can't easily create images programmatically in JavaFX without additional setup,
        // so we'll return null and handle it in the UI
        return null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}