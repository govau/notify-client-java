package uk.gov.notifications.client.api;

import uk.gov.notifications.client.model.EmailRequest;
import uk.gov.notifications.client.model.NotificationCreatedResponse;
import uk.gov.notifications.client.model.SmsRequest;
import uk.gov.notifications.client.model.StatusRequest;
import uk.gov.notifications.client.model.StatusResponse;

/**
 * Client to handle communication with Gov Notify API.
 */
public interface GovNotifyApi {

    /**
     * Sends email notification.
     *
     * @param request object representing request to notify by email
     * @return notification handle
     */
    NotificationCreatedResponse sendEmail(EmailRequest request) throws GovNotifyClientException;

    /**
     * Sends email notification.
     *
     * @param email      valid email address
     * @param templateId id of the template to build a notification from
     * @return notification handle
     */
    NotificationCreatedResponse sendEmail(String email, String templateId)
            throws GovNotifyClientException;

    /**
     * Sends text notification.
     *
     * @param request object representing request to notify by sms
     * @return notification handle
     */
    NotificationCreatedResponse sendSms(SmsRequest request) throws GovNotifyClientException;

    /**
     * Sends text notification.
     *
     * @param phoneNumber valid phoneNumber number
     * @param templateId  id of the template to build a notification from
     * @return notification handle
     */
    NotificationCreatedResponse sendSms(String phoneNumber, String templateId)
            throws GovNotifyClientException;

    /**
     * Checks the status of sms/email notification request.
     *
     * @param request object representing request to check the status of notification
     * @return data about state of notification
     */
    StatusResponse checkStatus(StatusRequest request) throws GovNotifyClientException;

    /**
     * Checks the status of sms/email notification request.
     *
     * @param notificationId id of the notification
     * @return data about state of notification
     */
    StatusResponse checkStatus(String notificationId) throws GovNotifyClientException;
}
