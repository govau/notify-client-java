# GOV.UK Notify - notifications-java-client

This is the Java client for the GOV.UK Notify API.

It provides client calls, response marshalling and authentication for the GOV.UK Notify API.

This application is built using Java 8.


## Usage

Before usage, creaate a service through the Notify admin console. This allows access to the API credentials for your application. There are normal, test and team API keys available.
Read the [API documentation](https://www.notifications.service.gov.uk/documentation) for more information.

Add the maven dependency to your project.
mvn dependency to populate 

**Import the NotificationClient**

    ```import uk.gov.service.notify.NotificationClient;
    import uk.gov.service.notify.Notification;
    import uk.gov.service.notify.NotificationList;
    import uk.gov.service.notify.NotificationResponse;
    ```

**Create a new instance of NotificationClient and objects returned by the client**:

    `NotificationClient client = new NotificationClient(secret, serviceId, "https://api.notifications.service.gov.uk");`
    
**Send and email or sms**:

    `NotificationResponse response = client.sendEmail(templateId, emailAddress, personalisation);`

or

    `NotificationResponse response client.sendSms(templateId, mobileNumber, personalisation);`

* `mobileNumber` is the mobile phone number for the notification
    * Only UK mobiles are supported
    * Must start with +44
    * Must not have leading zero
    * Must not have any whitespace, punctuation etc.
    * valid format is +447777111222
* `emailAddress` is the email address for the notification
* `template_id` is the template to send
    * Must be an uuid that identifies a valid template. Templates are created in the admin tools.
* `personalisation` is the template to send
    * Must be a JSON string, with keys matching the placeholders in the template, eg {"name": "Chris"}

**Fetch notification by id**:

    `Notification notification = client.getNotificationById(notificationId);`

* `notificationId` is the id of the notification. The Id is part of the notification object returned when `sendEmail` or `sendSms` is called.
 
**Fetch all notification for your service**:

    `Notification notification = client.getNotification(status, notificationType);`

* `status` is a string that represents the status of the notifications you want returned. Options for status:
    * null (for all status types)
    * delivered 
    * failed
    * sending
* `notificationType` is the type of notification to return. Options for notificaitonType:
    * null (for both types)
    * email 
    * sms


### Testing
There is a main class that can be used to test the integration. It is also useful to read this class to see how to integrate with the notification client.
On a command line build the project with maven:

`> mvn test`

Then run the mvn execute java command with the excute arguments:

`>  mvn exec:java -Dexec.mainClass=TestNotificationClient -Dexec.args="api-key-id service-id https://api.notifications.service.gov.uk"`

You will be prompted to select an options to submit the API call.
