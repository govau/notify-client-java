package uk.gov.service.notify;

import org.joda.time.DateTime;
import org.json.JSONObject;

import java.util.Optional;
import java.util.UUID;

public class Template {
    private UUID id;
    private String templateType;
    private DateTime createdAt;
    private DateTime updatedAt;
    private String createdBy;
    private int version;
    private String body;
    private String subject;


    public Template(String content){
        JSONObject responseBodyAsJson = new JSONObject(content);
        build(responseBodyAsJson);

    }

    public Template(org.json.JSONObject data){
        build(data);

    }

    private void build(JSONObject data) {
        id = UUID.fromString(data.getString("id"));
        templateType = data.getString("type");
        createdAt = new DateTime(data.getString("created_at"));
        updatedAt = data.isNull("updated_at") ? null : new DateTime(data.getString("updated_at"));
        version = data.getInt("version");
        body = data.getString("body");
        subject = data.isNull("subject") ? null : data.getString("subject");
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(DateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Optional<DateTime> getUpdatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    public void setUpdatedAt(DateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Optional<String> getSubject() {
        return Optional.ofNullable(subject);
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "Template{" +
                "id=" + id +
                ", templateType='" + templateType + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", createdBy='" + createdBy + '\'' +
                ", version=" + version +
                ", body='" + body + '\'' +
                ", subject='" + subject + '\'' +
                '}';
    }
}
