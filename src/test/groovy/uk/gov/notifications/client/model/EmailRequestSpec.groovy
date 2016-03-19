package uk.gov.notifications.client.model

import spock.lang.Specification

public class EmailRequestSpec extends Specification {

    def builder = EmailRequest.builder()
            .email("email@email1.com")
            .templateId("template")
            .personalisation(Personalisation.builder().field("firstName", "Bob").build());

    def "Given invalid email, the builder should throw an exception"(String invalidEmail) {

        when:
        builder.email(invalidEmail).build();

        then:
        thrown(IllegalArgumentException)

        where:
        invalidEmail    | _
        null            | _
        ""              | _
        "invalid.email" | _
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

    def "Given all parameters valid, the builder should create request object"() {

        when:
        def request = builder.build();

        then:
        request != null
    }
}
