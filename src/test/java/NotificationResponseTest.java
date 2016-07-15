import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NotificationResponseTest {
    @Test
    public void testNotificationResponse(){
        String jsonResponse = "{\n" +
                "  \"data\": {\n" +
                "    \"body\": \"Hello there\",\n" +
                "    \"notification\": {\n" +
                "      \"id\": \"some id\"\n" +
                "    },\n" +
                "    \"template_version\": 1\n" +
                "  }\n" +
                "}";

        NotificationResponse response = new NotificationResponse(jsonResponse);
        assertEquals("Hello there", response.getBody());
        assertEquals("some id", response.getNotificationId());
        assertEquals(1, response.getTemplateVersion());
    }
}