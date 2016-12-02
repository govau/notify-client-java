package uk.gov.service.notify;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ClientIntegrationTestIT {

    @Test
    public void testEmailNotificationIT() throws NotificationClientException, InterruptedException {
        NotificationClient client = getClient();
        SendEmailResponse emailResponse = sendEmail(client);
        Notification notification = client.getNotificationById(emailResponse.getNotificationId().toString());
        assertNotification(notification);
    }

    @Test
    public void testSmsNotificationIT() throws NotificationClientException, InterruptedException {
        NotificationClient client = getClient();
        SendSmsResponse response = sendSms(client);
        Notification notification = client.getNotificationById(response.getNotificationId().toString());
        assertNotification(notification);
    }

    @Test
    public void testGetAllNotifications() throws NotificationClientException {
        NotificationClient client = getClient();
        NotificationList notificationList = client.getNotifications(null, null);
        assertNotNull(notificationList);
        assertNotNull(notificationList.getTotal());
        assertNotNull(notificationList.getNotifications());
        assertFalse(notificationList.getNotifications().isEmpty());
        // Just check the first notification in the list.
        assertNotification(notificationList.getNotifications().get(0));
    }

    @Test
    public void testEmailNotificationWithoutPersonalisationReturnsErrorMessageIT() {
        NotificationClient client = getClient();
        try {
            client.sendEmail(System.getenv("EMAIL_TEMPLATE_ID"), System.getenv("FUNCTIONAL_TEST_EMAIL"));
            fail("Expected NotificationClientException: Template missing personalisation: name");
        } catch (NotificationClientException e) {
            assert(e.getMessage().contains("Template missing personalisation: name"));
            assert(e.getMessage().contains("Status code: 400"));
        }
    }

    @Test
    public void testSmsNotificationWithoutPersonalisationReturnsErrorMessageIT() {
        NotificationClient client = getClient();
        try {
            client.sendSms(System.getenv("SMS_TEMPLATE_ID"), System.getenv("FUNCTIONAL_TEST_NUMBER"));
            fail("Expected NotificationClientException: Template missing personalisation: name");
        } catch (NotificationClientException e) {
            assert(e.getMessage().contains("Template missing personalisation: name"));
            assert(e.getMessage().contains("Status code: 400"));
        }
    }

    private NotificationClient getClient(){
        String apiKey = System.getenv("API_KEY");
        String baseUrl = System.getenv("NOTIFY_API_URL");
        NotificationClient client = new NotificationClient(apiKey, baseUrl);
        return client;
    }

    private SendEmailResponse sendEmail(final NotificationClient client) throws NotificationClientException {
        HashMap<String, String> personalisation = new HashMap<>();
        String uniqueName = UUID.randomUUID().toString();
        personalisation.put("name", uniqueName);
        SendEmailResponse response = client.sendEmail(System.getenv("EMAIL_TEMPLATE_ID"),
                System.getenv("FUNCTIONAL_TEST_EMAIL"), personalisation);
        assertNotificationEmailResponse(response, uniqueName);
        return response;
    }

    private SendSmsResponse sendSms(final NotificationClient client) throws NotificationClientException {
        HashMap<String, String> personalisation = new HashMap<>();
        String uniqueName = UUID.randomUUID().toString();
        personalisation.put("name", uniqueName);
        SendSmsResponse response = client.sendSms(System.getenv("SMS_TEMPLATE_ID"), System.getenv("FUNCTIONAL_TEST_NUMBER"), personalisation);
        assertNotificationSmsResponse(response, uniqueName);
        return response;
    }

    private void assertNotificationSmsResponse(final SendSmsResponse response, final String uniqueName){
        assertNotNull(response);
        assertTrue(response.getBody().contains(uniqueName));
        assertNotNull(response.getNotificationId());
        assertNotNull(response.getTemplateVersion());
        assertNotNull(response.getTemplateUri());
        assertNotNull(response.getTemplateUri());
        assertNotNull(response.getTemplateVersion());
        assertNotNull(response.getTemplateId());
    }

    private void assertNotificationEmailResponse(final SendEmailResponse response, final String uniqueName){
        assertNotNull(response);
        assertTrue(response.getBody().contains(uniqueName));
        assertNotNull(response.getNotificationId());
        assertNotNull(response.getTemplateVersion());
        assertNotNull(response.getSubject());
        assertNotNull(response.getTemplateUri());
        assertNotNull(response.getFromEmail());
        assertNotNull(response.getTemplateUri());
        assertNotNull(response.getTemplateVersion());
        assertNotNull(response.getTemplateId());
    }


    private Notification assertNotification(Notification notification) throws NotificationClientException {
        assertNotNull(notification);
        assertNotNull(notification.getId());
        assertNotNull(notification.getTemplateId());
        assertNotNull(notification.getTemplateVersion());
        assertNotNull(notification.getCreatedAt());
        assertNotNull(notification.getStatus());
        assertNotNull(notification.getNotificationType());
        if(notification.getNotificationType().equals("sms")){
            assertNotNull(notification.getPhoneNumber());
            assertNull(notification.getEmailAddress());
            assertNull(notification.getLine1());
            assertNull(notification.getLine2());
            assertNull(notification.getLine3());
            assertNull(notification.getLine4());
            assertNull(notification.getLine5());
            assertNull(notification.getLine6());
            assertNull(notification.getPostcode());
        }
        if(notification.getNotificationType().equals("email")){
            assertNotNull(notification.getEmailAddress());
            assertNull(notification.getPhoneNumber());
            assertNull(notification.getLine1());
            assertNull(notification.getLine2());
            assertNull(notification.getLine3());
            assertNull(notification.getLine4());
            assertNull(notification.getLine5());
            assertNull(notification.getLine6());
            assertNull(notification.getPostcode());
        }
        if(notification.getNotificationType().equals("letter")){
            assertNotNull(notification.getLine1());
            // the other address lines are optional.
            assertNull(notification.getEmailAddress());
            assertNull(notification.getPhoneNumber());
        }

        assertTrue("expected status to be created, sending or delivered", Arrays.asList("created", "sending", "delivered").contains(notification.getStatus()));

        return notification;
    }

}
