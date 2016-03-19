package uk.gov.notifications.client.handler

import groovy.json.JsonSlurper
import spock.lang.Specification
import uk.gov.notifications.client.model.Personalisation
import uk.gov.notifications.client.model.SmsRequest

import static groovy.json.JsonOutput.toJson
import static uk.gov.notifications.client.http.HttpMethod.POST
import static uk.gov.notifications.testutil.TestStub.CONFIGURATION
import static uk.gov.notifications.testutil.TestStub.govNotifyHttpClientStub

class SmsRequestHandlerSpec extends Specification {

    def EXPECTED_NOTIFICATION_ID = "999"

    def httpClient = govNotifyHttpClientStub(toJson(["notification_id": EXPECTED_NOTIFICATION_ID]));

    def handler = new SmsRequestHandler(CONFIGURATION, httpClient)

    def """When I request sms notification,
        I should expect POST request with correct body to
        /notifications/sms resource """() {

        given:
        def expectedBody = [
                "to"             : "+447890123456",
                "template"       : "1",
                "personalisation": ["name": "John"]
        ]

        def request = SmsRequest.builder()
                .phoneNumber("+447890123456")
                .templateId("1")
                .personalisation(Personalisation.builder().field("name", "John").build())
                .build()

        when:
        handler.handle request

        then:
        def resourcePath = "/notifications/sms"
        httpClient.requests.first().with {
            new JsonSlurper().parseText(body) == expectedBody
            method == POST
            url.endsWith resourcePath
        }
    }

    def """When I request sms notification,
        I should expect a notification id
        so I can further check its status"""() {

        given:
        def request = Mock(SmsRequest)

        when:
        def response = handler.handle request

        then:
        response.id == EXPECTED_NOTIFICATION_ID
    }
}

