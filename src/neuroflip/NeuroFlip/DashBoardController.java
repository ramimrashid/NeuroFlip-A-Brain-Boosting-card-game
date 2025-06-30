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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        logoutB.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) logoutB.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Login Page");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        
    }    
    // ✅ This method will be called from LoginController
    public void setUsername(String username) {
        ProfileName.setText(username);
        Wusername.setText(username);
    }
    
}
