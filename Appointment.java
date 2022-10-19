package sample;

import javafx.beans.value.ObservableValue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * A Class used to store Appointment data and manipulate it as needed
 * @author Avraham Gross
 * @version 1.0
 */
public class Appointment {
    private static int apptCount = 1;
    private final int apptId;
    private String title;
    private String description;
    private String location;
    private String contactName;
    private int contactId;
    private String type;
    private ZonedDateTime start;
    private ZonedDateTime end;
    private int customerId;
    private int userId;
    private ZonedDateTime createDateTime;
    private String creator;
    private ZonedDateTime updateDateTime;
    private String updater;

    /**
     * Constructor to make an Appointment. Auto increments apptCount for each appointment made, if apptCount is less than the apptId argument
     * Takes an Update time and Updater as arguments
     * @param apptId
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param createDateTime
     * @param creator
     * @param updateDateTime
     * @param updater
     * @param customerId
     * @param userId
     * @param contactId
     * @param contactName
     */
    public Appointment(int apptId, String title, String description, String location, String type, Timestamp start, Timestamp end, Timestamp createDateTime,
                       String creator, Timestamp updateDateTime, String updater, int customerId, int userId, int contactId, String contactName) {
        if (apptCount <= apptId) {
            apptCount = apptId + 1;
        }
        this.apptId = apptId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = ZonedDateTime.of(start.toLocalDateTime(), LoginController.getLocalTimeZone());
        this.end = ZonedDateTime.of(end.toLocalDateTime(), LoginController.getLocalTimeZone());
        this.customerId = customerId;
        this.userId = userId;
        this.contactName = contactName;
        this.contactId = contactId;
        this.createDateTime = ZonedDateTime.of(createDateTime.toLocalDateTime(), LoginController.getLocalTimeZone());
        this.creator = creator;
        if (updateDateTime != null) {
            this.updateDateTime = ZonedDateTime.of(updateDateTime.toLocalDateTime(), LoginController.getLocalTimeZone());
        }
        else {
            this.updateDateTime = null;
        }
        this.updater = updater;
    }

    /**
     * Constructor to make an Appointment. Auto increments apptCount for each appointment made, if apptCount is less than the apptId argument
     * Does not take update time and updater as arguments. Sets them to null
     * @param apptId
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param createDateTime
     * @param creator
     * @param customerId
     * @param userId
     * @param contactId
     * @param contactName
     */
    public Appointment(int apptId, String title, String description, String location, String type, ZonedDateTime start, ZonedDateTime end, ZonedDateTime createDateTime,
                       String creator, int customerId, int userId, int contactId, String contactName) {
        apptCount = apptId + 1;
        this.apptId = apptId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
        this.contactName = contactName;
        this.contactId = contactId;
        this.createDateTime = createDateTime;
        this.creator = creator;
        this.updateDateTime = null;
        this.updater = null;
    }

    /**
     * Get appointment count
     * @return int apptCount
     */
        public static int getApptCount() {
        return apptCount;
    }

    /**
     * Set appointment count
     * @param apptCount
     */
    public static void setApptCount(int apptCount) {
        Appointment.apptCount = apptCount;
    }

    /**
     * Get appointment Id
     * @return int apptId
     */
    public int getApptId() {
        return apptId;
    }

    /**
     * Get appointment title
     * @return String title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set appointment title
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get appointment description
     * @return String description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set appointment description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get appointment locaiton
     * @return String location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Set appointment location
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Get appointment type
     * @return String type
     */
    public String getType() {
        return type;
    }

    /**
     * Set appointment type
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get appointment start time
     * @return ZonedDateTime tart
     */
    public ZonedDateTime getStart() {
        return start;
    }

    /**
     * Set appointment start
     * @param start
     */
    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    /**
     * Get appointment end time
     * @return ZonedDateTime end
     */
    public ZonedDateTime getEnd() {
        return end;
    }

