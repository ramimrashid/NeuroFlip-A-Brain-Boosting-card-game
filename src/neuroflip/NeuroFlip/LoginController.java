/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package neuroflip.NeuroFlip;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.*;
/**
 *
 * @author Ramim
 */
public class LoginController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private Button Loginbutton;
    @FXML
    private TextField UseNameInput;
    @FXML
    private TextField PasswordInput;
    @FXML
    private Text UserNameT;
    @FXML
    private Text PasswordT;
    @FXML
    private Text LoginHedding;
    @FXML
    private Button SignUpbutton;
    @FXML
    private Text Lwarnings;
    
    
   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Handle Login Button
//        Loginbutton.setOnAction(event -> {
//           
//        });

        // Handle Sign Up Button
        SignUpbutton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUP.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) SignUpbutton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Sign Up Page");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });      
         // ✅ Login logic
    Loginbutton.setOnAction(event -> {
        String username = UseNameInput.getText().trim();
        String password = PasswordInput.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Lwarnings.setText("⚠️ Username or Password is empty.");
            return;
        }
        String dbURL = "jdbc:mysql://localhost:3306/neuroflip";
        String dbUser = "root";
        String dbPass = "";
        try {
            java.sql.Connection conn = java.sql.DriverManager.getConnection(dbURL, dbUser, dbPass);

            String query = "SELECT * FROM User WHERE user_name = ? AND password = ?";
            java.sql.PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            java.sql.ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Lwarnings.setText("✅ Login successful!");
          

                 // ✅ Load dashboard and pass username
                 FXMLLoader loader = new FXMLLoader(getClass().getResource("DashBoard.fxml"));
                 Parent root = loader.load();

                // Get DashboardController and call setUsername()
                DashBoardController controller = loader.getController();
                controller.setUsername(username);  // pass the logged-in username

                // Show the dashboard
                Stage stage = (Stage) Loginbutton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Dashboard");
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

    @FXML
    private void handleButtonAction(ActionEvent event) {
    }
    
}
