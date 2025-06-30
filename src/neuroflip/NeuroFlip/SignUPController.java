/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package neuroflip.NeuroFlip;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.*;

/**
 * FXML Controller class
 *
 * @author Ramim
 */
public class SignUPController implements Initializable {

    @FXML
    private TextField FullNameInput;
    @FXML
    private TextField gmailInput;
    @FXML
    private TextField cppasswordinput;
    @FXML
    private Text usernameT;
    @FXML
    private Text fullnameT;
    @FXML
    private Text gmailT;
    @FXML
    private Text passwordT;
    @FXML
    private Text cpasswordT;
    @FXML
    private Button sLoginB;
    @FXML
    private Button sSignupBS;
    @FXML
    private Text SignUpHedding;
    @FXML
    private TextField usernameInput;
    @FXML
    private TextField passwordInput;
    @FXML
    private Text Swarnig;
    @FXML
    private Text Ssuccess;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sLoginB.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) sLoginB.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Login Page");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        
        sSignupBS.setOnAction(event -> {
    String fullName = FullNameInput.getText().trim();
    String gmail = gmailInput.getText().trim();
    String userName = usernameInput.getText().trim();
    String password = passwordInput.getText();
    String cPassword = cppasswordinput.getText();

    // 1. Check for empty fields
    if (fullName.isEmpty() || gmail.isEmpty() || userName.isEmpty() || password.isEmpty() || cPassword.isEmpty()) {
        Swarnig.setText("⚠️ Please fill in all fields.");
        Ssuccess.setText("");
        return;
    }

    // 2. Check if passwords match
    if (!password.equals(cPassword)) {
        Swarnig.setText("⚠️ Passwords do not match.");
        Ssuccess.setText("");
        return;
    }

    // 3. DB Connection
    String DBurl = "jdbc:mysql://localhost:3306/neuroflip";
    String dbUser = "root";
    String dbPass = "";

    try {
        Connection conn = DriverManager.getConnection(DBurl, dbUser, dbPass);
        

        // 4. Check if username exists
        PreparedStatement checkUserStmt = conn.prepareStatement("SELECT * FROM User WHERE user_name = ?");
        checkUserStmt.setString(1, userName);
        ResultSet userRs = checkUserStmt.executeQuery();
        if (userRs.next()) {
            Swarnig.setText("⚠️ Username '" + userName + "' already exists.");
            Ssuccess.setText("");
            userRs.close();
            checkUserStmt.close();
            conn.close();
            return;
        }

        // 5. Check if gmail exists
        PreparedStatement checkGmailStmt = conn.prepareStatement("SELECT * FROM User WHERE gmail = ?");
        checkGmailStmt.setString(1, gmail);
        ResultSet gmailRs = checkGmailStmt.executeQuery();
        if (gmailRs.next()) {
            Swarnig.setText("⚠️ Gmail '" + gmail + "' is already registered.");
            Ssuccess.setText("");
            gmailRs.close();
            checkGmailStmt.close();
            conn.close();
            return;
        }

        // 6. Insert data
        String insertSQL = "INSERT INTO User (gmail, full_name, user_name, password) VALUES (?, ?, ?, ?)";
        PreparedStatement insertStmt = conn.prepareStatement(insertSQL);
        insertStmt.setString(1, gmail);
        insertStmt.setString(2, fullName);
        insertStmt.setString(3, userName);
        insertStmt.setString(4, password);  // ⚠️ Store as hash in real apps
        int rowsInserted = insertStmt.executeUpdate();

        if (rowsInserted > 0) {
            Ssuccess.setText("✅ User registered successfully!");
            Swarnig.setText("");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) sLoginB.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Login Page");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Swarnig.setText("❌ Failed to register user.");
            Ssuccess.setText("");
        }

        // 7. Close resources
        insertStmt.close();
        conn.close();

    } catch (Exception e) {
        Swarnig.setText("❌ Database error occurred.");
        Ssuccess.setText("");
        e.printStackTrace();
    }
});

        

        

 
    }    
    
}
