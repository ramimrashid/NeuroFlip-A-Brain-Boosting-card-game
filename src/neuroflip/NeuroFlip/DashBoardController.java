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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ramim
 */
public class DashBoardController implements Initializable {
    
    

    @FXML
    private Text ProfileName;
    @FXML
    private Button logoutB;
    @FXML
    private Text w;
    @FXML
    private Text Wusername;
    @FXML
    private Button play;
    @FXML
    private Text previousScour;
    @FXML
    private Text Highscoure;

    // Database connection details (update these with your actual values)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/neuroflip";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    @FXML
    private Button LeaderBoard;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Load scores from database when the controller initializes
        loadScores();

        logoutB.setOnAction(event -> {
            try {
                URL fxmlUrl = getClass().getResource("/neuroflip/NeuroFlip/Login.fxml");
                if (fxmlUrl == null) {
                    throw new IOException("Cannot find Login.fxml");
                }
                FXMLLoader loader = new FXMLLoader(fxmlUrl);
                Parent root = loader.load();
                Stage stage = (Stage) logoutB.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Login Page");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                // Optionally, show an alert to the user
            }
        });
        
        LeaderBoard.setOnAction(event -> {
            try {
                URL fxmlUrl = getClass().getResource("/neuroflip/NeuroFlip/Leaderboard.fxml");
                if (fxmlUrl == null) {
                    throw new IOException("Cannot find Leaderboard");
                }
                FXMLLoader loader = new FXMLLoader(fxmlUrl);
                Parent root = loader.load();
                Stage stage = (Stage) LeaderBoard.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Leader Board");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                // Optionally, show an alert to the user
            }
        });

        play.setOnAction(event -> {
            try {
                // Set username in Session
                String username = ProfileName.getText();
                Session.setUsername(username);

                // Load Gamefile.fxml
                URL fxmlUrl = getClass().getResource("/neuroflip/NeuroFlip/Gamefile.fxml");
                if (fxmlUrl == null) {
                    throw new IOException("Cannot find Gamefile.fxml");
                }
                System.out.println("Loading Gamefile.fxml from: " + fxmlUrl);
                FXMLLoader loader = new FXMLLoader(fxmlUrl);
                Parent root = loader.load();

                // Create scene and add stylesheet
                Scene scene = new Scene(root);
                URL cssUrl = getClass().getResource("styles.css");
                if (cssUrl == null) {
                    throw new IOException("Cannot find styles.css");
                }
                System.out.println("Loading styles.css from: " + cssUrl);
                scene.getStylesheets().add(cssUrl.toExternalForm());

                // Set up the stage
                Stage stage = (Stage) play.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("NeuroFlip Game");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                // Optionally, show an alert to the user
            }
        });
    }

    // Called from LoginController to set username
    public void setUsername(String username) {
        ProfileName.setText(username);
        Wusername.setText(username);
        Session.setUsername(username); // Ensure Session is updated
        loadScores(); // Reload scores when username is set
    }

    // Method to load previous and highest scores from the database
    private void loadScores() {
        String username = ProfileName.getText() != null ? ProfileName.getText() : Session.getUsername();
        if (username == null || username.isEmpty()) {
            previousScour.setText("N/A");
            Highscoure.setText("N/A");
            return;
        }

        String query = "SELECT previous_score, highest_score FROM results WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int previousScore = rs.getInt("previous_score");
                int highestScore = rs.getInt("highest_score");
                previousScour.setText(String.valueOf(previousScore));
                Highscoure.setText(String.valueOf(highestScore));
            } else {
                // If no record is found for the user
                previousScour.setText("0");
                Highscoure.setText("0");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            previousScour.setText("Error");
            Highscoure.setText("Error");
            // Optionally, show an alert to the user about the database error
        }
    }
}