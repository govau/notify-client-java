## 3.0.0-RELEASE

### Changed
* Using version 2 of the notification-api.
* Update to `NotificationClient.sendSms()`: 
    * added `reference`: an optional unique identifier for the notification or an identifier for a batch of notifications. `reference` can be an empty string or null.
    * returns SendSmsResponse, this object only contains the necessary information about the notification.
    * only one method signature:
            `public SendSmsResponse sendSms(String templateId, String phoneNumber, HashMap<String, String> personalisation, String reference) throws NotificationClientException;`
      Where `personalisation` can be an empty map or null and `reference` can be an empty string or null.
* Update to `NotificationClient.sendEmail()`: 
    * added `reference`: an optional unique identifier for the notification or an identifier for a batch of notifications. `reference` can be an empty string or null.
    * returns SendEmailResponse, this object only contains the necessary information about the notification.
    * only one method signature:
            `public SendEmailResponse sendEmail(String templateId, String emailAddress, HashMap<String, String> personalisation, String reference) throws NotificationClientException;`
      Where `personalisation` can be an empty map or null and `reference` can be an empty string or null.
* Notification class has been changed; return type of `NotificationClient.getNotificationById(id)`, see the README for details.
* `NotificationClient.getAllNotifications()` 
    * Notifications can be filtered by `reference`, see the README for details.
    * Notifications can be filtered by `olderThanId`, see the README for details.
    * NotificationList response has changed, see the README for details.
* `NotificationClient` removed the constructors containing the serviceId, which is no longer needed because the api key contains the service id.