## 3.2.0-RELEASE
* Template endpoints added to the NotificationsClient
* `getTemplateById` - get the latest version of a template by id.
* `getTemplateVersion` - get the template by id and version.
* `getAllTemplates` - get all templates, can be filtered by template type.
* `getTemplatePreview` - get the contents of a template with the placeholders replaced with the given personalisation.
* See the README for more information about the new template methods. 


## 3.1.3-RELEASE

### Changed
* Updated the jose4j dependency in light of the security issues: [https://auth0.com/blog/critical-vulnerability-in-json-web-encryption/](https://auth0.com/blog/critical-vulnerability-in-json-web-encryption/)


## 3.1.2-RELEASE

### Changed
* Added SSLContext to `NotificationClient` constructor, to allow clients to be created with a specified SSL Context.


## 3.1.1-RELEASE

### Fixed
* The Client UserAgent is now populated correctly.


## 3.1.0-RELEASE

### Changed
* `NotificationClientException` now has a getter for the httpResult, `NotificationClientException.getHttpResult()`
* Added `NotificationClientApi` interface for `NotificationClient`
  * The interface is useful if you want to stub the `NotificationClient` for tests.


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
