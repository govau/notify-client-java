package uk.gov.notifications.client.handler;


import uk.gov.notifications.client.Version;
import uk.gov.notifications.client.api.ClientConfiguration;
import uk.gov.notifications.client.api.GovNotifyClientException;
import uk.gov.notifications.client.api.GovNotifyClientFailedResponseException;
import uk.gov.notifications.client.authorisation.JwtTokenCreator;
import uk.gov.notifications.client.authorisation.RequestTokenCreator;
import uk.gov.notifications.client.http.GovNotifyHttpClient;
import uk.gov.notifications.client.http.GovNotifyHttpClientRequest;
import uk.gov.notifications.client.http.GovNotifyHttpClientResponse;
import uk.gov.notifications.client.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;

class BaseHandler {

    private RequestTokenCreator tokenCreator = new JwtTokenCreator();

    private ClientConfiguration configuration;

    private GovNotifyHttpClient client;

    public BaseHandler(ClientConfiguration configuration, GovNotifyHttpClient client) {
        this.configuration = configuration;
        this.client = client;
    }

    /**
     * Executes http request to gov notify interface.
     *
     * @param method http method to be used
     * @param path   relative path to specific service resource
     * @param body   optional payload to be sent to the service
     * @return a representation of succesful http response from the service
     * @throws GovNotifyClientException exception thrown when underlying transport fails or the
     *                                  response is not successful
     */
    public GovNotifyHttpClientResponse handle(HttpMethod method, String path, String body)
            throws GovNotifyClientException {

        String token = tokenCreator.create(
                configuration.getServiceId(),
                configuration.getSecret()
        );

        GovNotifyHttpClientResponse response;

        try {
            response = client.send(
                    GovNotifyHttpClientRequest.builder()
                            .method(method)
                            .url(configuration.getBaseUrl() + path)
                            .headers(standardHeaders(token))
                            .body(body)
                            .build()
            );

        } catch (Exception ex) {
            throw new GovNotifyClientException(ex);
        }

        if (response.getStatusCode() == SC_CREATED || response.getStatusCode() == SC_OK) {
            return response;
        } else {
            throw new GovNotifyClientFailedResponseException(
                    response.getStatusCode(), response.getBody()
            );
        }
    }

    public BaseHandler setTokenCreator(RequestTokenCreator creator) {
        this.tokenCreator = creator;
        return this;
    }

    private Map<String, String> standardHeaders(String token) {

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + token);
        headers.put("User-Agent", "NOTIFY-API-JAVA-CLIENT/" + Version.VERSION);
        return headers;
    }
}
