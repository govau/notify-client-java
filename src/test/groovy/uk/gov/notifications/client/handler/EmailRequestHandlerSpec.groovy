package uk.gov.notifications.client.handler

import groovy.json.JsonSlurper
import spock.lang.Specification
import uk.gov.notifications.client.model.EmailRequest
import uk.gov.notifications.client.model.Personalisation

import static groovy.json.JsonOutput.toJson
import static uk.gov.notifications.client.http.HttpMethod.POST
import static uk.gov.notifications.testutil.TestStub.CONFIGURATION
import static uk.gov.notifications.testutil.TestStub.govNotifyHttpClientStub

public class EmailRequestHandlerSpec extends Specification {

    def EXPECTED_NOTIFICATION_ID = "999"

    def httpClient = govNotifyHttpClientStub(toJson(["notification_id": EXPECTED_NOTIFICATION_ID]));

    def handler = new EmailRequestHandler(CONFIGURATION, httpClient)

    def """When I request email notification,
        I should expect POST request with correct body to appropriate resource"""() {

        def (email, template) = ["yes@itcan.work", "1"]

        given:
        def expectedBody = [
                "to"             : email,
                "template"       : template,
                "personalisation": ["name": "John"]
        ]

        def request = EmailRequest.builder()
                .email(email)
                .templateId(template)
                .personalisation(Personalisation.builder().field("name", "John").build())
                .build()

        when:
        handler.handle request

        then:

        def resourcePath = "/notifications/email";

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
        def request = Mock(EmailRequest)

        when:
        def response = handler.handle request

        then:
        response.id == EXPECTED_NOTIFICATION_ID
    }
}
