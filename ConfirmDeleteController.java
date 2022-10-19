package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * A Class to control Confirm Delete Window
 * @author Avraham Gross
 * @version 1.0
 */
public class ConfirmDeleteController {
    /**
     * boolean value if confirmed to delete
     */
    private static boolean confirmed = false;

    @FXML
    private Button cancelDelete;

    @FXML
    private Button confirmDelete;

    @FXML
    private Text customMessage;

    @FXML
    private VBox vBox;

    /**
     * Close Confirm Delete Window and ensure confirmed is set to false
     * @param event click on cancel button
     */
    @FXML
    void cancel(ActionEvent event) {
        confirmed = false;
        closeStage();
    }

    /**
     * Close Confirm Delete Window and ensure confirmed is set to true
     * @param event click on delete button
     */
    @FXML
    void delete(ActionEvent event) {
        confirmed = true;
        closeStage();
    }

    /**
     * Close Confirm Delete Window
     */
    void closeStage() {
        Stage stage = (Stage) vBox.getScene().getWindow();
        stage.close();
    }

    /**
     * Return confirmed
     * @return boolean confirmed
     */
    public static boolean getConfirmed() {
        return confirmed;
    }

    /**
     * Reset value of confirmed to false
     */
    public static void resetConfirmed() {
        confirmed = false;
    }

    /**
     * Initialize Confirm Delete Window
     */
    public void initialize(){
        customMessage.setText(LoginController.getResources().getString("customMessage"));
        cancelDelete.setText(LoginController.getResources().getString("cancelButton"));
        confirmDelete.setText(LoginController.getResources().getString("deleteApptButton"));
    }

}

