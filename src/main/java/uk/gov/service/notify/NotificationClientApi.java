package uk.gov.service.notify;


import java.util.HashMap;

public interface NotificationClientApi {

    /**
     * The sendEmail method will create an HTTPS POST request. A JWT token will be created and added as an Authorization header to the request.
     *
     * @param templateId      Find templateId by clicking API info for the template you want to send
     * @param emailAddress    The email address
     * @param personalisation HashMap representing the placeholders for the template if any. For example, key=name value=Bob
     * @param reference       A reference specified by the service for the notification. Get all notifications can be filtered by this reference.
     *                        This reference can be unique or used used to refer to a batch of notifications.
     * @return <code>SendEmailResponse</code>
     * @throws NotificationClientException
     */
    public SendEmailResponse sendEmail(String templateId, String emailAddress, HashMap<String, String> personalisation, String reference) throws NotificationClientException;

    /**
     * The sendEmail method will create an HTTPS POST request. A JWT token will be created and added as an Authorization header to the request.
     *
     * @param templateId Find templateId by clicking API info for the template you want to send
     * @param emailAddress The email address
     * @param personalisation HashMap representing the placeholders for the template if any. For example, key=name value=Bob
     * @return <code>SendEmailResponse</code>
     * @throws NotificationClientException
     */
    SendEmailResponse sendEmail(String templateId, String emailAddress, HashMap<String, String> personalisation) throws NotificationClientException;

    /**
     * The sendEmail method will create an HTTPS POST request. A JWT token will be created and added as an Authorization header to the request.
     *
     * @param templateId Find templateId by clicking API info for the template you want to send
     * @param emailAddress The email address
     * @return <code>SendEmailResponse</code>
     * @throws NotificationClientException
     */
    SendEmailResponse sendEmail(String templateId, String emailAddress) throws NotificationClientException;

    /**
     * The sendSms method will create an HTTPS POST request. A JWT token will be created and added as an Authorization header to the request.
     *
     * @param templateId      Find templateId by clicking API info for the template you want to send
     * @param phoneNumber              The mobile phone number
     * @param personalisation HashMap representing the placeholders for the template if any. For example, key=name value=Bob
     * @param reference       A reference specified by the service for the notification. Get all notifications can be filtered by this reference.
     *                        This reference can be unique or used used to refer to a batch of notifications.
     * @return <code>SendSmsResponse</code>
     * @throws NotificationClientException
     */
    public SendSmsResponse sendSms(String templateId, String phoneNumber, HashMap<String, String> personalisation, String reference) throws NotificationClientException;

    /**
     * The sendSms method will create an HTTPS POST request. A JWT token will be created and added as an Authorization header to the request.
     *
     * @param templateId Find templateId by clicking API info for the template you want to send
     * @param phoneNumber The mobile phone number
     * @param personalisation HashMap representing the placeholders for the template if any. For example, key=name value=Bob
     * @return <code>SendSmsResponse</code>
     * @throws NotificationClientException
     */
    SendSmsResponse sendSms(String templateId, String phoneNumber, HashMap<String, String> personalisation) throws NotificationClientException;

    /**
     * The sendSms method will create an HTTPS POST request. A JWT token will be created and added as an Authorization header to the request.
     *
     * @param templateId Find templateId by clicking API info for the template you want to send
     * @param phoneNumber The mobile phone number
     * @return <code>SendSmsResponse</code>
     * @throws NotificationClientException
     */
    SendSmsResponse sendSms(String templateId, String phoneNumber) throws NotificationClientException;

    /**
     * The getNotificationById method will return a <code>Notification</code> for a given notification id.
     * The id is can be retrieved from the <code>NotificationResponse</code> of a <code>sendEmail</code> or <code>sendSms</code> request.
     *
     * @param notificationId The id of the notification.
     * @return <code>Notification</code>
     * @throws NotificationClientException
     */
    Notification getNotificationById(String notificationId) throws NotificationClientException;

    /**
     * The getNotifications method will create a GET HTTPS request to retrieve all the notifications.
     *
     * @param status If status is not null notifications will only return notifications for the given status.
     *               Possible statuses are created|sending|delivered|permanent-failure|temporary-failure|technical-failure
     * @param notification_type If notification_type is not null only notification of the given status will be returned.
     *                          Possible notificationTypes are sms|email
     * @return <code>NotificationList</code>
     * @throws NotificationClientException
     */

    NotificationList getNotifications(String status, String notification_type) throws NotificationClientException;

}
