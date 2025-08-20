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
    private Button startGameButton;
    private Button callCardButton;
    private Button loteriaButton;
    private ImageView callerCardImageView;
    private Label callerCardNameLabel;
    private GridPane playerBoardGrid;
    private Label statusLabel;

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
        callCardButton = new Button("Call Next Card");
        callCardButton.setPrefSize(150, 50);
        loteriaButton = new Button("¡Lotería!");
        loteriaButton.setPrefSize(150, 50);
        Button newGameButton = new Button("New Game");
        newGameButton.setPrefWidth(150);
        gamePanel = new VBox(10, boardViewLabel, playerSelectorComboBox, new Separator(), callCardButton, loteriaButton, newGameButton);
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

        // --- Final Scene and Stage Setup ---
        Scene scene = new Scene(root);
        primaryStage.setTitle("¡Lotería!");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
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
            newPlayer.addBoard(new PlayerBoard(BOARD_SIZE, newPlayer));
            game.addPlayer(newPlayer);

            playerListView.getItems().add(name);
            playerSelectorComboBox.getItems().add(newPlayer);
            nameField.clear();
            statusLabel.setText(name + " has joined the game.");
            startGameButton.setDisable(false);
            if (currentPlayer == null) {
                currentPlayer = newPlayer;
                playerSelectorComboBox.setValue(currentPlayer);
            }
        } catch (LoteriaException e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    private void handleStartGame() {
        try {
            game.startGame(); // This sets the state but doesn't generate boards in the model

            // FIX: Explicitly generate the board for each player after starting the game.
            for(Player player : game.getPlayers()) {
                for(PlayerBoard board : player.getBoards()) {
                    board.generateBoard(game.getDeck());
                }
            }

            setupPanel.setVisible(false);
            gamePanel.setVisible(true);
            statusLabel.setText("Game started! Click 'Call Next Card' to begin.");
            drawPlayerBoard();
        } catch (LoteriaException e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    private void handleCallCard() {
        try {
            LoteriaCard card = game.callNextCard();
            callerCardImageView.setImage(loadImage(card.getCardNumber() + ".png"));
            callerCardNameLabel.setText("#" + card.getCardNumber() + ": " + card.getSpanishName());
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

        List<WinningPattern> patterns = StandardPatterns.getAllStandardPatterns(BOARD_SIZE);
        ChoiceDialog<WinningPattern> dialog = new ChoiceDialog<>(patterns.get(0), patterns);
        dialog.setTitle("¡Lotería! Claim");
        dialog.setHeaderText("Which pattern did you win with, " + claimingPlayer.getName() + "?");
        dialog.setContentText("Choose your pattern:");

        Optional<WinningPattern> result = dialog.showAndWait();
        result.ifPresent(pattern -> {
            if (game.validateWin(claimingPlayer, pattern)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("¡Lotería!");
                alert.setHeaderText("WINNER!");
                alert.setContentText(claimingPlayer.getName() + " has won with the pattern: " + pattern.getName());
                alert.showAndWait();
                callCardButton.setDisable(true);
                loteriaButton.setDisable(true);
            } else {
                statusLabel.setText(claimingPlayer.getName() + "'s claim was invalid. The game continues.");
            }
        });
    }

    private void handleNewGame() {
        game = new LoteriaGame(MAX_PLAYERS);
        currentPlayer = null;
        playerListView.getItems().clear();
        playerSelectorComboBox.getItems().clear();
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
            drawPlayerBoard();
            updateBoardHighlights();
        }
    }

    private void drawPlayerBoard() {
        playerBoardGrid.getChildren().clear();
        if (currentPlayer == null) return;
        PlayerBoard board = currentPlayer.getBoards().get(0);

        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                LoteriaCard card = board.getCard(row, col);
                if (card == null) { // Defensive check
                    System.err.println("Error: Card at (" + row + "," + col + ") is null. Board may not be generated.");
                    continue;
                }
                ImageView cardView = new ImageView(loadImage(card.getCardNumber() + ".png"));
                cardView.setFitHeight(100);
                cardView.setFitWidth(70);
                cardView.setPreserveRatio(true);
                StackPane cellPane = new StackPane(cardView);
                cellPane.setUserData(card);
                playerBoardGrid.add(cellPane, col, row);
            }
        }
    }

    private void updateBoardHighlights() {
        if (game.getGameState() != GameState.IN_PROGRESS) return;
        List<LoteriaCard> calledCards = game.getCalledCards();
        playerBoardGrid.getChildren().forEach(node -> {
            LoteriaCard card = (LoteriaCard) node.getUserData();
            if (calledCards.contains(card)) {
                node.setOpacity(0.5);
            } else {
                node.setOpacity(1.0);
            }
        });
    }

    private Image loadImage(String fileName) {
        String fullPath = "data/media/" + fileName;
        try {
            return new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(fullPath)));
        } catch (Exception e) {
            System.err.println("Failed to load image: " + fullPath + ". Make sure it's in the correct resources folder.");
            return null;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
