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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ramim
 */
public class LevelHandelController implements Initializable {

    @FXML
    private Button hardbutton;
    @FXML
    private Button easybutton;
    @FXML
    private Button normalbutton;
    @FXML
    private TextField hardinput;
    @FXML
    private TextField normalinput;
    @FXML
    private TextField easyinput;
    @FXML
    private Button back;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        

// Set initial values in input fields
hardinput.setText(String.valueOf(game_level.getHard()));
normalinput.setText(String.valueOf(game_level.getNormal()));
easyinput.setText(String.valueOf(game_level.getEasy()));

// Normal button updates Normal value
normalbutton.setOnAction(event -> {
    try {
        game_level.setNormal(Integer.parseInt(normalinput.getText().trim()));
        normalinput.setText(String.valueOf(game_level.getNormal()));
    } catch (NumberFormatException e) {
        System.out.println("Invalid input for normal level.");
    }
});

// Easy button updates Easy value
easybutton.setOnAction(event -> {
    try {
        game_level.setEasy(Integer.parseInt(easyinput.getText().trim()));
        easyinput.setText(String.valueOf(game_level.getEasy()));
    } catch (NumberFormatException e) {
        System.out.println("Invalid input for easy level.");
    }
});

// Hard button updates Hard value
hardbutton.setOnAction(event -> {
    try {
        game_level.setHard(Integer.parseInt(hardinput.getText().trim()));
        hardinput.setText(String.valueOf(game_level.getHard()));
    } catch (NumberFormatException e) {
        System.out.println("Invalid input for hard level.");
    }
});

back.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminPanel.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) back.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Admin Panel");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }); 
        
        
        
    }    
    
}
