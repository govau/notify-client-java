package uk.gov.notifications.client.http

import spock.lang.Specification

import static HttpMethod.GET
import static HttpMethod.POST

public class GovNotifyHttpClientRequestSpec extends Specification {

    def """Given request parameters,
    when a model for request created,
    it should expose those parameters """() {

        when:
        def headers = ['Content-Type': 'application/json'] as Map
        GovNotifyHttpClientRequest request = GovNotifyHttpClientRequest.builder()
                .method(POST)
                .url("http://url")
                .headers(headers)
                .body("body")
                .build()

        then:
        "http://url" == request.url
        "body" == request.body
        POST == request.method
        headers.equals request.headers
    }

    def "Given request method undefined, should throw an exception"() {

        when:
        GovNotifyHttpClientRequest.builder()
                .url("http://url")
                .build();
        then:
        thrown(NullPointerException)
    }

    def "Given url undefined, should throw an exception"() {

        when:
        GovNotifyHttpClientRequest.builder()
                .method(GET)
                .build();
        then:
        thrown(NullPointerException)
    }
}
