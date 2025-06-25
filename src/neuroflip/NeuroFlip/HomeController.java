/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package neuroflip.NeuroFlip;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
/**
 * FXML Controller class
 *
 * @author Ramim
 */
public class HomeController implements Initializable {

    @FXML
    private Text WelcomeSlogan;
    @FXML
    private Button ContinueB;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ContinueB.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
                Parent root = loader.load();

                // Get current stage and set new scene
                Stage stage = (Stage) ContinueB.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Login");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // TODO
    }    
    
}
