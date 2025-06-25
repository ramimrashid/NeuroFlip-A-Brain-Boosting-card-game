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
        // TODO
    }    

    @FXML
    private void handleButtonAction(ActionEvent event) {
    }
    
}
