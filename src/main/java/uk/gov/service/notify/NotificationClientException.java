package uk.gov.service.notify;

public class NotificationClientException extends Exception {
    public NotificationClientException(Exception ex) {
        super(ex);
    }

    NotificationClientException(int httpResult, String message) {
        super("Status code: " + httpResult + " " + message);
    }
}
