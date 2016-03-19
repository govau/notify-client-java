package uk.gov.notifications.client.model;

import org.apache.commons.validator.routines.EmailValidator;

/**
 * Represents an email request.
 */
public class EmailRequest extends GovNotifyRequest {

    private String email;
    private String templateId;
    private Personalisation personalisation;

    private EmailRequest() {
    }

    /**
     * Returns the email address.
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the template id.
     *
     * @return template id
     */
    public String getTemplateId() {
        return templateId;
    }

    /**
     * Returns personalisation data.
     *
     * @return Personalisation
     */
    public Personalisation getPersonalisation() {
        return personalisation;
    }

    /**
     * Returns builder object for this request.
     * @return builder object
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Allows for safe and fluent creation of EmailRequest object.
     */
    public static class Builder {

        private EmailRequest request = new EmailRequest();

        /**
         * Mandatory. Sets the email address to which the message is directed
         *
         * @param email recipient email
         * @return builder object
         */
        public Builder email(String email) {
            request.email = email;
            return this;
        }

        /**
         * Mandatory. Sets the id of the template to create the message
         *
         * @param templateId id of the template
         * @return builder object
         */
        public Builder templateId(String templateId) {
            request.templateId = templateId;
            return this;
        }

        /**
         * Optional. Sets the data holder for name-value pairs to personalise templates
         *
         * @param personalisation personalisation data
         * @return builder object
         */
        public Builder personalisation(Personalisation personalisation) {
            request.personalisation = personalisation;
            return this;
        }

        /**
         * Builds an email request.
         *
         * @return EmailRequest
         */
        public EmailRequest build() {
            validate();
            return request;
        }

        private void validate() {

            if (!EmailValidator.getInstance().isValid(request.email)) {
                throw new IllegalArgumentException("Email address must be valid");
            }

            if (request.templateId == null || request.templateId.isEmpty()) {
                throw new IllegalArgumentException("Template id cannot be empty");
            }
        }
    }
}
