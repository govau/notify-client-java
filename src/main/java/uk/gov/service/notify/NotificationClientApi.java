package uk.gov.service.notify;


import java.util.HashMap;

public interface NotificationClientApi {

    NotificationResponse sendEmail(String templateId, String to, HashMap<String, String> personalisation) throws NotificationClientException;

    NotificationResponse sendSms(String templateId, String to, HashMap<String, String> personalisation) throws NotificationClientException;

    Notification getNotificationById(String notificationId) throws NotificationClientException;

    NotificationList getNotifications(String status, String notification_type) throws NotificationClientException;

}
