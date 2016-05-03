package uk.gov.notifications.client.model;

import org.joda.time.DateTime;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Represents status information of the already requested notifications.
 */
public class StatusResponse implements Serializable {

    private String id;
    private String status;
    private int contentCharCount;
    private DateTime createdAt;
    private DateTime sentAt;
    private String sentBy;
    private String templateId;
    private String templateName;
    private String templateType;
    private String to;
    private DateTime updatedAt;

    private StatusResponse() {
    }

    /**
     * Returns an id of the notification.
     * @return notification id
     */
    public String getId() {
        return id;
    }

    /**
     * Returns a status of the notification request.
     * @return status of the notification request.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Returns character count for the content of the sent message
     * @return contentCharCount
     */
    public int getContentCharCount() {
        return contentCharCount;
    }

    /**
     * Return date time the notification was created at
     * @return createdAt
     */
    public DateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Return the date time the notification was sent at
     * @return sentAt
     */
    public DateTime getSentAt() {
        return sentAt;
    }

    /**
     * Returns name of the provider that sent the message
     * @return sentBy
     */
    public String getSentBy() {
        return sentBy;
    }

    /**
     * Return id of the template
      * @return templateId
     */
    public String getTemplateId() {
        return templateId;
    }

    /**
     * Return name of the template
     * @return templateName
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * Return type of the template, sms | email
     * @return templateType
     */
    public String getTemplateType() {
        return templateType;
    }

    /**
     * Return the phone number or email address the message was sent to
     * @return to
     */
    public String getTo() {
        return to;
    }

    /**
     * Return date time the notification was updated
     * @return updatedAt
     */
    public DateTime getUpdatedAt() {
        return updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Allows for safe and fluent creation of StatusResponse object.
     */
    public static class Builder {

        private StatusResponse response = new StatusResponse();

        public Builder id(String id) {
            response.id = id;
            return this;
        }

        public Builder status(String status) {
            response.status = status;
            return this;
        }

        public Builder notification(JSONObject notification){
            response.id = notification.getString("id");
            if(!notification.isNull("content_char_count")){ response.contentCharCount = notification.getInt("content_char_count"); }
            response.createdAt = new DateTime(notification.getString("created_at"));
            if(!notification.isNull("sent_at")) { response.sentAt = new DateTime(notification.getString("sent_at")); }
            if(!notification.isNull("updated_at")) { response.updatedAt = new DateTime(notification.getString("updated_at")); }
            response.templateId = notification.getJSONObject("template").getString("id");
            response.templateName = notification.getJSONObject("template").getString("name");
            response.templateType = notification.getJSONObject("template").getString("template_type");
            response.to = notification.getString("to");
            if(!notification.isNull("sent_by")) { response.sentBy = notification.getString("sent_by"); }
            response.status = notification.getString("status");

            return this;
        }
        public StatusResponse build() {
            return response;
        }

        @Override
        public String toString(){
           return "id: " +  response.id + "\n" +
                   "status: " + response.status + "\n" +
                   "sent at: " + response.sentAt + "\n" +
                   "sent by: " + response.sentBy + "\n" +
                   "sent to: " + response.to + "\n" +
                   "created at: " + response.createdAt + "\n" +
                   "updated at: " + response.updatedAt + "\n" +
                   "content character count: " + response.contentCharCount + "\n" +
                   "template id: : " + response.templateId + "\n" +
                   "template name: : " + response.templateName + "\n" +
                   "template type: : " + response.templateType;
        }
    }
}


