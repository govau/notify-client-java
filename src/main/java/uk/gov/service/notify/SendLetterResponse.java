package uk.gov.service.notify;

import org.json.JSONObject;

import java.util.Optional;
import java.util.UUID;

public class SendLetterResponse extends LetterResponse {
    private UUID templateId;
    private int templateVersion;
    private String templateUri;
    private String body;
    private String subject;
    private JSONObject content;
    private JSONObject template;


    public SendLetterResponse(String response) {
        super(response);
        content = getData().getJSONObject("content");
        body = tryToGetString(content, "body");
        subject = tryToGetString(content, "subject");
        template = getData().getJSONObject("template");
        templateId = UUID.fromString(template.getString("id"));
        templateVersion = template.getInt("version");
        templateUri = template.getString("uri");
    }

    public UUID getTemplateId() {
        return templateId;
    }

    public int getTemplateVersion() {
        return templateVersion;
    }

    public String getTemplateUri() {
        return templateUri;
    }

    public String getBody() {
        return body;
    }

    public String getSubject() {
        return subject;
    }

    @Override
    public String toString() {
        return "SendLetterResponse{" +
                "notificationId=" + getNotificationId() +
                ", reference=" + getReference() +
                ", templateId=" + templateId +
                ", templateVersion=" + templateVersion +
                ", templateUri='" + templateUri + '\'' +
                ", body='" + body + '\'' +
                ", subject='" + subject +
                '}';
    }
}
