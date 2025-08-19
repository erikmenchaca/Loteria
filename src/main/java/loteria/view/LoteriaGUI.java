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

public class LoteriaGUI extends Application {

    private LoteriaGame game;
    private Player currentPlayer; // We'll display the board for the first player who joins

    // --- UI Components ---
    // These need to be class fields so they can be accessed by different methods/event handlers
    private VBox setupPanel;
    private VBox gamePanel;
    private ListView<String> playerListView;
    private Button startGameButton;
    private Button callCardButton;
    private ImageView callerCardImageView;
    private Label callerCardNameLabel;
    private GridPane playerBoardGrid;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        game = new LoteriaGame(4); // Initialize the model

        // --- Main Layout ---
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setPrefSize(800, 600);

        // --- Left Panel (Caller Display) ---
        callerCardImageView = new ImageView();
        callerCardImageView.setFitHeight(180);
        callerCardImageView.setFitWidth(120);
        callerCardImageView.setPreserveRatio(true);

        callerCardNameLabel = new Label("Waiting to start...");
        Label callerTitle = new Label("Card Called");
        callerTitle.setFont(Font.font("System", FontWeight.BOLD, 18));

        VBox leftPanel = new VBox(10, callerTitle, callerCardImageView, callerCardNameLabel);
        leftPanel.setAlignment(Pos.TOP_CENTER);
        leftPanel.setPadding(new Insets(0, 0, 0, 10));
        root.setLeft(leftPanel);

        // --- Center Panel (Player Board) ---
        playerBoardGrid = new GridPane();
        playerBoardGrid.setAlignment(Pos.CENTER);
        playerBoardGrid.setHgap(10);
        playerBoardGrid.setVgap(10);
        root.setCenter(playerBoardGrid);

        // --- Right Panel (Controls) ---
        // Setup Panel (visible first)
        Label playersTitle = new Label("Players");
        playersTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        TextField playerNameField = new TextField();
        playerNameField.setPromptText("Enter your name");
        Button addPlayerButton = new Button("Add Player");
        addPlayerButton.setPrefWidth(150);
        playerListView = new ListView<>();
        startGameButton = new Button("Start Game");
        startGameButton.setPrefWidth(150);
        startGameButton.setDisable(true); // Can't start with no players
        setupPanel = new VBox(10, playersTitle, playerNameField, addPlayerButton, playerListView, startGameButton);
        setupPanel.setAlignment(Pos.TOP_CENTER);

        // Game Panel (visible after start)
        callCardButton = new Button("Call Next Card");
        callCardButton.setPrefSize(150, 50);
        Button loteriaButton = new Button("¡Lotería!");
        loteriaButton.setPrefSize(150, 50);
        Button newGameButton = new Button("New Game");
        newGameButton.setPrefWidth(150);
        gamePanel = new VBox(20, callCardButton, loteriaButton, newGameButton);
        gamePanel.setAlignment(Pos.TOP_CENTER);
        gamePanel.setVisible(false); // Hide it initially

        VBox rightPanel = new VBox(10, setupPanel, gamePanel);
        rightPanel.setPadding(new Insets(0, 10, 0, 0));
        root.setRight(rightPanel);

        // --- Bottom Panel (Status Bar) ---
        statusLabel = new Label("Welcome! Add players to begin.");
        root.setBottom(statusLabel);
        BorderPane.setMargin(statusLabel, new Insets(5, 0, 0, 5));

        // --- Event Handlers (Logic) ---
        addPlayerButton.setOnAction(_ -> handleAddPlayer(playerNameField));
        startGameButton.setOnAction(_ -> handleStartGame());
        callCardButton.setOnAction(_ -> handleCallCard());
        loteriaButton.setOnAction(_ -> handleLoteriaButton());
        newGameButton.setOnAction(_ -> handleNewGame());

        // --- Scene and Stage Setup ---
        Scene scene = new Scene(root);
        primaryStage.setTitle("¡Lotería! (Programmatic GUI)");
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
            newPlayer.addBoard(new PlayerBoard(4, newPlayer));
            game.addPlayer(newPlayer);

            playerListView.getItems().add(name);
            nameField.clear();
            statusLabel.setText(name + " has joined.");
            startGameButton.setDisable(false);
            if (currentPlayer == null) currentPlayer = newPlayer;
        } catch (LoteriaException e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    private void handleStartGame() {
        try {
            game.startGame();
            setupPanel.setVisible(false);
            gamePanel.setVisible(true);
            statusLabel.setText("Game started! Click 'Call Next Card'.");
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
            statusLabel.setText("Error: " + e.getMessage());
            callCardButton.setDisable(true);
        }
    }
    
    private void handleLoteriaButton() {
        WinningPattern pattern = StandardPatterns.createFourCorners(4);
        if (game.validateWin(currentPlayer, pattern)) {
            new Alert(Alert.AlertType.INFORMATION, currentPlayer.getName() + " has won the game!").showAndWait();
            gamePanel.setDisable(true);
        } else {
            statusLabel.setText(currentPlayer.getName() + "'s win claim was invalid.");
        }
    }

    private void handleNewGame() {
        game = new LoteriaGame(4);
        currentPlayer = null;
        playerListView.getItems().clear();
        playerBoardGrid.getChildren().clear();
        callerCardImageView.setImage(null);
        callerCardNameLabel.setText("Waiting to start...");
        statusLabel.setText("Welcome! Add players to begin.");
        gamePanel.setVisible(false);
        setupPanel.setVisible(true);
        gamePanel.setDisable(false);
        startGameButton.setDisable(true);
    }

    private void drawPlayerBoard() {
        playerBoardGrid.getChildren().clear();
        if (currentPlayer == null) return;
        PlayerBoard board = currentPlayer.getBoards().get(0);

        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                LoteriaCard card = board.getCard(row, col);
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
        List<LoteriaCard> calledCards = game.getCalledCards();
        playerBoardGrid.getChildren().forEach(node -> {
            LoteriaCard card = (LoteriaCard) node.getUserData();
            if (calledCards.contains(card)) {
                node.setOpacity(0.5);
            }
        });
    }

    private Image loadImage(String fileName) {
        try {
            return new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/" + fileName)));
        } catch (Exception e) {
            System.err.println("Failed to load image: " + fileName);
            return null;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}