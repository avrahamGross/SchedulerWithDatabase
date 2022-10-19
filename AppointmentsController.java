package sample;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ResourceBundle;
import java.util.function.Predicate;

/**
 * A class to control the Appointment table Window.
 * @author Avraham Gross
 * @version 1.0
 */
public class AppointmentsController {
    /**
     * ObservableList containing appointments
     */
    private static ObservableList<Appointment> appts;
    /**
     * FilteredList to view appointments of current week
     */
    private static FilteredList<Appointment> weekAppointmentFilteredList;
    /**
     * FilteredList to view appointments of current month
     */
    private static FilteredList<Appointment> monthAppointmentFilteredList;
    /**
     * Selected appointment reference
     */
    private static Appointment selectedAppt = null;
    /**
     * Selected appointment index
     */
    private static int selectedApptIndex = -1;
    /**
     * Resource bundle reference based on login controller
     * @see LoginController#getResources()
     */
    private static ResourceBundle resourceBundle = LoginController.getResources();

    @FXML
    private Tab weeklyTab;

    @FXML
    private Tab monthlyTab;

    @FXML
    private TableColumn<Appointment, Integer> monthApptId;

    @FXML
    private TableColumn<Appointment, String> monthContact;

    @FXML
    private TableColumn<Appointment, Integer> monthCustomerId;

    @FXML
    private TableColumn<Appointment, String> monthDescription;

    @FXML
    private TableColumn<Appointment, LocalDateTime> monthEnd;

    @FXML
    private TableColumn<Appointment, String> monthLocation;

    @FXML
    private TableColumn<Appointment, LocalDateTime> monthStart;

    @FXML
    private TableView<Appointment> monthTable;

    @FXML
    private TableColumn<Appointment, String> monthTitle;

    @FXML
    private TableColumn<Appointment, String> monthType;

    @FXML
    private TableColumn<Appointment, Integer> monthUserId;

    @FXML
    private TabPane tabPane;

    @FXML
    private TableColumn<Appointment, Integer> weekApptId;

    @FXML
    private TableColumn<Appointment, String> weekContact;

    @FXML
    private TableColumn<Appointment, Integer> weekCustId;

    @FXML
    private TableColumn<Appointment, String> weekDescription;

    @FXML
    private TableColumn<Appointment, LocalDateTime> weekEnd;

    @FXML
    private TableColumn<Appointment, String> weekLocation;

    @FXML
    private TableColumn<Appointment, LocalDateTime> weekStart;

    @FXML
    private TableView<Appointment> weekTable;

    @FXML
    private TableColumn<Appointment, String> weekTitle;

    @FXML
    private TableColumn<Appointment, String> weekType;

    @FXML
    private TableColumn<Appointment, Integer> weekUserId;

    @FXML
    private Button deleteButton;

    @FXML
    private Button scheduleNewButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button customersButton;

    @FXML
    private Button reportsButton;

    /**
     * Returns appointments list
     * @return ObservableList appts
     */
    public static ObservableList<Appointment> getAppts() {
        return appts;
    }

    /**
     * Returns selected appointment
     * @return Appointment selectedAppointment
     */
    public static Appointment getSelectedAppt() {
        return selectedAppt;
    }

    /**
     * Returns selected appointment index
     * @return int index
     */
    public static int getSelectedApptIndex() {
        return selectedApptIndex;
    }

    /**
     * Reset selected variables to null and -1 index
     */
    public static void resetSelected() {
        selectedAppt = null;
        selectedApptIndex = -1;
    }

