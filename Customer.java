package sample;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * A Class to create a Customer
 * @author Avraham Gross
 * @version 1.0
 */
public class Customer extends Person {
    private static int customerCount = 1;
    private String address;
    private String postalCode;
    private String phone;
    private ZonedDateTime createDateTime;
    private String creator;
    private ZonedDateTime updateDateTime;
    private String updater;
    private int divisionId;
    private  String state;

    /**
     * Constructor to make a customer.
     * Call to Person constructor for id and name
     * Used to make a Customer from user input data with ZonedDateTime
     * @param id
     * @param name
     * @param address
     * @param postalCode
     * @param phone
     * @param createDateTime
     * @param creator
     * @param updateDateTime
     * @param updater
     * @param divisionId
     * @param state
     */
    public Customer(int id, String name, String address, String postalCode, String phone, ZonedDateTime createDateTime, String creator, ZonedDateTime updateDateTime, String updater, int divisionId, String state) {
        super(id, name);
        customerCount += 1;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createDateTime = createDateTime;
        this.creator = creator;
        this.updateDateTime = updateDateTime;
        this.updater = updater;
        this.divisionId = divisionId;
        this.state = state;
    }

    /**
     * Constructor to make a Customer. Increment customerCount accurately to generate correct id
     * Call to Person constructor for id and name
     * Used to make a Customer from database data with Timestamp
     * @param id
     * @param name
     * @param address
     * @param postalCode
     * @param phone
     * @param createDateTime
     * @param creator
     * @param updateDateTime
     * @param updater
     * @param divisionId
     * @param state
     */
    public Customer(int id, String name, String address, String postalCode, String phone, Timestamp createDateTime, String creator, Timestamp updateDateTime, String updater, int divisionId, String state) {
        super(id, name);
        if (customerCount == id) {
            customerCount += 1;
        }
        else {
            customerCount = id + 1;
        }
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
            this.createDateTime = ZonedDateTime.of(createDateTime.toLocalDateTime(), LoginController.getLocalTimeZone());
        this.creator = creator;
        if (updateDateTime != null) {
            this.updateDateTime = ZonedDateTime.of(updateDateTime.toLocalDateTime(), LoginController.getLocalTimeZone());
        }
        else {
            this.updateDateTime = null;
        }
        this.updater = updater;
        this.divisionId = divisionId;
        this.state = state;
    }

    /**
     * Constructor to make a Customer.
     * Takes no id parameter, uses customer count in overloaded constructor call
     * @param name
     * @param address
     * @param postalCode
     * @param phone
     * @param createDateTime
     * @param creator
     * @param updateDateTime
     * @param updater
     * @param divisionId
     * @param state
     */
    public Customer(String name, String address, String postalCode, String phone, Timestamp createDateTime, String creator, Timestamp updateDateTime, String updater, int divisionId, String state) {
        this(customerCount, name, address, postalCode, phone, createDateTime, creator, updateDateTime, updater, divisionId, state);
    }

    /**
     * Returns customer count
     * @return int customerCount
     */
    public static int getCustomerCount() {
        return customerCount;
    }

    /**
     * Set customer count
     * @param customerCount
     */
    public static void setCustomerCount(int customerCount) {
        Customer.customerCount = customerCount;
    }

    /**
     * Return customer address
     * @return String address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set customer address
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Return customer postal code
     * @return String postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Set customer postal code
     * @param postalCode
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Return customer phone number
     * @return String phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Set customer phone number
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Return customer create date and time
     * @return ZonedDateTime createDateTime
     */
    public ZonedDateTime getCreateDateTime() {
        return createDateTime;
    }

    /**
     * Set customer create date and time
     * @param createDateTime
     */
    public void setCreateDateTime(ZonedDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    /**
     * Return customer creator
     * @return String creator
     */
    public String getCreator() {
        return creator;
    }

    /**
     * Set customer creator
     * @param creator
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * Return customer update date and time
     * @return ZonedDateTime updateDateTime
     */
    public ZonedDateTime getUpdateDateTime() {
        return updateDateTime;
    }

    /**
     * Set customer update date and time
     * @param updateDateTime
     */
    public void setUpdateDateTime(ZonedDateTime updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    /**
     * Return customer updater
     * @return String updater
     */
    public String getUpdater() {
        return updater;
    }

    /**
     * Set customer updater
     * @param updater
     */
    public void setUpdater(String updater) {
        this.updater = updater;
    }

    /**
     * Return customer division Id
     * @return int divisionId
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * Set customer division id
     * @param divisionId
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * Return custoer state
     * @return String state
     */
    public String getState() {
        return state;
    }

    /**
     * Set customer state
     * @param state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Update customer data
     * @param name
     * @param address
     * @param phone
     * @param postalCode
     * @param divisionId
     * @param updateDateTime
     * @param updater
     * @return this customer
     */
    public Customer updateCustomer(String name, String address, String phone, String postalCode, int divisionId, String state, ZonedDateTime updateDateTime, String updater){
        setName(name);
        this.address = address;
        this.phone = phone;
        this.postalCode = postalCode;
        this.divisionId = divisionId;
        this.state = state;
        this.updateDateTime = updateDateTime;
        this.updater = updater;
        return this;

    }

    /**
     * Update customer data in database. If new cusotmer, use insertRow. If updating existing customer, use updateRow
     * @param resultSet
     * @param type
     * @return boolean true
     * @throws SQLException
     */
    public boolean updateCustomersTable(ResultSet resultSet, String type) throws SQLException {
        resultSet.absolute(1);
        if (type.equals("new")) {
            resultSet.moveToInsertRow();
            resultSet.updateInt(1, this.getId());
        }
        resultSet.updateString(2, this.getName());
        resultSet.updateString(3, this.address);
        resultSet.updateString(4, this.postalCode);
        resultSet.updateString(5, this.phone);
        resultSet.updateTimestamp(6, Timestamp.valueOf(this.createDateTime.withZoneSameLocal(ZoneId.of("UTC")).toLocalDateTime()));
        resultSet.updateString(7, this.creator);
        if (this.updateDateTime != null) {
            resultSet.updateTimestamp(8, Timestamp.valueOf(this.updateDateTime.withZoneSameLocal(ZoneId.of("UTC")).toLocalDateTime()));
            resultSet.updateString(9, this.updater);
        }
        resultSet.updateInt(10, this.divisionId);
        if (type.equals("new")) {
            resultSet.insertRow();
        }
        else {
            resultSet.updateRow();
        }
        resultSet.moveToCurrentRow();
        return true;
    }
}
