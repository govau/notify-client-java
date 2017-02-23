package uk.gov.service.notify;

public class NotificationClientException extends Exception {
    private static final long serialVersionUID = 2l;
    private int httpResult;

    public NotificationClientException(Exception ex) {
        super(ex);
    }

    NotificationClientException(int httpResult, String message) {
        super("Status code: " + httpResult + " " + message);
        this.httpResult = httpResult;
    }

    public int getHttpResult(){
        return this.httpResult;
    }
}
