package sample;

import javafx.collections.ObservableList;


import java.time.Month;
import java.util.ArrayList;
import java.util.Hashtable;

/***
 * A Class to generate Reports
 * @author Avraham Gross
 * @version 1.0
 */
public class Reports {
    /**
     * HashTable to contain type and frequency
     */
    private static Hashtable<String, Integer> types;
    /**
     * HashTable to contain month and appointment count
     */
    private static Hashtable<Month, Integer> months;
    /**
     * HashTable to contain contact name and schedule
     */
    private static Hashtable<String, ArrayList<Appointment>> contactSchedule;
    /**
     * HashTable to contain customer name and appointment frequency
     */
    private static Hashtable<String, Integer> customersAppointmentCount;

    /**
     * Return types HashTable
     * @return HashTable types
     */
    public static Hashtable<String, Integer> getTypes() {
        return types;
    }

    /**
     * Return months HashTable
     * @return HashTable months
     */
    public static Hashtable<Month, Integer> getMonths() {
        return months;
    }

    /**
     * Return contact schedule HashTable
     * @return HashTable contactSchedule
     */
    public static Hashtable<String, ArrayList<Appointment>> getContactSchedule() {
        return contactSchedule;
    }

    /**
     * Return customer appointment count HashTable
     * @return HashTable customerAppointmentCount
     */
    public static Hashtable<String, Integer> getCustomersAppointmentCount() {
        return customersAppointmentCount;
    }

    /**
     * Generate all reports for Reports Window.
     * Iterate through each appointment and add to each HashTable appropriately
     * @param appts
     * @param customers
     */
    public static void makeReports(ObservableList<Appointment> appts, ObservableList<Customer> customers) {
        types = new Hashtable<>();
        months = new Hashtable<>();
        contactSchedule = new Hashtable<>();
        customersAppointmentCount = new Hashtable<>();
        for (Appointment a : appts) {
            addToTypesMap(a);
            addToMonthMap(a);
            contactsReport(a);
            customerReport(a, customers);
        }
    }

    /**
     * Track frequency of appointments per month
     * @param a
     */
    private static void addToMonthMap(Appointment a) {
        Month month = a.getStart().toLocalDate().getMonth();
        if (months.isEmpty() || !months.containsKey(month)) {
            months.put(month, 1);
        }
        else {
            months.replace(month, months.get(month) + 1);
        }
    }

    /**
     * Track frequency of appointments by type
     * @param a
     */
    private static void addToTypesMap(Appointment a) {
        if (types.isEmpty() || !types.containsKey(a.getType()))
            types.put(a.getType(), 1);
        else {
            types.replace(a.getType(), types.get(a.getType()) + 1);
        }
    }

    /**
     * Gather schedule for each contact
     * @param a
     */
    private static void contactsReport(Appointment a) {
        if (contactSchedule.isEmpty() || !contactSchedule.containsKey(a.getContactName())) {
            ArrayList<Appointment> list = new ArrayList<>();
            list.add(a);
            contactSchedule.put(a.getContactName(), list);
        }
        else {
            contactSchedule.get(a.getContactName()).add(a);
        }
    }

    /**
     * Track frequency of appointments by customer
     * @param a
     * @param customers
     */
    private static void customerReport(Appointment a, ObservableList<Customer> customers) {
        String customerName = "";
        for (Customer c: customers) {
            if (c.getId() == a.getCustomerId()) {
                customerName = c.getName();
            }
        }
        if (customersAppointmentCount.isEmpty() || !customersAppointmentCount.containsKey(customerName)) {
            customersAppointmentCount.put(customerName, 1);
        }
        else {
            customersAppointmentCount.replace(customerName, customersAppointmentCount.get(customerName) + 1);
        }
    }

}
