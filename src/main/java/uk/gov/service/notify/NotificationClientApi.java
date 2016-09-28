package uk.gov.service.notify;


import java.util.HashMap;

public interface NotificationClientApi {

    /**
     * The sendEmail method will create an HTTPS POST request. A JWT token will be created and added as an Authorization header to the request.
     *
     * @param templateId Find templateId by clicking API info for the template you want to send
     * @param to the email address
     * @param personalisation optional HashMap representing the placeholders for the template if any. For example, key=name value=Bob
     * @return <code>NotificationResponse</code>
     * @throws NotificationClientException
     */
    NotificationResponse sendEmail(String templateId, String to, HashMap<String, String> personalisation) throws NotificationClientException;

    /**
     * The sendSms method will create an HTTPS POST request. A JWT token will be created and added as an Authorization header to the request.
     *
     * @param templateId Find templateId by clicking API info for the template you want to send
     * @param to the mobile phone number
     * @param personalisation optional HashMap representing the placeholders for the template if any. For example, key=name value=Bob
     * @return <code>NotificationResponse</code>
     * @throws NotificationClientException
     */
    NotificationResponse sendSms(String templateId, String to, HashMap<String, String> personalisation) throws NotificationClientException;

    /**
     * The getNotificationById will return a <code>Notification</code> for a given notification id.
     * The id is can be retrieved from the <code>NotificationResponse</code> of a <code>sendEmail</code> or <code>sendSms</code> request.
     *
     * @param notificationId the id of the notification.
     * @return <code>Notification</code>
     * @throws NotificationClientException
     */
    Notification getNotificationById(String notificationId) throws NotificationClientException;

    /**
     * The getNotifications will create a GET HTTPS request to retrieve all the notifications.
     *
     * @param status if status is not null notifications will only return notifications for the given status.
     *               Possible statuses are created|sending|delivered|permanent-failure|temporary-failure|technical-failure
     * @param notification_type if notification_type is not null only notification of the given status will be returned.
     *                          Possible notificationTypes are sms|email
     * @return <code>NotificationList</code>
     * @throws NotificationClientException
     */
    NotificationList getNotifications(String status, String notification_type) throws NotificationClientException;

}
