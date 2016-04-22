package uk.gov.notifications.client.model

import spock.lang.Specification

public class SmsRequestSpec extends Specification {

    def builder = SmsRequest.builder()
            .phoneNumber("+447098123124")
            .templateId("template")
            .personalisation(Personalisation.builder().field("firstName", "Bob").build());

    def "Given invalid phone, the builder should throw an exception"(String invalidPhone) {

        when:
        builder.phoneNumber(invalidPhone).build();

        then:
        thrown(IllegalArgumentException)

        where:
        invalidPhone | _
        null         | _
        ""           | _
    }

    def "Given invalid template id , the builder should throw an exception"(String invalidTemplateId) {

        when:
        builder.templateId(invalidTemplateId).build();

        then:
        thrown(IllegalArgumentException)

        where:
        invalidTemplateId | _
        null              | _
        ""                | _
    }

    def "Given I set all parameters for the request, when I build it, then I should be able to access them"() {

        when:
        def request = builder.build();
        then:
        "+447098123124" == request.phoneNumber
        "template" == request.templateId
        "Bob" == request.personalisation.asMap()["firstName"]
    }
}