    /**
     * Open Confirm Delete Window to confirm delete
     * @return boolean confirmDelete
     * @see ConfirmDeleteController#getConfirmed()
     * @throws IOException if error in loading fxml file
     */
    public boolean confirmDelete() throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ConfirmDelete.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle(resourceBundle.getString("confirmDeletePageLabel"));
        stage.setScene(scene);
        stage.showAndWait();
        return ConfirmDeleteController.getConfirmed();
    }

    /**
     * Delete selected appointment
     * @see ConfirmDeleteController#resetConfirmed()
     * @param event click on delete button
     */
    @FXML
    void deleteAppt(ActionEvent event) {
        try {
            if (selectedAppt != null && confirmDelete()) {
                appts.remove(selectedAppt);
                Statement statement = JDBC.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                String query = "select * from appointments where appointment_id = " + selectedAppt.getApptId();
                ResultSet rs = statement.executeQuery(query);
                while(rs.next()){
                    rs.deleteRow();;
                }
                ConfirmDeleteController.resetConfirmed();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, resourceBundle.getString("appointment") + " #" +
                        selectedAppt.getApptId() + " - " + selectedAppt.getType() + " " + resourceBundle.getString("canceled"));
                alert.showAndWait();
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        finally {
            weekTable.refresh();
            monthTable.refresh();
            resetSelected();
        }
    }

    /**
     * Open new appointment Window
     * @param event click on new appointment button
     */
    @FXML
    void newAppt(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("NewAppointment.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle(resourceBundle.getString("newApptLabel"));
        stage.setScene(scene);
        stage.showAndWait();
        monthTable.refresh();
        weekTable.refresh();
        resetSelected();
    }

    /**
     * Open Update Appointment Window
     * @param event click on update appointment button
     */
    @FXML
    void updateAppt(ActionEvent event) {
        if (selectedAppt != null) {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getClassLoader().getResource("UpdateAppointment.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle(resourceBundle.getString("updateApptLabel"));
            stage.setScene(scene);
            stage.showAndWait();
            monthTable.refresh();
            weekTable.refresh();
        }
    }

    /**
     * Show Customer Table Window in screen over Appointment Window
     * @param event click on customers button
     */
    @FXML
    void showCustomers(ActionEvent event) {
        resetSelected();
        LoginController.getNewStage().hide();
        LoginController.getNewStage().show();
    }

    /**
     * Genereate accurate reports and open reports window
     * @param event click on reports button
     * @see Reports#makeReports(ObservableList, ObservableList)
     */
    @FXML
    void generateReports(ActionEvent event) {
        Reports.makeReports(appts, CustomerTableController.getCustomers());
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("Reports.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle(resourceBundle.getString("reports"));
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Set selected appointment and index to value in weekTable
     * @param event click on value in week table
     */
    @FXML
    void selectWeekAppt(MouseEvent event) {
        selectedAppt = weekTable.getSelectionModel().selectedItemProperty().get();
        selectedApptIndex = weekTable.getSelectionModel().selectedIndexProperty().get();
        event.consume();
    }

    /**
     * Set selected appointment and index to value in monthTable
     * @param event click on value in month table
     */
    @FXML
    void selectMonthAppt(MouseEvent event) {
        selectedAppt = monthTable.getSelectionModel().selectedItemProperty().get();
        selectedApptIndex = monthTable.getSelectionModel().selectedIndexProperty().get();
        event.consume();
    }

    /**
     * Initialize Appointments Table Window. Filter appts list for appointments that meet the Predicates for accurate week and month appointments
     */
    @FXML
    public void initialize() {
        appts = CustomerTableController.getAppts();
        weekApptId.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("apptId"));
        weekTitle.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        weekDescription.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));
        weekLocation.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
        weekContact.setCellValueFactory(new PropertyValueFactory<Appointment, String>("contactName"));
        weekType.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
        weekStart.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("start"));
        weekEnd.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("end"));
        weekCustId.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("customerId"));
        weekUserId.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("userId"));

        monthApptId.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("apptId"));
        monthTitle.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        monthDescription.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));
        monthLocation.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
        monthContact.setCellValueFactory(new PropertyValueFactory<Appointment, String>("contactName"));
        monthType.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
        monthStart.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("start"));
        monthEnd.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("end"));
        monthCustomerId.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("customerId"));
        monthUserId.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("userId"));
        Predicate<Appointment> thisWeek = appointment -> (appointment.getStart().toLocalDate()).compareTo(LocalDate.now().with(WeekFields.of(LoginController.getHere()).dayOfWeek(), 1)) >= 0 &&
                appointment.getStart().toLocalDate().isBefore(LocalDate.now().with(WeekFields.of(LoginController.getHere()).getFirstDayOfWeek()));
        Predicate<Appointment> thisMonth = appointment -> YearMonth.from(appointment.getStart().toLocalDate()).equals(YearMonth.from(LocalDate.now()));
        weekAppointmentFilteredList = new FilteredList<>(appts, thisWeek);
        monthAppointmentFilteredList = new FilteredList<>(appts, thisMonth);
        appts.addListener((ListChangeListener<Appointment>) change -> {
            weekAppointmentFilteredList = new FilteredList<>(appts, thisWeek);
            monthAppointmentFilteredList = new FilteredList<>(appts, thisMonth);
        });
        weekTable.setItems(weekAppointmentFilteredList);
        monthTable.setItems(monthAppointmentFilteredList);


        weeklyTab.setText(resourceBundle.getString("weeklyLabel"));
        monthlyTab.setText(resourceBundle.getString("monthlyLabel"));
        weekContact.setText(resourceBundle.getString("contactLabel"));
        weekApptId.setText(resourceBundle.getString("appointmentsApptIdLabel"));
        monthApptId.setText(resourceBundle.getString("appointmentsApptIdLabel"));
        weekTitle.setText(resourceBundle.getString("titleLabel"));
        monthTitle.setText(resourceBundle.getString("titleLabel"));
        weekDescription.setText(resourceBundle.getString("descriptionLabel"));
        monthDescription.setText(resourceBundle.getString("descriptionLabel"));
        weekType.setText(resourceBundle.getString("typeLabel"));
        monthType.setText(resourceBundle.getString("typeLabel"));
        weekLocation.setText(resourceBundle.getString("locationLabel"));
        monthLocation.setText(resourceBundle.getString("locationLabel"));
        weekStart.setText(resourceBundle.getString("startLabel"));
        monthStart.setText(resourceBundle.getString("startLabel"));
        weekEnd.setText(resourceBundle.getString("endLabel"));
        monthEnd.setText(resourceBundle.getString("endLabel"));
        weekCustId.setText(resourceBundle.getString("appointmentsCustomerIdLabel"));
        monthCustomerId.setText(resourceBundle.getString("appointmentsCustomerIdLabel"));
        weekUserId.setText(resourceBundle.getString("userIdLabel"));
        monthUserId.setText(resourceBundle.getString("userIdLabel"));
        scheduleNewButton.setText(resourceBundle.getString("newApptButton"));
        updateButton.setText(resourceBundle.getString("updateApptButton"));
        deleteButton.setText(resourceBundle.getString("deleteApptButton"));
    }
}
