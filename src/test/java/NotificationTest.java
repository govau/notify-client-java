import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.*;

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
        assertEquals("some reference", notification.getReference());
        assertEquals("some template id", notification.getTemplateId());
        assertEquals("some api key", notification.getApiKey());
        assertEquals(new DateTime("2016-07-15T15:16:21.675607+00:00"), notification.getCreatedAt());
        assertEquals(new DateTime("2016-07-15T15:16:23.057513+00:00"), notification.getSentAt());
        assertEquals(new DateTime("2016-07-15T15:16:23.729177+00:00"), notification.getUpdatedAt());
        assertEquals("testing@digital.cabinet-office.gov.uk", notification.getTo());
        assertEquals("Testing template", notification.getTemplateName());
        assertEquals(2, notification.getTemplateVersion());
        assertNull(notification.getJob());
        assertEquals(0, notification.getJobRowNumber());
        assertEquals("delivered", notification.getStatus());
        assertEquals("theProvider", notification.getSentBy());
        assertEquals("email", notification.getNotificationType());

    }
}