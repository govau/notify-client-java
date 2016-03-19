package uk.gov.notifications.client.model;

import java.io.Serializable;

/**
 * Represents status information of the already requested notifications.
 */
public class StatusResponse implements Serializable {

    private String id;

    private String status;

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

        public StatusResponse build() {
            return response;
        }
    }
}


