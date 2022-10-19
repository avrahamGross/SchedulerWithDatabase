package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.Month;

/**
 * A Class to control the reports Window
 */
public class ReportsController {
    @FXML
    private TextArea appointmentCount;

    @FXML
    private TextArea contactSchedule;

    @FXML
    private TextArea customerReport;
    @FXML
    private VBox vBox;

    /**
     * Close Reports Window
     * @param event
     */
    @FXML
    void close(ActionEvent event) {
    Stage stage = (Stage) vBox.getScene().getWindow();
    stage.close();
    }

    /**
     * Initialize Reports Window.
     * Iterate through each map and format text appropriately before adding text to Reports Window
     */
    public void initialize() {
        StringBuilder types = new StringBuilder("Types:\n");
        for (String t: Reports.getTypes().keySet()) {
            types.append("Type: " + t + " Amount: " + Reports.getTypes().get(t) + "\n");
        }
        types.append("\nMonths:\n");
        for (Month m: Reports.getMonths().keySet()) {
            types.append("Month: " + m.toString() + " Amount: " + Reports.getMonths().get(m) + "\n");
        }
        StringBuilder contactsSchedule = new StringBuilder(("Contacts:\n"));
        for (String c: Reports.getContactSchedule().keySet()) {
            contactsSchedule.append("Contact: " + c + "    Schedule:\n");
            for (Appointment a: Reports.getContactSchedule().get(c)){
                contactsSchedule.append("\t" + a.toString() + "\n");
            }
        }
        StringBuilder customers = new StringBuilder("Customers:\n");
        for (String c: Reports.getCustomersAppointmentCount().keySet()) {
            customers.append("Customer: " + c + " Amount: " + Reports.getCustomersAppointmentCount().get(c) + "\n");
        }
        appointmentCount.setText(types.toString());
        contactSchedule.setText(contactsSchedule.toString());
        customerReport.setText(customers.toString());
    }
}
