package uk.gov.notifications.client.http

import spock.lang.Specification

import static org.apache.http.HttpStatus.SC_OK

public class GovNotifyHttpClientResponseSpec extends Specification {

    def """Given response parameters,
    when a model for response created,
    it should expose those parameters"""() {

        given:
        def body = "some_body"
        def statusCode = SC_OK

        when:
        GovNotifyHttpClientResponse response = GovNotifyHttpClientResponse.builder()
                .body(body)
                .statusCode(statusCode)
                .build()

        then:
        response.body == body
        response.statusCode == statusCode
    }
}
