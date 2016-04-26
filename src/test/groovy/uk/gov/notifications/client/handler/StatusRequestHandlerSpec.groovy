package uk.gov.notifications.client.handler

import spock.lang.Specification
import uk.gov.notifications.client.api.GovNotifyClientFailedResponseException
import uk.gov.notifications.client.model.StatusRequest

import static groovy.json.JsonOutput.toJson
import static uk.gov.notifications.testutil.TestStub.CONFIGURATION
import static uk.gov.notifications.testutil.TestStub.govNotifyHttpClientStub

public class StatusRequestHandlerSpec extends Specification {

    def STATUS_RESPONSE = ["data": ["notification":
                                    ["content_char_count": 29,
                                    "created_at": "2016-04-25T10:27:03.524679+00:00",
                                    "id": "6e1c96a7-4da6-439a-8143-037fad35f317",
                                    "job": null,
                                    "reference": null,
                                    "sent_at": "2016-04-25T10:27:03.524679+00:00",
                                    "sent_by": "mmg",
                                    "service": "5cf87313-fddd-4482-a2ea-48e37320efd1",
                                    "status": "delivered",
                                    "template": [
                                        "id": "5e427b42-4e98-46f3-a047-32c4a87d26bb",
                                        "name": "First template",
                                        "template_type": "sms"
                                    ],
                                    "to": "+441234123123",
                                    "updated_at": "2016-04-25T10:27:04.550407+00:00"
                                    ]
                                    ]
                          ]
    def SENDING_STATUS_RESPONSE = ["data": ["notification":
                                            [
                                             "id": "6e1c96a7-4da6-439a-8143-037fad35f317",
                                             "content_char_count": 29,
                                             "created_at": "2016-04-25T10:27:03.524679+00:00",
                                             "job": null,
                                             "reference": null,
                                             "sent_at": null,
                                             "sent_by": null,
                                             "service": "5cf87313-fddd-4482-a2ea-48e37320efd1",
                                             "status": "sending",
                                             "template": [
                                                     "id": "5e427b42-4e98-46f3-a047-32c4a87d26bb",
                                                     "name": "First template",
                                                     "template_type": "sms"
                                             ],
                                             "to": "+441234123123",
                                             "updated_at": null
                                            ]
    ]
    ]
    def httpClient = govNotifyHttpClientStub(toJson(STATUS_RESPONSE))
    def handler = new StatusRequestHandler(CONFIGURATION, httpClient)

    def """When I request notification status,
           then I should expect GET request without body
           to appropriate resource"""() {
        def client = govNotifyHttpClientStub(null, 404)
        def nullHandler = new StatusRequestHandler(CONFIGURATION, client)
        given:
        def request = StatusRequest.builder().notificationId("5").build()

        when:
        nullHandler.handle request;

        then:
        thrown(GovNotifyClientFailedResponseException)
    }

    def """When I request a notification status,
        then I should expect status response that is correctly initialized"""() {

        given:
        def request = Mock(StatusRequest)

        when:
        def response = handler.handle request

        then:
        def notificationObject = STATUS_RESPONSE["data"]["notification"];

        response.with {
            id == notificationObject["id"]
            status == notificationObject["status"]
        }
    }


    def """When I request a notification status,
           When status is sending,
        then I should expect status response that is correctly initialized"""() {
        def client = govNotifyHttpClientStub(toJson(SENDING_STATUS_RESPONSE))
        def sendingHandler = new StatusRequestHandler(CONFIGURATION, client)
        given:
        def request = Mock(StatusRequest)

        when:
        def response = sendingHandler.handle request

        then:
        def notificationObject = SENDING_STATUS_RESPONSE["data"]["notification"];

        response.with {
            id == notificationObject["id"]
            status == notificationObject["status"]
        }
    }
}
