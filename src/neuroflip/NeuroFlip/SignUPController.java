/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package neuroflip.NeuroFlip;

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

        // Sign Up Submit Button (basic example)
//        sSignupBS.setOnAction(event -> {
//            // You can later add form validation and user creation logic here
//            System.out.println("User signed up with name: " + FullNameInput.getText());
//            // Optional: show message or navigate somewhere
//        });
        // TODO
    }    
    
}
