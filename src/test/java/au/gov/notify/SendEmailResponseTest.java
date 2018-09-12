package au.gov.notify;

import au.gov.notify.dtos.SendEmailResponse;
import org.json.JSONObject;
import org.junit.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class SendEmailResponseTest {

    @Test
    public void testNotificationResponseForEmailResponse(){
        JSONObject postEmailResponse = new JSONObject();
        UUID id = UUID.randomUUID();
        postEmailResponse.put("id", id);
        postEmailResponse.put("reference", "clientReference");
        JSONObject template = new JSONObject();
        UUID templateId = UUID.randomUUID();
        template.put("id", templateId);
        template.put("version", 1);
        template.put("uri", "https://rest-api.notify.gov.au/templates/"+templateId);
        postEmailResponse.put("template", template);
        JSONObject content = new JSONObject();
        content.put("body", "hello Fred");
        content.put("from_email", "senderId");
        content.put("subject", "Reminder for thing");
        postEmailResponse.put("content", content);


        SendEmailResponse response = new SendEmailResponse(postEmailResponse.toString());
        assertEquals(id, response.getNotificationId());
        assertEquals(Optional.of("clientReference"), response.getReference());
        assertEquals(templateId, response.getTemplateId());
        assertEquals("https://rest-api.notify.gov.au/templates/"+templateId, response.getTemplateUri());
        assertEquals(1, response.getTemplateVersion());
        assertEquals("hello Fred", response.getBody());
        assertEquals("Reminder for thing", response.getSubject());
        assertEquals(Optional.of("senderId"), response.getFromEmail());
    }
}
