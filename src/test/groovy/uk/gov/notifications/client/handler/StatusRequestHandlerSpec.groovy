package uk.gov.notifications.client.handler

import spock.lang.Specification
import uk.gov.notifications.client.model.StatusRequest

import static groovy.json.JsonOutput.toJson
import static uk.gov.notifications.testutil.TestStub.CONFIGURATION
import static uk.gov.notifications.testutil.TestStub.govNotifyHttpClientStub

public class StatusRequestHandlerSpec extends Specification {

    def STATUS_RESPONSE = [
            "notification":
                    [
                            "id"    : "4",
                            "status": "delivered",
                    ]
    ]

    def httpClient = govNotifyHttpClientStub(toJson(STATUS_RESPONSE))
    def handler = new StatusRequestHandler(CONFIGURATION, httpClient)

    def """When I request notification status,
           then I should expect GET request without body
           to appropriate resource"""() {

        given:
        def request = StatusRequest.builder().notificationId("5").build()

        when:
        handler.handle request;

        then:
        def resourcePath = "/notifications/${request.notificationId}"
        httpClient.requests.first().with {
            body == null
            url.endsWith resourcePath
        }
    }

    def """When I request a notification status,
        then I should expect status response that is correctly initialized"""() {

        given:
        def request = Mock(StatusRequest)

        when:
        def response = handler.handle request

        then:
        def notificationObject = STATUS_RESPONSE["notification"];

        response.with {
            id == notificationObject["notificationId"]
            status == notificationObject["status"]
        }
    }
}
