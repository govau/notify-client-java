package uk.gov.notifications.client.http;

/**
 * Enables custom implementation of http clients to call GovNotify service.
 */
public interface GovNotifyHttpClient {

    /**
     * Processes a request and returns response.
     * @param request request representation
     * @return response representation
     * @throws Exception representation of any client exception
     */
    GovNotifyHttpClientResponse send(GovNotifyHttpClientRequest request) throws Exception;
}
