package au.gov.notify.dtos;

import org.json.JSONObject;

import java.util.Optional;
import java.util.UUID;

public class TemplatePreview {
    private UUID id;
    private String templateType;
    private int version;
    private String body;
    private String subject;


    public TemplatePreview(String content) {
        JSONObject responseBodyAsJson = new JSONObject(content);
        build(responseBodyAsJson);
    }

    public TemplatePreview(org.json.JSONObject data) {
        build(data);
    }

    private void build(JSONObject data) {
        id = UUID.fromString(data.getString("id"));
        templateType = data.getString("type");
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
                ", version=" + version +
                ", body='" + body + '\'' +
                ", subject='" + subject + '\'' +
                '}';
    }
}
