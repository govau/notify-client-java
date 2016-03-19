package uk.gov.notifications.client.api

import spock.lang.Specification
import uk.gov.notifications.client.http.GovNotifyHttpClient
import uk.gov.notifications.client.http.GovNotifyHttpClientResponse

import static groovy.json.JsonOutput.toJson
import static org.apache.http.HttpStatus.SC_FORBIDDEN
import static org.apache.http.HttpStatus.SC_OK
import static uk.gov.notifications.testutil.TestStub.CONFIGURATION

class GovNotifyApiClientSpec extends Specification {


    def httpClient = Mock(GovNotifyHttpClient)

    def apiClient = new GovNotifyApiClient(CONFIGURATION, httpClient)


    def "When I send email notification request, I expect its id in return"() {

        def notificationId = "3"
        httpClient.send(_) >> httpResponse(toJson(["notification_id": notificationId]));

        when:
        def response = apiClient.sendEmail "my@example-email.com" , "some_template_id"

        then:
        response.id == notificationId
    }

    def "When I send sms notification request, I expect its id in return"() {

        def notificationId = "3"
        httpClient.send(_) >> httpResponse(toJson(["notification_id": notificationId]))

        when:
        def response = apiClient.sendSms "+487809123123", "some_template_id"

        then:
        response.id == notificationId
    }

    def "When I check notification status by id, I expect status data in return"() {

        httpClient.send(_) >> httpResponse(toJson(
                ["notification":
                         ["id"    : "4",
                          "status": "delivered"]
                ]
        ));

        when:
        def response = apiClient.checkStatus "4"

        then:
        response.id == "4"
        response.status == "delivered"
    }

    def """Given GovNotify responds with error (invalid token, token expired, invalid serviceId, etc)
            when I call the client,
            then I should expect GovNotifyClientFailedResponseException"""() {

        given:
        httpClient.send(_) >> httpResponse("Invalid token", SC_FORBIDDEN)
        when:
        apiClient.sendEmail "my@example.works", "some_template_id"
        then:
        thrown(GovNotifyClientFailedResponseException)

    }

    def httpResponse(String body, statusCode = SC_OK) {
        return GovNotifyHttpClientResponse.builder().body(body).statusCode(statusCode).build()
    }
}
