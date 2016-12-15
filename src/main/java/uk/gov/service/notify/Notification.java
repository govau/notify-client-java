package uk.gov.service.notify;

import org.joda.time.DateTime;
import org.json.JSONObject;

import java.util.Optional;
import java.util.UUID;

public class Notification {
    private UUID id;
    private Optional<String> reference;
    private Optional<String> emailAddress;
    private Optional<String> phoneNumber;
    private Optional<String> line1;
    private Optional<String> line2;
    private Optional<String> line3;
    private Optional<String> line4;
    private Optional<String> line5;
    private Optional<String> line6;
    private Optional<String> postcode;
    private String notificationType;
    private String status;
    private UUID templateId;
    private int templateVersion;
    private String templateUri;
    private String body;
    private Optional<String> subject;
    private DateTime createdAt;
    private Optional<DateTime> sentAt;
    private Optional<DateTime> completedAt;

    public Notification(String content){
        JSONObject responseBodyAsJson = new JSONObject(content);
        build(responseBodyAsJson);

    }

    public Notification(org.json.JSONObject data){
        build(data);

    }

    private void build(JSONObject data) {
        id = UUID.fromString(data.getString("id"));
        reference = data.isNull("reference") ? Optional.empty() : Optional.of(data.getString("reference"));
        emailAddress = data.isNull("email_address") ? Optional.empty() : Optional.of(data.getString("email_address"));
        phoneNumber = data.isNull("phone_number") ? Optional.empty() : Optional.of(data.getString("phone_number"));
        line1 = data.isNull("line_1") ? Optional.empty() : Optional.of(data.getString("line_1"));
        line2 = data.isNull("line_2") ? Optional.empty() : Optional.of(data.getString("line_2"));
        line3 = data.isNull("line_3") ? Optional.empty() : Optional.of(data.getString("line_3"));
        line4 = data.isNull("line_4") ? Optional.empty() : Optional.of(data.getString("line_4"));
        line5 = data.isNull("line_5") ? Optional.empty() : Optional.of(data.getString("line_5"));
        line6 = data.isNull("line_6") ? Optional.empty() : Optional.of(data.getString("line_6"));
        postcode = data.isNull("postcode") ? Optional.empty() : Optional.of(data.getString("postcode"));
        notificationType = data.getString("type");
        JSONObject template = data.getJSONObject("template");
        templateId = UUID.fromString(template.getString("id"));
        templateVersion = template.getInt("version");
        templateUri = template.getString("uri");
        body = data.getString("body");
        subject = data.isNull("subject") ? Optional.empty() : Optional.of(data.getString("subject"));
        status = data.getString("status");
        createdAt = new DateTime(data.getString("created_at"));
        sentAt =  data.isNull("sent_at") ? Optional.empty() : Optional.of(new DateTime(data.getString("sent_at")));
        completedAt = data.isNull("completed_at") ? Optional.empty() : Optional.of(new DateTime(data.getString("completed_at")));
    }

    public UUID getId() {
        return id;
    }

    public Optional<String> getReference() {
        return reference;
    }

    public Optional<String> getEmailAddress() {
        return emailAddress;
    }

    public Optional<String> getPhoneNumber() {
        return phoneNumber;
    }

    public Optional<String> getLine1() {
        return line1;
    }

    public Optional<String> getLine2() {
        return line2;
    }

    public Optional<String> getLine3() {
        return line3;
    }

    public Optional<String> getLine4() {
        return line4;
    }

    public Optional<String> getLine5() {
        return line5;
    }

    public Optional<String> getLine6() {
        return line6;
    }

    public Optional<String> getPostcode() {
        return postcode;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public String getStatus() {
        return status;
    }

    public UUID getTemplateId() {
        return templateId;
    }

    public int getTemplateVersion() {
        return templateVersion;
    }

    public String getTemplateUri(){
        return templateUri;
    }

    public String getBody() {
        return body;
    }

    public Optional<String> getSubject() {
        return subject;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public Optional<DateTime> getSentAt() {
        return sentAt;
    }

    public Optional<DateTime> getCompletedAt() {
        return completedAt;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", reference=" + reference +
                ", emailAddress=" + emailAddress +
                ", phoneNumber=" + phoneNumber +
                ", line1=" + line1 +
                ", line2=" + line2 +
                ", line3=" + line3 +
                ", line4=" + line4 +
                ", line5=" + line5 +
                ", line6=" + line6 +
                ", postcode=" + postcode +
                ", notificationType='" + notificationType + '\'' +
                ", status='" + status + '\'' +
                ", templateId='" + templateId + '\'' +
                ", templateVersion=" + templateVersion +
                ", templateUri=" + templateUri +
                ", body=" + body +
                ", subject=" + subject +
                ", createdAt=" + createdAt +
                ", sentAt=" + sentAt +
                ", completedAt=" + completedAt +
                '}';
    }
}
