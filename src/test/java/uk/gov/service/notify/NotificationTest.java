package uk.gov.service.notify;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NotificationTest {

    @Test
    public void testNotification_canCreateObjectFromJson(){
        String content = "{\n" +
                "  \"data\": {\n" +
                "    \"notification\": {\n" +
                "      \"api_key\": \"some api key\",\n" +
                "      \"body\": \"Hello hello\",\n" +
                "      \"content_char_count\": 11,\n" +
                "      \"created_at\": \"2016-07-15T15:16:21.675607+00:00\",\n" +
                "      \"id\": \"some id\",\n" +
                "      \"job\": null,\n" +
                "      \"job_row_number\": null,\n" +
                "      \"notification_type\": \"email\",\n" +
                "      \"reference\": \"some reference\",\n" +
                "      \"sent_at\": \"2016-07-15T15:16:23.057513+00:00\",\n" +
                "      \"sent_by\": \"theProvider\",\n" +
                "      \"service\": \"service id\",\n" +
                "      \"status\": \"delivered\",\n" +
                "      \"subject\": \"Version 2\",\n" +
                "      \"template\": {\n" +
                "        \"id\": \"some template id\",\n" +
                "        \"name\": \"Testing template\",\n" +
                "        \"template_type\": \"email\"\n" +
                "      },\n" +
                "      \"template_version\": 2,\n" +
                "      \"to\": \"testing@digital.cabinet-office.gov.uk\",\n" +
                "      \"updated_at\": \"2016-07-15T15:16:23.729177+00:00\"\n" +
                "    }\n" +
                "  }\n" +
                "}\n";

        Notification notification = new Notification(content);
        assertEquals("some id", notification.getId());
        assertEquals("Hello hello", notification.getBody());
        assertEquals(11, notification.getContentCharCount());
        assertEquals("Version 2", notification.getSubject());
        assertEquals("some reference", notification.getReference());
        assertEquals("some template id", notification.getTemplateId());
        assertEquals("some api key", notification.getApiKey());
        assertEquals(new DateTime("2016-07-15T15:16:21.675607+00:00"), notification.getCreatedAt());
        assertEquals(new DateTime("2016-07-15T15:16:23.057513+00:00"), notification.getSentAt());
        assertEquals(new DateTime("2016-07-15T15:16:23.729177+00:00"), notification.getUpdatedAt());
        assertEquals("testing@digital.cabinet-office.gov.uk", notification.getTo());
        assertEquals("Testing template", notification.getTemplateName());
        assertEquals(2, notification.getTemplateVersion());
        assertNull(notification.getJobId());
        assertNull(notification.getJobFileName());
        assertEquals(0, notification.getJobRowNumber());
        assertEquals("delivered", notification.getStatus());
        assertEquals("theProvider", notification.getSentBy());
        assertEquals("email", notification.getNotificationType());

    }

    @Test
    public void testNotification_canCreateObjectWithJob(){

        String data = "{\n" +
                "  \"data\": {\n" +
                "    \"notification\": {\n" +
                "      \"api_key\": null,\n" +
                "      \"body\": \"Does this work\",\n" +
                "      \"content_char_count\": null,\n" +
                "      \"created_at\": \"2016-07-18T10:05:29.666050+00:00\",\n" +
                "      \"id\": \"b4f2678f-2c52-4d81-8055-85f75e2e0e95\",\n" +
                "      \"job\": {\n" +
                "        \"id\": \"somejobid\",\n" +
                "        \"original_file_name\": \"test_email.csv\"\n" +
                "      },\n" +
                "      \"job_row_number\": 8,\n" +
                "      \"notification_type\": \"email\",\n" +
                "      \"reference\": \"01020155fd77c29c-13952ead-3c6a-486f-91b7-01ce1d20d2cd-000000\",\n" +
                "      \"sent_at\": \"2016-07-18T10:05:32.350296+00:00\",\n" +
                "      \"sent_by\": \"ses\",\n" +
                "      \"service\": \"1339fcc3-7a4e-4661-aa0e-188de366bd92\",\n" +
                "      \"status\": \"delivered\",\n" +
                "      \"subject\": \"Should work\",\n" +
                "      \"template\": {\n" +
                "        \"id\": \"321abb8e-e5e2-4a48-b9e4-bf54eaf708dd\",\n" +
                "        \"name\": \"New template\",\n" +
                "        \"template_type\": \"email\"\n" +
                "      },\n" +
                "      \"template_version\": 3,\n" +
                "      \"to\": \"someone@digital.cabinet-office.gov.uk\",\n" +
                "      \"updated_at\": \"2016-07-18T10:05:32.905219+00:00\"\n" +
                "    }\n" +
                "  }\n" +
                "}\n";

        Notification notification = new Notification(data);
        assertEquals(8, notification.getJobRowNumber());
        assertEquals("somejobid", notification.getJobId());
        assertEquals("test_email.csv", notification.getJobFileName());

    }
}