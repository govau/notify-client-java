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
        httpClient.send(_) >> httpResponse(toJson(["data":["notification":["id": notificationId]]]));

        when:
        def response = apiClient.sendEmail "my@example-email.com" , "some_template_id"

        then:
        response.id == notificationId
    }

    def "When I send sms notification request, I expect its id in return"() {

        def notificationId = "3"
        httpClient.send(_) >> httpResponse(toJson(["data":["notification":["id": notificationId]]]))

        when:
        def response = apiClient.sendSms "+487809123123", "some_template_id"

        then:
        response.id == notificationId
    }

    def "When I check notification status by id, I expect status data in return"() {

        def notificationId = "6e1c96a7-4da6-439a-8143-037fad35f317"
        httpClient.send(_) >> httpResponse(toJson(
                ["data": ["notification":
                                  ["id"    : notificationId,
                                   "status": "delivered",
                                   "content_char_count": 29,
                                   "created_at": "2016-04-25T10:27:03.524679+00:00",
                                   "id": notificationId,
                                   "job": null,
                                   "reference": null,
                                   "sent_at": "2016-04-25T10:27:04.119717+00:00",
                                   "sent_by": "mmg",
                                   "service": "5cf87313-fddd-4482-a2ea-48e37320efd1",
                                   "status": "delivered",
                                   "template": [
                                           "id": "5e427b42-4e98-46f3-a047-32c4a87d26bb",
                                           "name": "First template",
                                           "template_type": "sms"
                                   ],
                                   "to": "+441111111111",
                                   "updated_at": "2016-04-25T10:27:04.550407+00:00"
                                  ]
                ]
                ]
        ));

        when:
        def response = apiClient.checkStatus notificationId

        then:
        response.id == notificationId
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