    /**
     * Set appointment end time
     * @param end
     */
    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    /**
     * Get customer id for appointment
     * @return int customerId
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Set customer id for appointment
     * @param customerId
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Get user id for appointment
     * @return int userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Set user id for appointment
     * @param userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Get appointment contact name
     * @return String contactName
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Set appointment contact name
     * @param contactName
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * Get appointment create date and time
     * @return ZonedDateTime createDateTime
     */
    public ZonedDateTime getCreateDateTime() {
        return createDateTime;
    }

    /**
     * Set appointment create date and time
     * @param createDateTime
     */
    public void setCreateDateTime(ZonedDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    /**
     * Get appointment creator
     * @return String creator
     */
    public String getCreator() {
        return creator;
    }

    /**
     * Set appointment creator
     * @param creator
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * Get appointment update date and time
     * @return ZonedDateTime updateDateTime
     */
    public ZonedDateTime getUpdateDateTime() {
        return updateDateTime;
    }

    /**
     * Set appointment update date and time
     * @param updateDateTime
     */
    public void setUpdateDateTime(ZonedDateTime updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    /**
     * Get appointment updater
     * @return String updater
     */
    public String getUpdater() {
        return updater;
    }

    /**
     * Set appointment updater
     * @param updater
     */
    public void setUpdater(String updater) {
        this.updater = updater;
    }

    /**
     * Update current appointment with new data. Return updated instance of appointment
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param updateDateTime
     * @param updater
     * @param customerId
     * @param contactId
     * @param contactName
     * @return Appointment this (appointment)
     */
    public Appointment updateAppt(String title, String description, String location, String type, ZonedDateTime start, ZonedDateTime end,
                                  ZonedDateTime updateDateTime, String updater, int customerId, int contactId, String contactName) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.updateDateTime = updateDateTime;
        this.updater = updater;
        this.customerId = customerId;
        this.contactId =  contactId;
        this.contactName = contactName;
        return this;
    }

    /**
     * Update database with appointment. If a new appointment, insert appointment. If appointment already exists, update appointment
     * @param resultSet
     * @param type
     * @return boolean true
     * @throws SQLException
     */
    public boolean updateAppointments(ResultSet resultSet, String type) throws SQLException {
        resultSet.absolute(1);
        if (type.equals("new")) {
            resultSet.moveToInsertRow();
            resultSet.updateInt(1, this.apptId);
        }
        resultSet.updateString(2, this.title);
        resultSet.updateString(3, this.description);
        resultSet.updateString(4, this.location);
        resultSet.updateString(5, this.type);
        resultSet.updateTimestamp(6, Timestamp.valueOf(this.start.withZoneSameLocal(ZoneId.of("UTC")).toLocalDateTime()));
        resultSet.updateTimestamp(7, Timestamp.valueOf(this.end.withZoneSameLocal(ZoneId.of("UTC")).toLocalDateTime()));
        resultSet.updateTimestamp(8, Timestamp.valueOf(this.createDateTime.withZoneSameLocal(ZoneId.of("UTC")).toLocalDateTime()));
        resultSet.updateString(9, this.creator);
        if (this.updateDateTime != null) {
            resultSet.updateTimestamp(10, Timestamp.valueOf(this.updateDateTime.withZoneSameLocal(ZoneId.of("UTC")).toLocalDateTime()));
            resultSet.updateString(11, this.updater);
        }
        resultSet.updateInt(12, this.customerId);
        resultSet.updateInt(13, this.userId);
        resultSet.updateInt(14, this.contactId);
        if (type.equals("new")) {
            resultSet.insertRow();
        }
        else {
            resultSet.updateRow();
        }
        resultSet.moveToCurrentRow();
        return true;
    }

    /**
     * Returns string containing relevant data about an appointment
     * @return String string representation of appointment
     */
    @Override
    public String toString() {
        return "Appointment: " +
                "Id: " + apptId +
                ", Title: " + title + '\'' +
                ", Description: " + description + '\'' +
                ", Location: " + location + '\'' +
                ", Type: " + type + '\'' +
                ", Start: " + start +
                ", End: " + end +
                ", CustomerId: " + customerId;
    }
}
