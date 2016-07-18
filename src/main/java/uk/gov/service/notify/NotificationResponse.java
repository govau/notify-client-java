package uk.gov.service.notify;

import org.json.JSONObject;

public class NotificationResponse {
    private String body;
    private String notificationId;
    private int templateVersion;

    public NotificationResponse(String content) {
        JSONObject responseBodyAsJson = new JSONObject(content);
        JSONObject data = responseBodyAsJson.getJSONObject("data");
        notificationId = data.getJSONObject("notification").getString("id");
        body = data.getString("body");
        templateVersion = data.getInt("template_version");
    }

    public String getBody() {
        return body;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public int getTemplateVersion() {
        return templateVersion;
    }
}
