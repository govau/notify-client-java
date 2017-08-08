package uk.gov.service.notify;

import org.json.JSONObject;
import org.junit.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class SendLetterResponseTest {

    @Test
    public void testNotificationResponseForLetterResponse(){
        JSONObject postLetterResponse = new JSONObject();
        UUID id = UUID.randomUUID();
        postLetterResponse.put("id", id);
        postLetterResponse.put("reference", "clientReference");
        JSONObject template = new JSONObject();
        UUID templateId = UUID.randomUUID();
        template.put("id", templateId);
        template.put("version", 1);
        template.put("uri", "https://api.notifications.service.gov.uk/templates/"+templateId);
        postLetterResponse.put("template", template);
        JSONObject content = new JSONObject();
        content.put("body", "hello Fred");
        content.put("subject", "Reminder for thing");
        postLetterResponse.put("content", content);


        SendLetterResponse response = new SendLetterResponse(postLetterResponse.toString());
        assertEquals(id, response.getNotificationId());
        assertEquals(Optional.of("clientReference"), response.getReference());
        assertEquals(templateId, response.getTemplateId());
        assertEquals("https://api.notifications.service.gov.uk/templates/"+templateId, response.getTemplateUri());
        assertEquals(1, response.getTemplateVersion());
        assertEquals("hello Fred", response.getBody());
        assertEquals("Reminder for thing", response.getSubject());
    }
}
