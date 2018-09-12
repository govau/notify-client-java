package au.gov.notify;

public class NotifyClientException extends Exception
{
    private static final long serialVersionUID = 2l;
    private int httpResult;

    public NotifyClientException(Exception ex)
    {
        super(ex);
    }

    public NotifyClientException(String message)
    {
        super(message);
    }

    public NotifyClientException(String message, Throwable cause)
    {
        super(message, cause);
    }

    NotifyClientException(int httpResult, String message)
    {
        super("Status code: " + httpResult + " " + message);
        this.httpResult = httpResult;
    }

    public int getHttpResult()
    {
        return this.httpResult;
    }
}
