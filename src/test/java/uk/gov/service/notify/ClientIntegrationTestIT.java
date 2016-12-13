package uk.gov.service.notify;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ClientIntegrationTestIT {

    @Test
    public void testEmailNotificationIT() throws NotificationClientException, InterruptedException {
        NotificationClient client = getClient();
        SendEmailResponse emailResponse = sendEmailAndAssertResponse(client);
        Notification notification = client.getNotificationById(emailResponse.getNotificationId().toString());
        assertNotification(notification);
    }

    @Test
    public void testSmsNotificationIT() throws NotificationClientException, InterruptedException {
        NotificationClient client = getClient();
        SendSmsResponse response = sendSmsAndAssertResponse(client);
        Notification notification = client.getNotificationById(response.getNotificationId().toString());
        assertNotification(notification);
    }

    @Test
    public void testGetAllNotifications() throws NotificationClientException {
        NotificationClient client = getClient();
        NotificationList notificationList = client.getNotifications(null, null, null);
        assertNotNull(notificationList);
        assertNotNull(notificationList.getNotifications());
        assertFalse(notificationList.getNotifications().isEmpty());
        // Just check the first notification in the list.
        assertNotification(notificationList.getNotifications().get(0));
        String baseUrl = System.getenv("NOTIFY_API_URL");
        assertEquals(baseUrl + "/v2/notifications", notificationList.getCurrentPageLink());

    }

    @Test
    public void testEmailNotificationWithoutPersonalisationReturnsErrorMessageIT() {
        NotificationClient client = getClient();
        try {
            client.sendEmail(System.getenv("EMAIL_TEMPLATE_ID"), System.getenv("FUNCTIONAL_TEST_EMAIL"), null, null);
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
            client.sendSms(System.getenv("SMS_TEMPLATE_ID"), System.getenv("FUNCTIONAL_TEST_NUMBER"), null, null);
            fail("Expected NotificationClientException: Template missing personalisation: name");
        } catch (NotificationClientException e) {
            assert(e.getMessage().contains("Template missing personalisation: name"));
            assert(e.getMessage().contains("Status code: 400"));
        }
    }

    private NotificationClient getClient(){
        String apiKey = System.getenv("API_KEY");
        String baseUrl = System.getenv("NOTIFY_API_URL");
        return new NotificationClient(apiKey, baseUrl);
    }

    private SendEmailResponse sendEmailAndAssertResponse(final NotificationClient client) throws NotificationClientException {
        HashMap<String, String> personalisation = new HashMap<>();
        String uniqueName = UUID.randomUUID().toString();
        personalisation.put("name", uniqueName);
        SendEmailResponse response = client.sendEmail(System.getenv("EMAIL_TEMPLATE_ID"),
                System.getenv("FUNCTIONAL_TEST_EMAIL"), personalisation, uniqueName);
        assertNotificationEmailResponse(response, uniqueName);
        return response;
    }

    private SendSmsResponse sendSmsAndAssertResponse(final NotificationClient client) throws NotificationClientException {
        HashMap<String, String> personalisation = new HashMap<>();
        String uniqueName = UUID.randomUUID().toString();
        personalisation.put("name", uniqueName);
        SendSmsResponse response = client.sendSms(System.getenv("SMS_TEMPLATE_ID"), System.getenv("FUNCTIONAL_TEST_NUMBER"), personalisation, uniqueName);
        assertNotificationSmsResponse(response, uniqueName);
        return response;
    }

    private void assertNotificationSmsResponse(final SendSmsResponse response, final String uniqueName){
        assertNotNull(response);
        assertTrue(response.getBody().contains(uniqueName));
        assertEquals(Optional.of(uniqueName), response.getReference());
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
        assertEquals(Optional.of(uniqueName), response.getReference());
        assertNotNull(response.getNotificationId());
        assertNotNull(response.getTemplateVersion());
        assertNotNull(response.getSubject());
        assertNotNull(response.getTemplateUri());
        assertNotNull(response.getFromEmail());
        assertNotNull(response.getTemplateUri());
        assertNotNull(response.getTemplateVersion());
        assertNotNull(response.getTemplateId());
    }
    private Notification assertNotification(Notification notification){
        assertNotNull(notification);
        assertNotNull(notification.getId());
        assertNotNull(notification.getTemplateId());
        assertNotNull(notification.getTemplateVersion());
        assertNotNull(notification.getTemplateUri());
        assertNotNull(notification.getCreatedAt());
        assertNotNull(notification.getStatus());
        assertNotNull(notification.getNotificationType());
        if(notification.getNotificationType().equals("sms")){
            assertTrue(notification.getPhoneNumber().isPresent());
            assertEquals(Optional.empty(), notification.getEmailAddress());
            assertEquals(Optional.empty(), notification.getLine1());
            assertEquals(Optional.empty(), notification.getLine2());
            assertEquals(Optional.empty(), notification.getLine3());
            assertEquals(Optional.empty(), notification.getLine4());
            assertEquals(Optional.empty(), notification.getLine5());
            assertEquals(Optional.empty(), notification.getLine6());
            assertEquals(Optional.empty(), notification.getPostcode());
        }
        if(notification.getNotificationType().equals("email")){
            assertTrue(notification.getEmailAddress().isPresent());
            assertEquals(Optional.empty(), notification.getPhoneNumber());
            assertEquals(Optional.empty(), notification.getLine1());
            assertEquals(Optional.empty(), notification.getLine2());
            assertEquals(Optional.empty(), notification.getLine3());
            assertEquals(Optional.empty(), notification.getLine4());
            assertEquals(Optional.empty(), notification.getLine5());
            assertEquals(Optional.empty(), notification.getLine6());
            assertEquals(Optional.empty(), notification.getPostcode());
        }
        if(notification.getNotificationType().equals("letter")){
            assertTrue(notification.getLine1().isPresent());
            // the other address lines are optional.
            assertEquals(Optional.empty(), notification.getEmailAddress());
            assertEquals(Optional.empty(), notification.getPhoneNumber());
        }

        assertTrue("expected status to be created, sending or delivered", Arrays.asList("created", "sending", "delivered").contains(notification.getStatus()));

        return notification;
    }

}
