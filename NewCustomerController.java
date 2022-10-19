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
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * A Class to control a New Customer Window
 * @author Avraham Gross
 * @version 1.0
 */
public class NewCustomerController {
    private ResultSet rs = null;
    private ArrayList<String> countryCodes = new ArrayList<>();
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
     * Add new customer to customer table and database
     * @param event click on submit button
     * @throws SQLException
     * @throws NumberFormatException
     */
    @FXML
    void addNewCustomer(ActionEvent event) {
        try {
            Statement stmt = JDBC.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery("Select division_id from first_level_divisions where division = \"" + stateBox.getText() + "\"");
            int divisionId = -1;
            while (rs.next()) {
                divisionId = rs.getInt(1);
            }
            Customer newCustomer = new Customer(Integer.parseInt(idBox.getText()), nameBox.getText(), (addressBox.getText() + ", " + cityBox.getText()),
                    postalBox.getText(), phoneBox.getText(), ZonedDateTime.of(LocalDateTime.now(), LoginController.getLocalTimeZone()), LoginController.getUserName(),
                    null, null, divisionId, stateBox.getText());
            rs = stmt.executeQuery("select * from customers");
            boolean successfulInsert = newCustomer.updateCustomersTable(rs, "new");
             if (successfulInsert) {
            CustomerTableController.getCustomers().add(newCustomer);
             }
            Stage myStage = (Stage) vBox.getScene().getWindow();
            myStage.close();
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * Close New Customer Window
     * @param event click on close button
     */
    @FXML
    void cancel(ActionEvent event) {
        Stage myStage = (Stage) vBox.getScene().getWindow();
        myStage.close();
    }

    /**
     * Set Country text box to selected country. Set first level division combo box to correct type based on country
     * @param event click on country in combo box
     * @throws SQLException
     */
    @FXML
    void selectCountry(ActionEvent event) {
        String selectedCountry = countryComb.getSelectionModel().getSelectedItem().toString();
        countryBox.setText(selectedCountry);
        ObservableList<String> stateList = FXCollections.observableArrayList();
        stateBox.clear();
        String countryId = countryCodes.get(countryCodes.indexOf(selectedCountry) - 1);
        String query = "select Division from first_level_divisions where Country_ID = " + countryId;
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
     * Select state from combo box
     * @param event click on state in combo box
     * @throws NullPointerException if country selection is changed after state selection is made
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
     * Initialize New Customer Window. Fill country combo box
     * @throws SQLException
     */
    public void initialize() {
        ObservableList<String> countryList = FXCollections.observableArrayList();
        String countryQuery = "select * from countries";
        try {
            Statement stmt = JDBC.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(countryQuery);
            while (rs.next()) {
                countryCodes.add(Integer.toString(rs.getInt(1)));
                countryCodes.add(rs.getString(2));
                countryList.add(rs.getString(2));
            }
            rs.beforeFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        countryComb.setItems(countryList);
        idBox.setText(Integer.toString(Customer.getCustomerCount() + 1));
        pageLabel.setText(resourceBundle.getString("newCustomerPageLabel"));
        idLabel.setText(resourceBundle.getString("customerIdLabel"));
        nameLabel.setText(resourceBundle.getString("customerNameLabel"));
        addressLabel.setText(resourceBundle.getString("customerAddressLabel"));
        phoneLabel.setText(resourceBundle.getString("customerPhoneLabel"));
        countryLabel.setText(resourceBundle.getString("customerCountryLabel"));
        countryBox.setText(resourceBundle.getString("customerCountryText"));
        countryComb.setPromptText(resourceBundle.getString("customerCountryComb"));
        stateLabel.setText(resourceBundle.getString("customerStateLabel"));
        stateBox.setText(resourceBundle.getString("customerStateText"));
        stateComb.setPromptText(resourceBundle.getString("customerStateComb"));
        cityLabel.setText(resourceBundle.getString("customerCityLabel"));
        postalLabel.setText(resourceBundle.getString("customerZipLabel"));
        submitButton.setText(resourceBundle.getString("submitButton"));
        cancelButton.setText(resourceBundle.getString("cancelButton"));
    }
}
