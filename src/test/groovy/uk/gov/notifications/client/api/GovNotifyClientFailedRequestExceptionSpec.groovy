package uk.gov.notifications.client.api

import spock.lang.Specification

import static org.apache.http.HttpStatus.SC_SERVICE_UNAVAILABLE

public class GovNotifyClientFailedRequestExceptionSpec extends Specification {

    def "Given statusCode and message, when I create an exception, it should expose them correctly"() {

        given:
        def message = "This is a message for exception";
        def statusCode = SC_SERVICE_UNAVAILABLE;

        when:
        def e = new GovNotifyClientFailedResponseException(statusCode, message);

        then:
        statusCode == e.getStatusCode()
        message == e.getMessage()
    }
}
