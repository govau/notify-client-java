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

    @Test
    public void testNotificationResponseForPrecompiledLetterResponse(){
        String precompiledPdfResponse = "{\n" +
                "  \"content\": {\n" +
                "    \"body\": null, \n" +
                "    \"subject\": \"Pre-compiled PDF\"\n" +
                "  }, \n" +
                "  \"id\": \"08c286fc-891b-46e3-8aad-727be53ba5d8\", \n" +
                "  \"reference\": \"b294078f-0905-443c-820d-49721d76346c\", \n" +
                "  \"scheduled_for\": null, \n" +
                "  \"template\": {\n" +
                "    \"id\": \"47621d11-56cc-4767-a785-31abfda4e12b\", \n" +
                "    \"uri\": \"https://api.notify.works/services/70eac486-bf44-469c-8943-c2a13d670572/templates/47621d11-56cc-4767-a785-31abfda4e12b\", \n" +
                "    \"version\": 1\n" +
                "  }, \n" +
                "  \"uri\": \"https://api.notify.works/v2/notifications/08c286fc-891b-46e3-8aad-727be53ba5d8\"\n" +
                "}";

        SendLetterResponse response = new SendLetterResponse(precompiledPdfResponse);
        assertEquals("08c286fc-891b-46e3-8aad-727be53ba5d8", response.getNotificationId().toString());
        assertEquals(Optional.of("b294078f-0905-443c-820d-49721d76346c"), response.getReference());
        assertEquals("47621d11-56cc-4767-a785-31abfda4e12b", response.getTemplateId().toString());
        assertEquals("https://api.notify.works/services/70eac486-bf44-469c-8943-c2a13d670572/templates/47621d11-56cc-4767-a785-31abfda4e12b", response.getTemplateUri());
        assertEquals(1, response.getTemplateVersion());
        assertEquals(null, response.getBody());
        assertEquals("Pre-compiled PDF", response.getSubject());
    }
}
