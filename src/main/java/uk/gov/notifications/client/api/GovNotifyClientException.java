package uk.gov.notifications.client.api;

/**
 * Base exception for all exceptions thrown by API client.
 */
public class GovNotifyClientException extends Exception {

    public GovNotifyClientException(Exception ex) {
        super(ex);
    }

    GovNotifyClientException(String message) {
        super(message);
    }
}
