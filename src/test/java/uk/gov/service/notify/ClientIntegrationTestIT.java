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
        NotificationResponse emailResponse = sendEmail(client);
        Notification notification = client.getNotificationById(emailResponse.getNotificationId());
        assertNotification(notification);
    }

    @Test
    public void testSmsNotificationIT() throws NotificationClientException, InterruptedException {
        NotificationClient client = getClient();
        NotificationResponse response = sendSms(client);
        Notification notification = client.getNotificationById(response.getNotificationId());
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
            fail("Expected NotificationClientException: Missing personalisation: name");
        } catch (NotificationClientException e) {
            assert(e.getMessage().contains("Missing personalisation: name"));
            assert(e.getMessage().contains("Status code: 400"));
        }
    }

    @Test
    public void testSmsNotificationWithoutPersonalisationReturnsErrorMessageIT() {
        NotificationClient client = getClient();
        try {
            client.sendSms(System.getenv("SMS_TEMPLATE_ID"), System.getenv("FUNCTIONAL_TEST_NUMBER"));
            fail("Expected NotificationClientException: Missing personalisation: name");
        } catch (NotificationClientException e) {
            assert(e.getMessage().contains("Missing personalisation: name"));
            assert(e.getMessage().contains("Status code: 400"));
        }
    }

    private NotificationClient getClient(){
        String serviceId = System.getenv("SERVICE_ID");
        String apiKey = System.getenv("API_KEY");
        String baseUrl = System.getenv("NOTIFY_API_URL");
        NotificationClient client = new NotificationClient(apiKey, baseUrl);
        return client;
    }

    private NotificationResponse sendEmail(final NotificationClient client) throws NotificationClientException {
        HashMap<String, String> personalisation = new HashMap<>();
        String uniqueName = UUID.randomUUID().toString();
        personalisation.put("name", uniqueName);
        NotificationResponse response = client.sendEmail(System.getenv("EMAIL_TEMPLATE_ID"),
                System.getenv("FUNCTIONAL_TEST_EMAIL"), personalisation);
        assertNotificationResponse(response, uniqueName);
        return response;
    }

    private NotificationResponse sendSms(final NotificationClient client) throws NotificationClientException {
        HashMap<String, String> personalisation = new HashMap<>();
        String uniqueName = UUID.randomUUID().toString();
        personalisation.put("name", uniqueName);
        NotificationResponse response = client.sendSms(System.getenv("SMS_TEMPLATE_ID"), System.getenv("FUNCTIONAL_TEST_NUMBER"), personalisation);
        assertNotificationResponse(response, uniqueName);
        return response;
    }

    private void assertNotificationResponse(final NotificationResponse response, final String uniqueName){
        assertNotNull(response);
        assertTrue(response.getBody().contains(uniqueName));
        assertNotNull(response.getNotificationId());
        assertNotNull(response.getTemplateVersion());
    }


    private Notification assertNotification(Notification notification) throws NotificationClientException {
        assertNotNull(notification);
        assertNotNull(notification.getId());
        assertNotNull(notification.getBody());
        assertTrue(Arrays.asList("email", "sms").contains(notification.getNotificationType()));
        assertNotNull(notification.getStatus());
        if(notification.getNotificationType().equals("email")){
            assertNotNull(notification.getSubject());
            if(notification.getStatus().equals("created")){
                assertNull(notification.getReference());
            }else{
                assertNotNull(notification.getReference());
            }
            assertEquals(0, notification.getContentCharCount());
        }
        else{
            assertNull(notification.getSubject());
            assertNull(notification.getReference());
            assertNotEquals(0, notification.getContentCharCount());
        }
        assertNotNull(notification.getTemplateId());
        assertNotNull(notification.getTemplateName());
        assertNotNull(notification.getTemplateVersion());
        assertNotNull(notification.getTo());
        if(notification.getStatus().equals("created")) {
            assertNull(notification.getSentBy());
            assertNull(notification.getSentAt());
        }
        else{
            assertNotNull(notification.getSentBy());
            assertNotNull(notification.getSentAt());
        }
        assertTrue("expected status to be created, sending or delivered", Arrays.asList("created", "sending", "delivered").contains(notification.getStatus()));
        assertNotNull(notification.getApiKey());
        assertNull(notification.getJobId());
        assertNull(notification.getJobFileName());
        assertEquals(0, notification.getJobRowNumber());

        return notification;
    }

}
