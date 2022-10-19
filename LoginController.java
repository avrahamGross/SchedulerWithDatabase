package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * A Class to control the Login Window
 * @author Avraham Gross
 * @version 1.0
 */
public class LoginController {
    @FXML
    private VBox vBox;
    @FXML
    private TextField userNameBox;
    @FXML
    private TextField userPswdBox;
    @FXML
    private Text currentLocation;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Button button;
    /**
     * Locale of current user
     */
    private static Locale here;
    /**
     * HashMap of usernames and passwords
     */
    private static HashMap<String, String> loginCredentials = new HashMap<>();
    private static Stage newStage;
    /**
     * String of current user userName
     */
    private static String userName;
    /**
     * ZoneId for current user
     */
    private static ZoneId localTimeZone;
    /**
     * ResourceBundle for current user language
     */
    private static ResourceBundle rb;

    /**
     * Return locale for current user
     * @return Locale here
     */
    public static Locale getHere() {
        return here;
    }

    /**
     * Return current user local time zone
     * @return ZoneId localTimeZone
     */
    public static ZoneId getLocalTimeZone() {
        return localTimeZone;
    }

    /**
     * Return new stage
     * @return Stage newStage
     */
    public static Stage getNewStage() {
        return newStage;
    }

    /**
     * Returnp resource bundle for current user language
     * @return ResourceBundle rb
     */
    public static ResourceBundle getResources() {
        return rb;
    }

    /**
     * Populate loginCredentials map with all valid user names and passwords
     */
    private static void populateMap() {
        try {
            Statement stmt = JDBC.getConnection().createStatement();
            String query = "Select user_name, password from users";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                loginCredentials.put(rs.getString(1), rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return user name
     * @return String userName
     */
    public static String getUserName() {
        return userName;
    }

    /**
     * Attempt to log in using input user credentials. Open Customer Table Window if successful
     * Log all login attempts in file
     * @see LoginController#trackLoginAttempts(String)
     * @param actionEvent click on submit button
     */
    @FXML
    public void logIn(ActionEvent actionEvent) {
        populateMap();
        if (loginCredentials.containsKey(userNameBox.getText()) && loginCredentials.get(userNameBox.getText()).equals(userPswdBox.getText())) {
            userName = userNameBox.getText();
            trackLoginAttempts("Login Successful");
            Scene customers = null;
            try {
                Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("customerTable.fxml"));
                customers = new Scene(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
            newStage = new Stage();
            newStage.setTitle(rb.getString("customerPageLabel"));
            newStage.setScene(customers);
            Stage myStage = (Stage) vBox.getScene().getWindow();
            myStage.close();
            newStage.show();
        } else {
            trackLoginAttempts("Login Failed");
            Alert alert = new Alert(Alert.AlertType.ERROR, rb.getString("loginError"));
            alert.showAndWait();
        }
    }

    /**
     * Set current user location based on system default
     */
    public void setLocation() {
        localTimeZone = ZoneId.systemDefault();
        currentLocation.setText(localTimeZone.toString());
        here = new Locale(Locale.getDefault().getLanguage());
        rb = ResourceBundle.getBundle("Login", here);
        usernameLabel.setText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        button.setText(rb.getString("submit"));
    }

    /**
     * Log login attempts to txt file, append to file for each attempt
     * @param status
     */
    public void trackLoginAttempts(String status) {
        try {
            File file = new File("C:\\Users\\LabUser\\IdeaProjects\\HelloWorldJFX\\login_activity.txt");
            FileWriter fileWriter = new FileWriter(file, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println("Username: " + userNameBox.getText());
            printWriter.println("Date and Time (" + Locale.getDefault().getDisplayCountry() + "): " + ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a")) );
            printWriter.println(status + "\n");
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize Login Window
     * @see LoginController#setLocation()
     */
    public void initialize() {
        setLocation();
    }
}
