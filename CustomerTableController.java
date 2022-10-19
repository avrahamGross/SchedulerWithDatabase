package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * A Class to control the Customer Table Window
 * @author Avraham Gross
 * @version 1.0
 */
public class CustomerTableController {
    private ResultSet currentCustomers;
    private static Customer selectedCustomer;
    private static int selectedCustomerIndex;
    private static Stage apptsStage = null;
    private static ArrayList<Integer> validCustomerIds = new ArrayList();
    private static ObservableList<Appointment> appts;

    @FXML
    private Label customersPageLabel;
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, Integer> idCol;
    @FXML
    private TableColumn<Customer, String> nameCol;
    @FXML
    private TableColumn<Customer, String> addressCol;
    @FXML
    private TableColumn<Customer, String> stateCol;
    @FXML
    private TableColumn<Customer, String> zipCol;
    @FXML
    private TableColumn<Customer, String> phoneCol;
    @FXML
    private TableColumn<Customer, LocalDateTime> createDateCol;
    @FXML
    private TableColumn<Customer, String> creatorCol;
    @FXML
    private TableColumn<Customer, LocalDateTime> updateDateCol;
    @FXML
    private TableColumn<Customer, String> updaterCol;
    @FXML
    private TableColumn<Customer, Integer> divisionIdCol;
    @FXML
    private Button newCustomerButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button appointmentsButton;
    @FXML
    private Button reportsButton;

    private static ObservableList<Customer> customers = FXCollections.observableArrayList();

    private static ResourceBundle resourceBundle = LoginController.getResources();

    /**
     * Returns list of appointments
     * @return ObservableList of appointments
     */
    public static ObservableList<Appointment> getAppts() {
        return appts;
    }

    /**
     * Reset selected customer to null, index to -1
     */
    public static void resetSelected() {
        selectedCustomer = null;
        selectedCustomerIndex = -1;
    }

    /**
     * Returns index of selected customer
     * @return int index of selected customer
     */
    public static int getSelectedCustomerIndex() {
        return selectedCustomerIndex;
    }

    /**
     * Returns selected customer
     * @return Customer selected customer
     */
    public static Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    /**
     * Returns list of customers
     * @return ObservableList of customers
     */
    public static ObservableList<Customer> getCustomers() {
        return customers;
    }

