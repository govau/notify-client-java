package uk.gov.notifications.client.http;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Model for HTTP request to GovNotify
 */
public class GovNotifyHttpClientRequest {

    private HttpMethod method;

    private String url;

    private String body;

    private Map<String, String> headers = new HashMap<>();

    /**
     * Returns a method
     * @return http method
     */
    public HttpMethod getMethod() {
        return method;
    }

    /**
     * Returns a target url
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns request body
     * @return request body
     */
    public String getBody() {
        return body;
    }

    /**
     * Returns headers to be sent with the request
     * @return request headers
     */
    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    /**
     * Returns a fluent builder for http request object.
     * @return fluent builder for http request
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Allows for safe and fluent creation of the request.
     */
    public static class Builder {

        GovNotifyHttpClientRequest request = new GovNotifyHttpClientRequest();

        /**
         * Sets http method on the builder object.
         * @param method http method
         * @return builder object
         */
        public Builder method(HttpMethod method) {
            request.method = method;
            return this;
        }

        /**
         * Sets target url on the builder object.
         * @param url target url
         * @return builder object
         */
        public Builder url(String url) {
            request.url = url;
            return this;
        }

        /**
         * Sets target body on the builder object.
         * @param body request body
         * @return builder object
         */
        public Builder body(String body) {
            request.body = body;
            return this;
        }

        /**
         * Sets http headers on the builder object.
         * @param headers http headers
         * @return builder object
         */
        public Builder headers(Map<String, String> headers) {
            request.headers = headers;
            return this;
        }

        /**
         * Validates and builds the request.
         * @return request representation
         */
        public GovNotifyHttpClientRequest build() {
            Objects.requireNonNull(request.method, "Method has to be specified!");
            Objects.requireNonNull(request.url, "Url has to be specified");
            return request;
        }
    }
}
