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
import java.util.ResourceBundle;

/**
 * A Class to control the Update Appointment Window
 * @author Avraham Gross
 * @version 1.0
 */
public class updateAppointmentController {
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
    private TextField customerIdBox;

    @FXML
    private Label customerIdLabel;

    @FXML
    private TextField descriptionBox;

    @FXML
    private Label descriptionLabel;

    @FXML
    private TextField endDateBox;

    @FXML
    private Label endDateLabel;

    @FXML
    private TextField endTimeBox;

    @FXML
    private Label endTimeLabel;

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
    private TextField startTimeBox;

    @FXML
    private Label startTimeLabel;

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
     * Close Update Customer Window, disregard changes
     * @param event click on cancel button
     */
    @FXML
    void cancel(ActionEvent event) {
        AppointmentsController.resetSelected();
        Stage myStage = (Stage) vBox.getScene().getWindow();
        myStage.close();
    }

    /**
     * Select contact from combo box
     * @param event click on contact
     */
    @FXML
    void selectContact(ActionEvent event) {
        String selectedContact = contactComb.getSelectionModel().getSelectedItem();
        contactBox.setText(selectedContact);
    }

    /**
     * Confirm valid input data, save changes to list and add to database
     * <p>
     * A lambda is used to validate input data. One Lambda is used to confirm appointment timing is within business hours. A second Lambda is used to confirm
     * no other appointment is scheduled for the same time
     * </p>
     * @param event click on submit button
     * @throws SQLException if error occurs with database connection or invalid data is given, for example a violation of a key constraint
     * @throws IllegalArgumentException if invalid data put in date and time fields
     * @throws DateTimeException if lambda attemps to parse invalid date or time format
     */
    @FXML
    void submit(ActionEvent event) {
        ValidateData timing = time ->
                time.toInstant().atZone(ZoneId.of("America/New_York")).getHour() >= 8 && time.toInstant().atZone(ZoneId.of("America/New_York")).getHour() < 22;
        ValidateData overlap = (time) -> {
            for (Appointment a : AppointmentsController.getAppts()) {
                if (a != AppointmentsController.getSelectedAppt()) {
                    if (a.getStart().toLocalDate().compareTo(time.toLocalDate()) == 0) {
                        if (a.getStart().toLocalTime().compareTo(time.toLocalTime()) == 0) {
                            Alert alert = new Alert(Alert.AlertType.ERROR, resourceBundle.getString("overlapMessage"));
                            alert.showAndWait();
                            return true;
                        }
                    }
                }
            }
            return false;
        };
        try {
            ZonedDateTime startTime = ZonedDateTime.of(LocalDateTime.of(LocalDate.parse(startDateBox.getText(), DateTimeFormatter.ISO_LOCAL_DATE),
                    LocalTime.parse(startTimeBox.getText(), DateTimeFormatter.ISO_LOCAL_TIME)), LoginController.getLocalTimeZone());
            ZonedDateTime endTime = ZonedDateTime.of(LocalDateTime.of(LocalDate.parse(endDateBox.getText(), DateTimeFormatter.ISO_LOCAL_DATE),
                    LocalTime.parse(endTimeBox.getText(), DateTimeFormatter.ISO_LOCAL_TIME)), LoginController.getLocalTimeZone());
            if (!timing.test(startTime)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, resourceBundle.getString("invalidTime"));
                alert.showAndWait();
            }
            else  if (timing.test(endTime) && !overlap.test(startTime)) {
                Statement stmt = JDBC.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                String query = "select contact_id from contacts where contact_name = \"" + contactBox.getText() + "\"";
                ResultSet resultSet = stmt.executeQuery(query);
                int apptId = AppointmentsController.getSelectedAppt().getApptId();
                Appointment updatedAppointment = null;
                while (resultSet.next()) {
                    updatedAppointment = AppointmentsController.getSelectedAppt().updateAppt(titleBox.getText(),
                            descriptionBox.getText(), locationBox.getText(), typeBox.getText(), startTime, endTime, ZonedDateTime.of(LocalDateTime.now(), LoginController.getLocalTimeZone()),
                            LoginController.getUserName(), Integer.parseInt(customerIdBox.getText()), resultSet.getInt(1), contactBox.getText());
                }
                resultSet = stmt.executeQuery("select * from appointments where appointment_id = " + updatedAppointment.getApptId());
                while (resultSet.next()) {
                    System.out.println(resultSet.getInt(1));
                }
                boolean successfulUpdate = updatedAppointment.updateAppointments(resultSet, "update");
                if (successfulUpdate) {
                    AppointmentsController.getAppts().remove(AppointmentsController.getSelectedAppt());
                    AppointmentsController.getAppts().add(updatedAppointment);
                }
                AppointmentsController.resetSelected();
                Stage myStage = (Stage) vBox.getScene().getWindow();
                myStage.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException | DateTimeException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "INVALID INPUTS.\nPLEASE TRY AGAIN.");
            alert.showAndWait();
        }
    }

    /**
     * Initialize Update Appointment Window
     * @throws SQLException if connect to database fails
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
        Appointment selected = AppointmentsController.getSelectedAppt();
        apptIdBox.setText(Integer.toString(selected.getApptId()));
        customerIdBox.setText(Integer.toString(selected.getCustomerId()));
        titleBox.setText(selected.getTitle());
        descriptionBox.setText(selected.getDescription());
        typeBox.setText(selected.getType());
        locationBox.setText(selected.getLocation());
        contactBox.setText(selected.getContactName());
        startDateBox.setText(selected.getStart().toLocalDate().toString());
        startTimeBox.setText(selected.getStart().toLocalTime().toString());
        endDateBox.setText(selected.getEnd().toLocalDate().toString());
        endTimeBox.setText(selected.getEnd().toLocalTime().toString());

        pageLabel.setText(resourceBundle.getString("updateApptLabel"));
        apptIdLabel.setText(resourceBundle.getString("schedApptIdLabel"));
        customerIdLabel.setText(resourceBundle.getString("schedCustomerIdLabel"));
        titleLabel.setText(resourceBundle.getString("schedTitleLabel"));
        descriptionLabel.setText(resourceBundle.getString("schedDescriptionLabel"));
        typeLabel.setText(resourceBundle.getString("schedTypeLabel"));
        locationLabel.setText(resourceBundle.getString("schedLocationLabel"));
        startDateLabel.setText(resourceBundle.getString("schedStartDateLabel"));
        startTimeLabel.setText(resourceBundle.getString("schedStartTimeLabel"));
        endDateLabel.setText(resourceBundle.getString("schedEndDateLabel"));
        endTimeLabel.setText(resourceBundle.getString("schedEndTimeLabel"));
        contactLabel.setText(resourceBundle.getString("schedContactNameLabel"));
        contactComb.setPromptText(resourceBundle.getString("schedContactCombo"));
        submitButton.setText(resourceBundle.getString("submitButton"));
        cancelButton.setText(resourceBundle.getString("cancelButton"));
    }
}
