package uk.gov.notifications.client.handler;

import org.json.JSONObject;

import uk.gov.notifications.client.api.ClientConfiguration;
import uk.gov.notifications.client.api.GovNotifyClientException;
import uk.gov.notifications.client.http.GovNotifyHttpClient;
import uk.gov.notifications.client.http.GovNotifyHttpClientResponse;
import uk.gov.notifications.client.model.NotificationCreatedResponse;
import uk.gov.notifications.client.model.SmsRequest;

import static uk.gov.notifications.client.http.HttpMethod.POST;


public final class SmsRequestHandler {

    private static final String REQUEST_PATH = "/notifications/sms";

    private BaseHandler baseHandler;

    public SmsRequestHandler(ClientConfiguration configuration, GovNotifyHttpClient client) {
        baseHandler = new BaseHandler(configuration, client);
    }

    /**
     * Handles SMS request.
     *
     * @param request request to send SMS
     * @return response object
     * @throws GovNotifyClientException thrown when any error occurs
     */
    public NotificationCreatedResponse handle(SmsRequest request) throws GovNotifyClientException {

        String body = buildBody(request);

        GovNotifyHttpClientResponse httpResponse = baseHandler.handle(
                POST, REQUEST_PATH, body
        );

        return buildResponse(httpResponse);
    }

    private String buildBody(SmsRequest request) {

        JSONObject bodyJsonObject = new JSONObject()
                .put("to", request.getPhoneNumber())
                .put("template", request.getTemplateId());

        if (request.getPersonalisation() != null) {
            JSONObject personalisationAsJson = new JSONObject(request.getPersonalisation().asMap());
            bodyJsonObject.put("personalisation", personalisationAsJson);
        }
        return bodyJsonObject.toString();
    }

    private NotificationCreatedResponse buildResponse(GovNotifyHttpClientResponse httpResponse) {

        JSONObject responseBodyAsJson = new JSONObject(httpResponse.getBody());
        String notificationId = responseBodyAsJson.getJSONObject("data").getJSONObject("notification").getString("id");
        return new NotificationCreatedResponse.Builder().id(notificationId).build();
    }
}
