package uk.gov.notifications.client.api;

/**
 * Generic exception for failure scenarios when using API client.
 */
public class GovNotifyClientFailedResponseException extends GovNotifyClientException {

    private int statusCode;

    public GovNotifyClientFailedResponseException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    /**
     * Returns HTTP status code of the response.
     */
    public int getStatusCode() {
        return statusCode;
    }
}
