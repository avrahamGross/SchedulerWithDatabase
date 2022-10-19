package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * A Class to control the New Appointment Window
 * @author Avraham Gross
 * @version 1.0
 */
public class newAppointmentController {
    /**
     * Resource Bundle for current user language
     */
    private static ResourceBundle resourceBundle = LoginController.getResources();
    @FXML
    private TextField apptIdBox;

    @FXML
    private Label apptIdLabel;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField contactBox;

    @FXML
    private ComboBox<String> contactComb;

    @FXML
    private Label contactLabel;

    @FXML
    private Label custIdLabel;

    @FXML
    private TextField customerIdBox;

    @FXML
    private TextField descriptionBox;

    @FXML
    private Label descriptionLabel;

    @FXML
    private TextField endDateBox;

    @FXML
    private Label endDateLabel;

    @FXML
    private Label endTimeLabel;

    @FXML
    private TextField endTimeBox;

    @FXML
    private TextField locationBox;

    @FXML
    private Label locationLabel;

    @FXML
    private Label pageLabel;

    @FXML
    private TextField startDateBox;

    @FXML
    private Label startDateLabel;

    @FXML
    private Label startTimeLabel;

    @FXML
    private TextField startTimeBox;

    @FXML
    private Button submitButton;

    @FXML
    private TextField titleBox;

    @FXML
    private Label titleLabel;

    @FXML
    private TextField typeBox;

    @FXML
    private Label typeLabel;

    @FXML
    private VBox vBox;

    /**
     * Close New Appointment Window
     * @param event
     */
    @FXML
    void cancel(ActionEvent event) {
        Stage myStage = (Stage) vBox.getScene().getWindow();
        myStage.close();
    }

    /**
     * Select Contact name from contact combo box
     * @param event click on name in combo box
     */
    @FXML
    void selectContact(ActionEvent event) {
        String selectedContact = contactComb.getSelectionModel().getSelectedItem();
        contactBox.setText(selectedContact);
    }

    /**
     * Add appointment to schedule. Confirm valid input data, add to list and database
     * <p>
     * A lambda is used to validate input data. One Lambda is used to confirm appointment timing is within business hours. A second Lambda is used to confirm
     * no other appointment is scheduled for the same time
     * </p>
     * @param event click on submit button
     * @throws SQLException if connect to database invalid
     * @throws NumberFormatException if date or time data not in correct format
     * @throws IllegalArgumentException
     * @throws DateTimeException
     */
    @FXML
    void submit(ActionEvent event) {
        ValidateData timing = time ->
                time.toInstant().atZone(ZoneId.of("America/New_York")).getHour() >= 8 && time.toInstant().atZone(ZoneId.of("America/New_York")).getHour() < 22;
        ValidateData overlap = (time) -> {
            for (Appointment a : AppointmentsController.getAppts()) {
                if (a.getStart().toLocalDate().compareTo(time.toLocalDate()) == 0) {
                    if (a.getStart().toLocalTime().compareTo(time.toLocalTime()) == 0) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, resourceBundle.getString("overlapMessage"));
                        alert.showAndWait();
                        return true;
                    }
                }
            }
            return false;
        };
        ZonedDateTime startTime = ZonedDateTime.of(LocalDateTime.of(LocalDate.parse(startDateBox.getText(), DateTimeFormatter.ISO_LOCAL_DATE),
                LocalTime.parse(startTimeBox.getText(), DateTimeFormatter.ISO_LOCAL_TIME)), LoginController.getLocalTimeZone());
        ZonedDateTime endTime = ZonedDateTime.of(LocalDateTime.of(LocalDate.parse(endDateBox.getText(), DateTimeFormatter.ISO_LOCAL_DATE),
                LocalTime.parse(endTimeBox.getText(), DateTimeFormatter.ISO_LOCAL_TIME)), LoginController.getLocalTimeZone());
        try {
            if (!timing.test(startTime)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, resourceBundle.getString("invalidTime"));
                alert.showAndWait();
            }
            else if (timing.test(endTime) && !overlap.test(startTime)) {
                Statement stmt = JDBC.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                String query = "select c.contact_id, u.user_id from contacts c, users u where contact_name = \"" + contactBox.getText() +
                        "\" and u.user_name = \"" + LoginController.getUserName() + "\"";
                ResultSet rs = stmt.executeQuery(query);
                Appointment newAppoitnment = null;
                while (rs.next()) {
                    newAppoitnment = new Appointment(Appointment.getApptCount(), titleBox.getText(),
                            descriptionBox.getText(), locationBox.getText(), typeBox.getText(), startTime, endTime, ZonedDateTime.of(LocalDateTime.now(),
                            LoginController.getLocalTimeZone()), LoginController.getUserName(), Integer.parseInt(customerIdBox.getText()), rs.getInt(2),
                            rs.getInt(1), contactBox.getText());
                }
                rs = stmt.executeQuery("select * from appointments");
                boolean successfulInsert = newAppoitnment.updateAppointments(rs, "new");
                if (successfulInsert) {
                    AppointmentsController.getAppts().add(newAppoitnment);
                }
                Stage myStage = (Stage) vBox.getScene().getWindow();
                myStage.close();
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException | DateTimeException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, resourceBundle.getString("submitError"));
            alert.showAndWait();
        }
    }

    /**
     * Initialize Add Appointment Window
     * @throws SQLException if error with database connection or query syntax
     */
    @FXML
    public void initialize() {
        ObservableList<String> contactNames = FXCollections.observableArrayList();
        String query = "Select contact_name from contacts";
        try {
            Statement stmt = JDBC.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                contactNames.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        contactComb.setItems(contactNames);
        apptIdBox.setText(Integer.toString(Appointment.getApptCount()));
        pageLabel.setText(resourceBundle.getString("newApptLabel"));
        apptIdLabel.setText(resourceBundle.getString("schedApptIdLabel"));
        custIdLabel.setText(resourceBundle.getString("schedCustomerIdLabel"));
        titleLabel.setText(resourceBundle.getString("schedTitleLabel"));
        descriptionLabel.setText(resourceBundle.getString("schedDescriptionLabel"));
        typeLabel.setText(resourceBundle.getString("schedTypeLabel"));
        locationLabel.setText(resourceBundle.getString("schedLocationLabel"));
        startDateLabel.setText(resourceBundle.getString("schedStartDateLabel"));
        startTimeLabel.setText(resourceBundle.getString("schedStartTimeLabel"));
        endDateLabel.setText(resourceBundle.getString("schedEndDateLabel"));
        endTimeLabel.setText(resourceBundle.getString("schedEndTimeLabel"));
        contactLabel.setText(resourceBundle.getString("schedContactNameLabel"));
        contactBox.setText(resourceBundle.getString("schedContactText"));
        contactComb.setPromptText(resourceBundle.getString("schedContactCombo"));
        submitButton.setText(resourceBundle.getString("submitButton"));
        cancelButton.setText(resourceBundle.getString("cancelButton"));
    }
}