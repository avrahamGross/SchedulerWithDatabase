package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

/**
 * A Class to run a Customer and Appointment Tracking Application
 * @author Avraham Gross
 * @version 1.0
 */
public class Main extends Application {
    /**
     * Open Login Window
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * Run Application
     * @param args
     */
    public static void main(String[] args) {
        JDBC.makeConnection();
        try {
            System.out.println(JDBC.getConnection().getMetaData().getDriverVersion());
        } catch (SQLException e) {

        }
        launch(args);
        JDBC.closeConnection();
    }
}
