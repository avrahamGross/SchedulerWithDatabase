# SchedulerWithDatabase
Appointment Scheduler Application:
This application is used to track customers and appointment data while maintaining accurate data in the database.

Author: Avraham Gross
Contact: agros84@wgu.edu
Version: 1.0
Date: 10/13/2022

Java JDK Version 17.0.1
JavaFX SDK  11.0.2

The application opens to a log in form. The program verifies the login credentials before loading all existing customers.
From the customer window, the user can add, update or delete a customer.
Upon clicking delete, a window will open to confirm the delete. A delete removes all appointments for that customer.

The Appointments button opens the appointments window. Appointments are automatically filtered to the current week in the Week tab and the current month in the Month tab.
From the appointment window, the user can add, update or delete an appointment.
Upon clicking delete, a window will open to confirm the delete.

All Customer and Appointment data must be filled out correctly for the application to run properly.
Dates must be formatted in the format of yyyy-mm-dd. TIme must be formatted in the format hh:mm, 24hr format.

The report button opens a Reports window showing 3 reports.
The third report shows how many appointments a customer has, as long as the customer has at least one appointment scheduled.

