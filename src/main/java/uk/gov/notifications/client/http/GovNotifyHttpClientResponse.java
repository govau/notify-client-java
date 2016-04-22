package uk.gov.notifications.client.http;

/**
 * Model for HTTP response to GovNotify
 */
public class GovNotifyHttpClientResponse {

    private String body;

    private int statusCode;

    /**
     * Returns response body
     * @return response body
     */
    public String getBody() {
        return body;
    }

    /**
     * Returns response status code.
     * @return response status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Returns a fluent builder for http response.
     * @return fluent builder for http response
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        GovNotifyHttpClientResponse response = new GovNotifyHttpClientResponse();

        public Builder body(String body) {
            response.body = body;
            return this;
        }

        public Builder statusCode(int statusCode) {
            response.statusCode = statusCode;
            return this;
        }

        public GovNotifyHttpClientResponse build() {
            return response;
        }
    }
}
