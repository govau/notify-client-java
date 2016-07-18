import org.joda.time.DateTime;
import org.json.JSONObject;

public class Notification {
    private String id;
    private String body;
    private int contentCharCount;
    private String notificationType;
    // Reference from email provider, will be null for sms
    private String reference;
    private String subject;
    private String templateId;
    private String templateName;
    private int templateVersion;
    private String to;
    private String sentBy;
    private DateTime sentAt;
    private String status;
    private DateTime createdAt;
    private DateTime updatedAt;
    private String apiKey;
    private String jobId;
    private String jobFileName;
    private int jobRowNumber;

    public Notification(String content){
        org.json.JSONObject responseBodyAsJson = new org.json.JSONObject(content);
        org.json.JSONObject data = responseBodyAsJson.getJSONObject("data").getJSONObject("notification");
        build(data);

    }

    public Notification(org.json.JSONObject data){
        build(data);

    }

    private void build(JSONObject data) {
        id = data.getString("id");
        body = data.getString("body");
        contentCharCount = data.isNull("content_char_count") ? 0 : data.getInt("content_char_count");
        notificationType = data.getString("notification_type");
        reference = data.isNull("reference") ? null : data.getString("reference");
        subject = data.isNull("subject") ? null : data.getString("subject");
        JSONObject template = data.getJSONObject("template");
        templateId = template.getString("id");
        templateName = template.getString("name");
        templateVersion = data.getInt("template_version");
        to = data.getString("to");
        sentBy = data.getString("sent_by");
        sentAt = new DateTime(data.getString("sent_at"));
        status = data.getString("status");
        createdAt = new DateTime(data.getString("created_at"));
        updatedAt = data.isNull("updated_at") ? null :  new DateTime(data.getString("updated_at"));
        apiKey = data.isNull("api_key") ? null : data.getString("api_key");
        org.json.JSONObject job = data.isNull("job") ? null : data.getJSONObject("job");
        if(job == null){
            jobId = null;
            jobFileName = null;
        }
        else {
            jobId = job.isNull("id") ? null : job.getString("id");
            jobFileName = job.isNull("original_file_name") ? null : job.getString("original_file_name");
        }
        jobRowNumber = data.isNull("job_row_number") ? 0 : data.getInt("job_row_number");
    }

    public String getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public int getContentCharCount() {
        return contentCharCount;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public String getReference() {
        return reference;
    }

    public String getSubject() {
        return subject;
    }

    public String getTemplateId() {
        return templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public int getTemplateVersion() {
        return templateVersion;
    }

    public String getTo() {
        return to;
    }

    public String getSentBy() {
        return sentBy;
    }

    public DateTime getSentAt() {
        return sentAt;
    }

    public String getStatus() {
        return status;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public DateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getJobId() {
        return jobId;
    }

    public String getJobFileName() {
        return jobFileName;
    }

    public int getJobRowNumber() {
        return jobRowNumber;
    }
}
