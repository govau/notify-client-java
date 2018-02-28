package uk.gov.service.notify;

public class NotificationClientException extends Exception
{
    private static final long serialVersionUID = 2l;
    private int httpResult;

    public NotificationClientException(Exception ex)
    {
        super(ex);
    }

    public NotificationClientException(String message)
    {
        super(message);
    }

    public NotificationClientException(String message, Throwable cause)
    {
        super(message, cause);
    }

    NotificationClientException(int httpResult, String message)
    {
        super("Status code: " + httpResult + " " + message);
        this.httpResult = httpResult;
    }

    public int getHttpResult()
    {
        return this.httpResult;
    }
}
