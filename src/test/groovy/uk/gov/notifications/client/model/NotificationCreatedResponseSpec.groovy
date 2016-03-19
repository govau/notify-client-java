package uk.gov.notifications.client.model

import spock.lang.Specification

public class NotificationCreatedResponseSpec extends Specification {

    def "Given all parameters valid, the builder should create response object"() {

        when:
        def response = NotificationCreatedResponse.builder().id("someId").build()
        then:
        response != null
    }
}