    /**
     * Opens New Customer Window
     * @param actionEvent
     */
    @FXML
    public void addCustomer(ActionEvent actionEvent) {
        resetSelected();
        Parent root = null;
        FXMLLoader loader = null;
        try {
            root = FXMLLoader.load((getClass().getClassLoader().getResource("NewCustomer.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        Stage newStage = new Stage();
        newStage.setTitle(resourceBundle.getString("newCustomerPageLabel"));
        newStage.setScene(scene);
        newStage.show();
    }

    /**
     * Open Update Customer Window
     * @param actionEvent
     */
    @FXML
    public void updateCustomer(ActionEvent actionEvent) {
        if (selectedCustomer != null) {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getClassLoader().getResource("UpdateCustomer.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle(resourceBundle.getString("updateCustomerPageLabel"));
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Delete selected customer
     * <p>
     * A lambda is used to remove appointments associated with a customer. Using a lambda allows for the removal to be written in 1 line and with the same list
     * as opposed to iterating through the appointments to add each associated appointment to a new list and then iterate through the new list to delete from the
     * appointments list.
     * </p>
     * @param actionEvent
     */
    @FXML
    public void deleteCustomer(ActionEvent actionEvent) {
        if (selectedCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, resourceBundle.getString("customerTableError"));
            alert.showAndWait();

        } else {
            try (Statement stmt = JDBC.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE)) {
                if (confirmDelete()) {
                    if (AppointmentsController.getAppts() != null) {
                        AppointmentsController.getAppts().removeIf(appointment -> appointment.getCustomerId() == selectedCustomer.getId());
                    }
                    String query = "select * from appointments where customer_id = " + selectedCustomer.getId();
                    ResultSet resultSet = stmt.executeQuery(query);
                    while (resultSet.next()) {
                        resultSet.deleteRow();
                    }
                    customers.remove(selectedCustomerIndex);
                    query = "select * from customers where customer_id = " + selectedCustomer.getId();
                    resultSet = stmt.executeQuery(query);
                    while (resultSet.next()) {
                        resultSet.deleteRow();
                    }
                    ConfirmDeleteController.resetConfirmed();
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            } finally {
                resetSelected();
                customerTable.refresh();
            }
        }
    }

    /**
     * Open Appointment Window
     * @param event
     */
    @FXML
    void openAppointments(ActionEvent event) {
        resetSelected();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("Appointments.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (apptsStage == null) {
            Scene scene = new Scene(root);
            apptsStage = new Stage();
            apptsStage.setTitle(resourceBundle.getString("apptsPageLabel"));
            apptsStage.setScene(scene);
            apptsStage.show();
        } else {
            apptsStage.hide();
            apptsStage.show();
        }
    }

    /**
     * Collects selected customer and index of selected customer
     * @param mouseEvent
     */
    @FXML
    public void collectCustomer(MouseEvent mouseEvent) {
        selectedCustomer = customerTable.getSelectionModel().selectedItemProperty().get();
        selectedCustomerIndex = customerTable.getSelectionModel().selectedIndexProperty().get();
        mouseEvent.consume();
    }

    /**
     * Generates accurate reports and opens window to show reports
     */
    @FXML
    public void generateReports() {
        Reports.makeReports(appts, customers);
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
     * Open window to confirm delete
     * @return boolean confirm delete
     * @throws IOException
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
     * Fill customer ObservableList to view in TableView
     */
    public void fillList() {
        try {
            ArrayList<String> stateList = new ArrayList<>();
            String query = "Select fl.division_id, fl.division from customers c, first_level_divisions fl where fl.Division_ID = c.Division_ID";
            Statement stmt = JDBC.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                stateList.add(Integer.toString(rs.getInt(1)));
                stateList.add(rs.getString(2));
            }
            query = "SELECT * FROM Customers";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                customers.add(new Customer(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5), rs.getTimestamp(6),
                        rs.getString(7), rs.getTimestamp(8), rs.getString(9),
                        rs.getInt(10), stateList.get(stateList.indexOf(Integer.toString(rs.getInt(10))) + 1)));
                Customer.setCustomerCount(rs.getInt(1));
                validCustomerIds.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fill appointments list of all current appointments. Used to generate alert for upcoming appointment
     */
    private void fillAppointmentList() {
        appts = FXCollections.observableArrayList();
        String query = "Select a.*, c.contact_name from appointments a inner join contacts c on c.contact_id = a.contact_id;";
        try (Statement stmt = JDBC.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                appts.add(new Appointment(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5), rs.getTimestamp(6),
                        rs.getTimestamp(7), rs.getTimestamp(8), rs.getString(9), rs.getTimestamp(10),
                        rs.getString(11), rs.getInt(12), rs.getInt(13), rs.getInt(14), rs.getString(15)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns user id for current user
     * @return int userId
     */
    public int getUserId() {
        int userId = -1;
     try (Statement statement = JDBC.getConnection().createStatement()) {
         ResultSet resultSet = statement.executeQuery("select user_id from users where user_name = \"" + LoginController.getUserName() + "\"");
         while (resultSet.next()) {
             userId = resultSet.getInt(1);
         }
     } catch (SQLException e) {
         e.printStackTrace();
     }
     return userId;
    }

    /**
     * Initialize Customer Table window, fill lists with customer data, show alert for upcoming appointment
     */
    public void initialize() {
        fillAppointmentList();
        idCol.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("id"));
        idCol.setText(resourceBundle.getString("idCol"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
        nameCol.setText(resourceBundle.getString("nameCol"));
        addressCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        addressCol.setText(resourceBundle.getString("addressCol"));
        stateCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("state"));
        stateCol.setText(resourceBundle.getString("stateCol"));
        zipCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("postalCode"));
        zipCol.setText(resourceBundle.getString("zipCol"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("phone"));
        phoneCol.setText(resourceBundle.getString("phoneCol"));
        createDateCol.setCellValueFactory(new PropertyValueFactory<Customer, LocalDateTime>("createDateTime"));
        createDateCol.setText(resourceBundle.getString("createDateCol"));
        creatorCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("creator"));
        creatorCol.setText(resourceBundle.getString("creatorCol"));
        updateDateCol.setCellValueFactory(new PropertyValueFactory<Customer, LocalDateTime>("updateDateTime"));
        updateDateCol.setText(resourceBundle.getString("updateDateCol"));
        updaterCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("updater"));
        updaterCol.setText(resourceBundle.getString("updaterCol"));
        divisionIdCol.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("divisionId"));
        divisionIdCol.setText(resourceBundle.getString("divisionIdCol"));
        fillList();
        customerTable.setItems(customers);
        customersPageLabel.setText(resourceBundle.getString("customerPageLabel"));
        appointmentsButton.setText(resourceBundle.getString("appointmentsButton"));
        reportsButton.setText(resourceBundle.getString("reportsButton"));
        newCustomerButton.setText(resourceBundle.getString("newCustomerButton"));
        updateButton.setText(resourceBundle.getString("updateCustomerButton"));
        deleteButton.setText(resourceBundle.getString("deleteCustomerButton"));

        int userId = getUserId();
        boolean noAppts = true;
        for (Appointment a : appts) {
            if (a.getUserId() == userId) {
                if (a.getStart().toLocalDate().equals(LocalDate.now()) && a.getStart().toLocalTime().isAfter(LocalTime.now()) && a.getStart().toLocalTime().isBefore(LocalTime.now().plusMinutes(15))) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, resourceBundle.getString("upcomingAppointment") +
                            "\n" + resourceBundle.getString("appointmentId") + a.getApptId() + "\n" + resourceBundle.getString("at") +
                            a.getStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a")));
                    alert.showAndWait();
                    noAppts = false;
                    break;
                }
            }
        }
        if (noAppts) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, resourceBundle.getString("nothingUpcoming"));
            alert.showAndWait();
        }
    }
}
