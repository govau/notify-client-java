package au.gov.notify.dtos;

import org.json.JSONObject;

import java.util.Optional;
import java.util.UUID;

public class SendSmsResponse {
    private UUID notificationId;
    private String reference;
    private UUID templateId;
    private int templateVersion;
    private String templateUri;
    private String body;
    private String fromNumber;


    public SendSmsResponse(String response) {
        JSONObject data = new JSONObject(response);
        notificationId = UUID.fromString(data.getString("id"));
        reference = data.isNull("reference") ? null : data.getString("reference");
        JSONObject content = data.getJSONObject("content");
        body = content.getString("body");
        fromNumber = content.isNull("from_number") ? null : content.getString("from_number");
        JSONObject template = data.getJSONObject("template");
        templateId = UUID.fromString(template.getString("id"));
        templateVersion = template.getInt("version");
        templateUri = template.getString("uri");
    }

    public UUID getNotificationId() {
        return notificationId;
    }

    public Optional<String> getReference() {
        return Optional.ofNullable(reference);
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

    public Optional<String> getFromNumber() {
        return Optional.ofNullable(fromNumber);
    }

    @Override
    public String toString() {
        return "SendSmsResponse{" +
                "notificationId=" + notificationId +
                ", reference=" + reference +
                ", templateId=" + templateId +
                ", templateVersion=" + templateVersion +
                ", templateUri='" + templateUri + '\'' +
                ", body='" + body + '\'' +
                ", fromNumber=" + fromNumber +
                '}';
    }
}

