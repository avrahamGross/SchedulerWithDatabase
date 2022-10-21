# SchedulerWithDatabase
Appointment Scheduler Application:
This application is used to track customers and appointment data while maintaining accurate data in the database.

Author: Avraham Gross
<br>
Contact: agros84@wgu.edu
<br>
Version: 1.0
<br>
Date: 10/13/2022

Java JDK Version 17.0.1
<br>
JavaFX SDK  11.0.2

The application opens to a log in form. The program verifies the login credentials before loading all existing customers.
Upon login, a custom message informs the user of an upcoming appointment in the next 15 minutes.
From the customer window, the user can add, update or delete a customer.
Upon clicking delete, a window will open to confirm the delete. A delete removes all appointments for that customer.

The Appointments button opens the appointments window. Appointments are automatically filtered to the current week in the Week tab and the current month in the Month tab.
From the appointment window, the user can add, update or delete an appointment.
Upon clicking delete, a window will open to confirm the delete.

All Customer and Appointment data must be filled out correctly for the application to run properly.
Dates must be formatted in the format of yyyy-mm-dd. Time must be formatted in the format hh:mm, 24hr format.
Business hours are set to EST, user input should be local time but will be validated against business hours

The report button opens a Reports window showing 3 reports:
<br>
The first report shows how many appointments by type and by month.
<br>
The second report shows a schedule for each contact in the database.
<br>
The third report shows how many appointments a customer has, as long as the customer has at least one appointment scheduled.
<br>

Database connection class given by University, not configured for use outside of project

Screenshots of working applicaion:
![image](https://user-images.githubusercontent.com/89807553/197191634-db4a064f-e5c6-4146-a38f-f1aedae05b96.png)
<br>
![image](https://user-images.githubusercontent.com/89807553/197191719-8819be10-89cd-436f-b5ba-1e8153245918.png)
<br>
![image](https://user-images.githubusercontent.com/89807553/197191799-377ebd74-962c-4f05-a5e7-ad7872e16729.png)
