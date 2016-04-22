package uk.gov.notifications.client.model;

/**
 * Represents a request to retrieve status information about already requested notifications.
 */
public class StatusRequest extends GovNotifyRequest {

    private String notificationId;

    private StatusRequest() {
    }

    /**
     * Returns an id of notification a status of which is requested.
     * @return notification id
     */
    public String getNotificationId() {
        return notificationId;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Allows for safe and fluent creation of StatusRequest object.
     */
    public static class Builder {

        private StatusRequest request = new StatusRequest();

        /**
         * Sets an id of the notification the status of which is requested.
         * @param notificationId notification id
         * @return builder object
         */
        public Builder notificationId(String notificationId) {
            request.notificationId = notificationId;
            return this;
        }

        /**
         * Validates and builds a StatusRequest object.
         * @return StatusRequest object
         */
        public StatusRequest build() {
            validate();
            return request;
        }

        private void validate() {

            if (request.notificationId == null || request.notificationId.isEmpty()) {
                throw new IllegalArgumentException("notificationId cannot be empty!");
            }
        }
    }
}
