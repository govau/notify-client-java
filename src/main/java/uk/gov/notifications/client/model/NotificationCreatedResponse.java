package uk.gov.notifications.client.model;

import java.io.Serializable;

/**
 * Represents information available at the time of requesting a notification.
 */
public class NotificationCreatedResponse implements Serializable {

    private String id;

    private NotificationCreatedResponse() {
    }

    /**
     * Returns the id of newly created notification request.
     * @return id of notification request
     */
    public String getId() {
        return id;
    }

    /**
     * Returns builder object.
     * @return builder object
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Allows for safe and fluent creation of NotificationCreatedResponse object.
     */
    public static class Builder {

        NotificationCreatedResponse response = new NotificationCreatedResponse();

        /**
         * Sets an id on builder object.
         * @param id notification request id
         * @return builder object
         */
        public Builder id(String id) {
            response.id = id;
            return this;
        }

        /**
         * Builds notification created response.
         * @return notification created
         */
        public NotificationCreatedResponse build() {
            return response;
        }
    }
}
