/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package neuroflip.NeuroFlip;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.*;
/**
 * FXML Controller class
 *
 * @author Ramim
 */
public class AdminLoginController implements Initializable {

    @FXML
    private Button adminlogin;

    @FXML
    private Text Lwarnings;

    @FXML
    private TextField Username;

    @FXML
    private PasswordField Password;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        adminlogin.setOnAction(event -> {
            String username = Username.getText().trim();
            String password = Password.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Lwarnings.setText("⚠️ Username or Password is empty.");
                return;
            }

            String dbURL = "jdbc:mysql://localhost:3306/neuroflip";
            String dbUser = "root";
            String dbPass = "";

            try {
                Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPass);

                String query = "SELECT * FROM admin WHERE UserName = ? AND Password = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, username);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    Lwarnings.setText("✅ Login successful!");

                    // Load AdminPanel.fxml
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminPanel.fxml"));
                    Parent root = loader.load();

              

                    // Show the dashboard
                    Stage stage = (Stage) adminlogin.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Admin Dashboard");
                    stage.show();

                } else {
                    Lwarnings.setText("❌ Invalid username or password.");
                }

                rs.close();
                stmt.close();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
                Lwarnings.setText("❌ Login failed due to database error.");
            }
        });
    }
}