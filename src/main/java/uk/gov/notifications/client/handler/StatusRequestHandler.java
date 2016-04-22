package uk.gov.notifications.client.handler;

import org.json.JSONObject;

import uk.gov.notifications.client.api.ClientConfiguration;
import uk.gov.notifications.client.api.GovNotifyClientException;
import uk.gov.notifications.client.http.GovNotifyHttpClient;
import uk.gov.notifications.client.http.GovNotifyHttpClientResponse;
import uk.gov.notifications.client.http.HttpMethod;
import uk.gov.notifications.client.model.StatusRequest;
import uk.gov.notifications.client.model.StatusResponse;

public final class StatusRequestHandler {

    private static final String REQUEST_PATH = "/notifications/%s";

    private BaseHandler baseHandler;

    public StatusRequestHandler(ClientConfiguration configuration, GovNotifyHttpClient client) {
        baseHandler = new BaseHandler(configuration, client);
    }

    /**
     * Handles a request for notification status.
     * @param request status request
     * @return response with status details
     * @throws GovNotifyClientException thrown when any error occurs
     */
    public StatusResponse handle(StatusRequest request) throws GovNotifyClientException {

        String path = String.format(REQUEST_PATH, request.getNotificationId());

        GovNotifyHttpClientResponse httpResponse = baseHandler.handle(HttpMethod.GET, path, null);

        return buildResponse(httpResponse);
    }

    private StatusResponse buildResponse(GovNotifyHttpClientResponse httpResponse) {

        JSONObject responseBodyAsJson = new JSONObject(httpResponse.getBody());
        JSONObject notificationStatusJson = responseBodyAsJson.getJSONObject("data").getJSONObject("notification");
        return StatusResponse.builder()
                .id(notificationStatusJson.getString("id"))
                .status(notificationStatusJson.getString("status"))
                .build();
    }
}
