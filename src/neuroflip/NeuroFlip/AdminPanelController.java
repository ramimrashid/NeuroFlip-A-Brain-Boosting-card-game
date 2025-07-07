/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package neuroflip.NeuroFlip;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Ramim
 */

public class AdminPanelController implements Initializable {

    @FXML private TextField CardName;
    @FXML private Button CardAdd;
    @FXML private Button CardEdit;
    @FXML private Button CardDelete;
    @FXML private TableView<ObservableList<String>> CardValueTable;
    @FXML private TableColumn<ObservableList<String>, String> cardIDCol;
    @FXML private TableColumn<ObservableList<String>, String> cardnameCol;

    private ObservableList<ObservableList<String>> cardList = FXCollections.observableArrayList();

    private String selectedCardID = null; // Store selected ID

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/neuroflip", "root", "");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTable();
        loadCards();

        CardValueTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedCardID = newSelection.get(0); // 0 = cardID
                CardName.setText(newSelection.get(1)); // 1 = cardname
            }
        });

        CardAdd.setOnAction(event -> {
            String name = CardName.getText().trim();
            if (name.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Card name is required.");
                return;
            }

            try (Connection conn = getConnection()) {
                // Check if card already exists
                String checkQuery = "SELECT * FROM Cards WHERE cardname = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                checkStmt.setString(1, name);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    showAlert(Alert.AlertType.INFORMATION, "Card already exists.");
                } else {
                    String insertQuery = "INSERT INTO Cards (cardname) VALUES (?)";
                    PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                    insertStmt.setString(1, name);
                    insertStmt.executeUpdate();

                    showAlert(Alert.AlertType.INFORMATION, "Card added successfully.");
                    CardName.clear();
                    loadCards();
                }

                rs.close();
                checkStmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Database error.");
            }
        });

        CardEdit.setOnAction(event -> {
            String newName = CardName.getText().trim();
            if (selectedCardID == null) {
                showAlert(Alert.AlertType.WARNING, "Please select a card to edit.");
                return;
            }

            if (newName.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Card name cannot be empty.");
                return;
            }

            try (Connection conn = getConnection()) {
                String updateQuery = "UPDATE Cards SET cardname = ? WHERE cardID = ?";
                PreparedStatement stmt = conn.prepareStatement(updateQuery);
                stmt.setString(1, newName);
                stmt.setString(2, selectedCardID);
                stmt.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "Card updated.");
                CardName.clear();
                selectedCardID = null;
                loadCards();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Update failed.");
            }
        });

        CardDelete.setOnAction(event -> {
            if (selectedCardID == null) {
                showAlert(Alert.AlertType.WARNING, "Please select a card to delete.");
                return;
            }

            try (Connection conn = getConnection()) {
                String deleteQuery = "DELETE FROM Cards WHERE cardID = ?";
                PreparedStatement stmt = conn.prepareStatement(deleteQuery);
                stmt.setString(1, selectedCardID);
                stmt.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "Card deleted.");
                CardName.clear();
                selectedCardID = null;
                loadCards();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Delete failed.");
            }
        });
    }

    private void setupTable() {
        cardIDCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(0)));
        cardnameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(1)));
        CardValueTable.setItems(cardList);
    }

    private void loadCards() {
        cardList.clear();
        try (Connection conn = getConnection()) {
            String query = "SELECT * FROM Cards";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                row.add(String.valueOf(rs.getInt("cardID")));
                row.add(rs.getString("cardname"));
                cardList.add(row);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}