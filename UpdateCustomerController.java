package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * This class controls the Update Customer Window
 * @author Avraham Gross
 * @version 1.0
 */
public class UpdateCustomerController {
    private ResultSet rs = null;
    private ArrayList<String> divisionIdCodes = new ArrayList<>();
    private static ResourceBundle resourceBundle = LoginController.getResources();

    @FXML
    private TextField addressBox;

    @FXML
    private Label addressLabel;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField cityBox;

    @FXML
    private Label cityLabel;

    @FXML
    private TextField countryBox;

    @FXML
    private ComboBox<String> countryComb;

    @FXML
    private Label countryLabel;

    @FXML
    private TextField idBox;

    @FXML
    private Label idLabel;

    @FXML
    private TextField nameBox;

    @FXML
    private Label nameLabel;

    @FXML
    private Label pageLabel;

    @FXML
    private TextField phoneBox;

    @FXML
    private Label phoneLabel;

    @FXML
    private TextField postalBox;

    @FXML
    private Label postalLabel;

    @FXML
    private TextField stateBox;

    @FXML
    private ComboBox<String> stateComb;

    @FXML
    private Label stateLabel;

    @FXML
    private Button submitButton;

    @FXML
    private VBox vBox;

    /**
     * Updates customer data, ands it to the customer list and updates the database
     * @param event click on submit button
     * @throws SQLException if connection to database fails or if invalid data types put in wrong fields
     */
    @FXML
    void updateCustomer(ActionEvent event) {
        try {
            Statement stmt = JDBC.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery("Select division_id from first_level_divisions where division = \"" + stateBox.getText() + "\"");
            int divisionId = -1;
            while (rs.next()) {
                divisionId = rs.getInt(1);
            }
            int customerId = CustomerTableController.getSelectedCustomer().getId();
            Customer updatedCustomer = CustomerTableController.getSelectedCustomer().updateCustomer(nameBox.getText(), (addressBox.getText() + ", " + cityBox.getText()),
                    phoneBox.getText(), postalBox.getText(), divisionId, stateBox.getText(), ZonedDateTime.of(LocalDateTime.now(), LoginController.getLocalTimeZone()), LoginController.getUserName());

            rs = stmt.executeQuery("select * from customers where customer_id = " + updatedCustomer.getId());
            boolean successfulUpdate = updatedCustomer.updateCustomersTable(rs, "update");
            if (successfulUpdate) {
                System.out.println(updatedCustomer.getUpdateDateTime().toString());
                CustomerTableController.getCustomers().set(CustomerTableController.getSelectedCustomerIndex(),updatedCustomer);
            }
            CustomerTableController.resetSelected();
            Stage myStage = (Stage) vBox.getScene().getWindow();
            myStage.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes Update Customer Window
     * @param event click on close button
     */
    @FXML
    void cancel(ActionEvent event) {
        Stage myStage = (Stage) vBox.getScene().getWindow();
        myStage.close();
    }

    /**
     * Choose Country of customer from combo box. State combo box fills based on selection
     * @param event click on combo box selection
     * @throws SQLException if connetion to databsae is invalid or query has an error
     */
    @FXML
    void selectCountry(ActionEvent event) {
        String selectedCountry = countryComb.getSelectionModel().getSelectedItem().toString();
        countryBox.setText(selectedCountry);
        stateBox.clear();
        ObservableList<String> stateList = FXCollections.observableArrayList();
        String query = "select Division from first_level_divisions where Country_ID = (select country_id from countries where country = \"" + selectedCountry + "\");";
        try {
            Statement stmt = JDBC.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                stateList.add(rs.getString(1));
            }
            rs.beforeFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        stateComb.setItems(stateList);
    }

    /**
     * Choose state of customer from combo box.
     * @param event click on combo box selection
     * @throws NullPointerException if different country selected after state is selected
     */
    @FXML
    void selectState(ActionEvent event) {
        try {
            String selected = stateComb.getSelectionModel().getSelectedItem().toString();
            stateBox.setText((selected));
        } catch (NullPointerException e) {
            stateComb.getSelectionModel().clearSelection();
            stateBox.setText("");
        }
    }

    /**
     * Initializes Update Customer Window. Fills the text fields with the selected customer current data
     * @throws SQLException if connection is invalid or query is invalid
     */
    public void initialize() {
        ObservableList<String> countryList = FXCollections.observableArrayList();
        ObservableList<String> stateList = FXCollections.observableArrayList();
        String currentCountry = "";
        String countryQuery = "select fl.division_id, c.country, fl.division from first_level_divisions fl " +
                "inner join countries c on c.Country_ID = fl.Country_ID;";
        try {
            Statement stmt = JDBC.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet countryRS = stmt.executeQuery(countryQuery);
            while (countryRS.next()) {
                divisionIdCodes.add(Integer.toString(countryRS.getInt(1)));
                if (!countryList.contains(countryRS.getString(2)))
                    countryList.add(countryRS.getString(2));
                if (countryRS.getInt(1) == CustomerTableController.getSelectedCustomer().getDivisionId()) {
                    stateBox.setText(countryRS.getString(3));
                    currentCountry = countryRS.getString(2);
                }
            }
            countryRS.beforeFirst();
            String stateQuery = "select division from first_level_divisions fl where fl.country_id = (select country_id from first_level_divisions where division_id = " + (CustomerTableController.getSelectedCustomer().getDivisionId()) + ");";
            ResultSet stateRS = stmt.executeQuery(stateQuery);
            while (stateRS.next()) {
                stateList.add(stateRS.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        countryComb.setItems(countryList);
        idBox.setText(Integer.toString(CustomerTableController.getSelectedCustomer().getId()));
        nameBox.setText(CustomerTableController.getSelectedCustomer().getName());
        phoneBox.setText(CustomerTableController.getSelectedCustomer().getPhone());
        if (CustomerTableController.getSelectedCustomer().getAddress().contains(",")) {
            addressBox.setText(CustomerTableController.getSelectedCustomer().getAddress().substring(0, CustomerTableController.getSelectedCustomer().getAddress().indexOf(',')));
            cityBox.setText(CustomerTableController.getSelectedCustomer().getAddress().substring(CustomerTableController.getSelectedCustomer().getAddress().indexOf(',') + 2));
        } else {
            addressBox.setText(CustomerTableController.getSelectedCustomer().getAddress());
        }
        postalBox.setText(CustomerTableController.getSelectedCustomer().getPostalCode());
        countryBox.setText(currentCountry);
        stateComb.setItems(stateList);

        pageLabel.setText(resourceBundle.getString("updateCustomerPageLabel"));
        idLabel.setText(resourceBundle.getString("customerIdLabel"));
        nameLabel.setText(resourceBundle.getString("customerNameLabel"));
        addressLabel.setText(resourceBundle.getString("customerAddressLabel"));
        phoneLabel.setText(resourceBundle.getString("customerPhoneLabel"));
        countryLabel.setText(resourceBundle.getString("customerCountryLabel"));
        countryComb.setPromptText(resourceBundle.getString("customerCountryComb"));
        stateLabel.setText(resourceBundle.getString("customerStateLabel"));
        stateComb.setPromptText(resourceBundle.getString("customerStateComb"));
        cityLabel.setText(resourceBundle.getString("customerCityLabel"));
        postalLabel.setText(resourceBundle.getString("customerZipLabel"));
        submitButton.setText(resourceBundle.getString("submitButton"));
        cancelButton.setText(resourceBundle.getString("cancelButton"));
    }
}

