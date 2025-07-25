/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package neuroflip.NeuroFlip;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ramim
 */
public class GamefileController implements Initializable {

    @FXML
    private Label welcomeLabel;
    @FXML
    private GridPane cardGrid;
    @FXML
    private Label movesLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Button resetButton;
    @FXML
    private ComboBox<String> levelSelector;
    @FXML
    private Button back;

    private List<Card> cards;
    private Card firstFlippedCard;
    private int moves;
    private int secondsElapsed;
    private PauseTransition timer;
    private Session session;
    private Connection dbConnection;
    

    private static class Card {
        Button button;
        String name;
        boolean isFlipped;
        boolean isMatched;

        Card(String name) {
            this.name = name;
            this.button = new Button();
            this.button.setPrefSize(100, 100);
            this.button.getStyleClass().add("card");
            this.isFlipped = false;
            this.isMatched = false;
            updateButtonText();
        }

        void updateButtonText() {
            this.button.setText(isFlipped || isMatched ? name : "");
            if (isMatched) {
                this.button.getStyleClass().setAll("card", "card-matched");
            } else if (isFlipped) {
                this.button.getStyleClass().setAll("card", "card-flipped");
            } else {
                this.button.getStyleClass().setAll("card");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        session = new Session();
        String userName = session.getUsername();
        welcomeLabel.setText("Welcome, " + userName + "!");
        connectToDatabase();

        // Initialize level selector
        levelSelector.setItems(FXCollections.observableArrayList("Hard", "Normal", "Easy"));
        levelSelector.setValue("Easy"); // Default to Easy
        levelSelector.setOnAction(event -> resetGame());

        loadCards();
        setupGame();
        startTimer();

        back.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("DashBoard.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) back.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("DashBoard");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void connectToDatabase() {
        try {
            dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/neuroflip", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadCards() {
        cards = new ArrayList<>();
        try {
            // Load all unique card names from the database
            PreparedStatement stmt = dbConnection.prepareStatement("SELECT cardname FROM cards");
            ResultSet rs = stmt.executeQuery();
            List<String> uniqueCardNames = new ArrayList<>();
            while (rs.next()) {
                uniqueCardNames.add(rs.getString("cardname"));
            }

            // Shuffle unique card names
            Collections.shuffle(uniqueCardNames);

            // Determine number of pairs based on level
            int numPairs;
            String level = levelSelector.getValue();
            switch (level) {
                case "Hard":
                    numPairs = game_level.getHard(); // Use all available cards
                    break;
                case "Normal":
                    numPairs = game_level.getNormal(); // 4 pairs (8 cards)
                    break;
                case "Easy":
                    numPairs = game_level.getEasy(); // 2 pairs (4 cards)
                    break;
                default:
                    numPairs = game_level.getEasy(); // Fallback to Easy
            }

            // Select unique card names and create pairs
            List<String> cardNames = new ArrayList<>();
            for (int i = 0; i < numPairs && i < uniqueCardNames.size(); i++) {
                String name = uniqueCardNames.get(i);
                cardNames.add(name); // Add first card of the pair
                cardNames.add(name); // Add second card of the pair
            }

            // Shuffle the paired cards
            Collections.shuffle(cardNames);

            // Create Card objects
            for (String name : cardNames) {
                Card card = new Card(name);
                cards.add(card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupGame() {
        moves = 0;
        secondsElapsed = 0;
        firstFlippedCard = null;
        cardGrid.getChildren().clear();

        // Determine grid size based on number of cards
        int numCards = cards.size();
        int cols = (int) Math.ceil(Math.sqrt(numCards));
        int rows = (int) Math.ceil((double) numCards / cols);

        int index = 0;
        for (int row = 0; row < rows && index < numCards; row++) {
            for (int col = 0; col < cols && index < numCards; col++) {
                Card card = cards.get(index++);
                cardGrid.add(card.button, col, row);
                card.button.setOnAction(event -> handleCardClick(card));
            }
        }
        updateMovesLabel();
        updateTimeLabel();
    }

    private void handleCardClick(Card card) {
        if (card.isFlipped || card.isMatched || firstFlippedCard == card) {
            return;
        }
        card.isFlipped = true;
        card.updateButtonText();
        moves++;
        updateMovesLabel();

        if (firstFlippedCard == null) {
            firstFlippedCard = card;
        } else {
            if (firstFlippedCard.name.equals(card.name)) {
                firstFlippedCard.isMatched = true;
                card.isMatched = true;
                firstFlippedCard.updateButtonText();
                card.updateButtonText();
                firstFlippedCard = null;
                checkGameOver();
            } else {
                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                Card prevCard = firstFlippedCard;
                pause.setOnFinished(event -> {
                    prevCard.isFlipped = false;
                    card.isFlipped = false;
                    prevCard.updateButtonText();
                    card.updateButtonText();
                    firstFlippedCard = null;
                });
                pause.play();
            }
        }
    }

    private void checkGameOver() {
        boolean allMatched = cards.stream().allMatch(card -> card.isMatched);
        if (allMatched) {
            timer.stop();
            saveResults();
            // Optionally, show a dialog with final score
        }
    }

    private void saveResults() {
        try {
            String userName = session.getUsername();
            PreparedStatement stmt = dbConnection.prepareStatement(
                "INSERT INTO results (username, previous_score, highest_score) " +
                "VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE " +
                "previous_score = ?, highest_score = LEAST(COALESCE(highest_score, ?), ?)");
            stmt.setString(1, userName);
            stmt.setInt(2, moves);
            stmt.setInt(3, moves);
            stmt.setInt(4, moves);
            stmt.setInt(5, moves);
            stmt.setInt(6, moves);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateMovesLabel() {
        movesLabel.setText("Moves: " + moves);
    }

    private void startTimer() {
        if (timer != null) {
            timer.stop();
            System.out.println("Stopped existing timer at: " + System.currentTimeMillis()); // Debug timestamp
        }
        timer = new PauseTransition(Duration.seconds(1));
        timer.setCycleCount(PauseTransition.INDEFINITE);
        timer.setOnFinished(event -> {
            secondsElapsed++;
            System.out.println("Timer tick at: " + System.currentTimeMillis() + ", secondsElapsed = " + secondsElapsed); // Debug log
            javafx.application.Platform.runLater(() -> {
                updateTimeLabel();
                System.out.println("Updated timeLabel in Platform.runLater to: Time: " + String.format("%02d:%02d", secondsElapsed / 60, secondsElapsed % 60)); // Debug log
            });
            timer.playFromStart();
        });
        secondsElapsed = 0;
        System.out.println("Timer starting, initial time set at: " + System.currentTimeMillis()); // Debug timestamp
        javafx.application.Platform.runLater(() -> {
            updateTimeLabel();
            System.out.println("Initial timeLabel update in Platform.runLater to: Time: 00:00"); // Debug log
        });
        timer.play();
        System.out.println("Timer started at: " + System.currentTimeMillis()); // Debug timestamp
    }

    private void updateTimeLabel() {
        int minutes = secondsElapsed / 60;
        int seconds = secondsElapsed % 60;
        String timeText = String.format("Time: %02d:%02d", minutes, seconds);
        if (timeLabel == null) {
            System.out.println("Error: timeLabel is null"); // Debug FXML binding
        } else {
            timeLabel.setText(timeText);
            System.out.println("Set timeLabel to: " + timeText); // Debug log
        }
    }

    @FXML
    private void resetGame() {
        timer.stop();
        loadCards();
        setupGame();
        startTimer();
    }
}