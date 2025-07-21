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
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 *
 * @author Ramim
 */
public class LeaderboardController implements Initializable {

    @FXML
    private TableView<LeaderboardEntry> leaderboardTable;
    @FXML
    private TableColumn<LeaderboardEntry, Number> serialColumn;
    @FXML
    private TableColumn<LeaderboardEntry, String> usernameColumn;
    @FXML
    private TableColumn<LeaderboardEntry, Number> highestScoreColumn;
    @FXML
    private Button backButton;

    // Database connection details (update with your actual values)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/neuroflip";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    // Data structure to hold leaderboard entries
    public static class LeaderboardEntry {
        private final SimpleIntegerProperty serial;
        private final SimpleStringProperty username;
        private final SimpleIntegerProperty highestScore;

        public LeaderboardEntry(int serial, String username, int highestScore) {
            this.serial = new SimpleIntegerProperty(serial);
            this.username = new SimpleStringProperty(username);
            this.highestScore = new SimpleIntegerProperty(highestScore);
        }

        // Property accessors
        public SimpleIntegerProperty serialProperty() {
            return serial;
        }

        public SimpleStringProperty usernameProperty() {
            return username;
        }

        public SimpleIntegerProperty highestScoreProperty() {
            return highestScore;
        }

        // Getters for direct value access (optional, not used by TableView)
        public int getSerial() {
            return serial.get();
        }

        public String getUsername() {
            return username.get();
        }

        public int getHighestScore() {
            return highestScore.get();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configure TableView columns
        serialColumn.setCellValueFactory(cellData -> cellData.getValue().serialProperty());
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        highestScoreColumn.setCellValueFactory(cellData -> cellData.getValue().highestScoreProperty());

        // Load leaderboard data
        loadLeaderboardData();

        // Handle back button action
        backButton.setOnAction(event -> handleBack());
    }

    // Fetch and display leaderboard data
    private void loadLeaderboardData() {
        ObservableList<LeaderboardEntry> data = FXCollections.observableArrayList();
        String query = "SELECT username, highest_score FROM results ORDER BY highest_score DESC";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            int serial = 1;
            while (rs.next()) {
                String username = rs.getString("username");
                int highestScore = rs.getInt("highest_score");
                data.add(new LeaderboardEntry(serial++, username, highestScore));
            }
            leaderboardTable.setItems(data);

        } catch (SQLException e) {
            e.printStackTrace();
            // Optionally, show an alert to the user
            leaderboardTable.setItems(FXCollections.observableArrayList(
                new LeaderboardEntry(1, "Error", 0)
            ));
        }
    }

    // Handle navigation back to the dashboard
    @FXML
    private void handleBack() {
        try {
            URL fxmlUrl = getClass().getResource("/neuroflip/NeuroFlip/DashBoard.fxml");
            if (fxmlUrl == null) {
                throw new IOException("Cannot find DashBoard.fxml");
            }
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Optionally, show an alert to the user
        }
    }
}