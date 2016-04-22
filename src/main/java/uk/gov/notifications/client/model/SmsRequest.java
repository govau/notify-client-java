package uk.gov.notifications.client.model;

/**
 * Represents an SMS request.
 */
public class SmsRequest extends GovNotifyRequest {

    private String phoneNumber;
    private String templateId;
    private Personalisation personalisation;

    private SmsRequest() {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getTemplateId() {
        return templateId;
    }

    public Personalisation getPersonalisation() {
        return personalisation;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Allows for safe and fluent creation of SmsRequest object.
     */
    public static class Builder {

        private SmsRequest request = new SmsRequest();

        public Builder phoneNumber(String phoneNumber) {
            request.phoneNumber = phoneNumber;
            return this;
        }

        public Builder templateId(String templateId) {
            request.templateId = templateId;
            return this;
        }

        public Builder personalisation(Personalisation personalisation) {
            request.personalisation = personalisation;
            return this;
        }

        /**
         * Validates and builds sms request object.
         * @return sms request object
         */
        public SmsRequest build() {
            validate();
            return request;
        }

        private void validate() {

            if (request.phoneNumber == null || request.phoneNumber.isEmpty()) {
                throw new IllegalArgumentException("Phone number cannot be empty");
            }

            if (request.templateId == null || request.templateId.isEmpty()) {
                throw new IllegalArgumentException("Template cannot be empty");
            }
        }
    }
}
