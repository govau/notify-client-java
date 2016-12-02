package uk.gov.service.notify;

import org.json.JSONObject;

import java.util.Optional;
import java.util.UUID;

public class SendEmailResponse {
    private UUID notificationId;
    private Optional<String> reference;
    private UUID templateId;
    private int templateVersion;
    private String templateUri;
    private String body;
    private String subject;
    private Optional<String> fromEmail;


    public SendEmailResponse(String response) {
        JSONObject data = new JSONObject(response);
        notificationId = UUID.fromString(data.getString("id"));
        reference = !data.isNull("reference") ? Optional.of(data.getString("reference")) : Optional.empty();
        JSONObject content = data.getJSONObject("content");
        body = content.getString("body");
        fromEmail = !content.isNull("from_email") ? Optional.of(content.getString("from_email")) : Optional.empty();
        subject = content.getString("subject");
        JSONObject template = data.getJSONObject("template");
        templateId = UUID.fromString(template.getString("id"));
        templateVersion = template.getInt("version");
        templateUri = template.getString("uri");
    }

    public UUID getNotificationId() {
        return notificationId;
    }

    public Optional<String> getReference() {
        return reference;
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

    public Optional<String> getFromEmail() {
        return fromEmail;
    }

    @Override
    public String toString() {
        return "SendEmailResponse{" +
                "notificationId=" + notificationId +
                ", reference=" + reference +
                ", templateId=" + templateId +
                ", templateVersion=" + templateVersion +
                ", templateUri='" + templateUri + '\'' +
                ", body='" + body + '\'' +
                ", subject='" + subject + '\'' +
                ", fromEmail=" + fromEmail +
                '}';
    }
}
