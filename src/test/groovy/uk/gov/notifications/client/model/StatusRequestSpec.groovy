package uk.gov.notifications.client.model

import spock.lang.Specification

public class StatusRequestSpec extends Specification {

    def builder = StatusRequest.builder().notificationId("444");

    def "Given invalid notification id, the builder should throw an exception"(String invalidNotificationId) {

        when:
        builder.notificationId(invalidNotificationId).build();

        then:
        thrown(IllegalArgumentException)

        where:
        invalidNotificationId | _
        null                  | _
        ""                    | _
    }

    def "Given I set all parameters for the request, when I build it, then I should be able to access them"() {

        when:
        def request = builder.build();
        then:
        "444" == request.getNotificationId()
    }
}
