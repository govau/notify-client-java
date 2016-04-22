package uk.gov.notifications.client.api

import spock.lang.Specification

import static uk.gov.notifications.testutil.TestStub.*

public class ClientConfigurationSpec extends Specification {

    def configBuilder = ClientConfiguration.builder()
            .baseUrl(BASE_URL)
            .secret(SECRET)
            .serviceId(SERVICE_ID);

    def "Given invalid url, should throw an exception"(String url) {

        when:
        configBuilder.baseUrl(url).build();

        then:
        thrown(IllegalArgumentException)

        where:
        url              | _
        null             | _
        ""               | _
        "http://url,com" | _
    }

    def "Given valid url, it should pass"(String url) {

        when:
        configBuilder.baseUrl(url).build();

        then:
        assert true

        where:
        url                           | _
        "http://localhost"            | _
        "https://some.works"          | _
        "https://some.boring.url.org" | _
    }

    def "Given invalid secret passed, the builder should throw an exception"(String secret) {

        when:
        configBuilder.secret(secret).build();

        then:
        thrown(IllegalArgumentException)

        where:
        secret                            | _
        ""                                | _
        "This is exactly 31 characters  " | _
    }

    def "Given invalid serviceId, the builder should throw an exception"(String serviceId) {

        when:
        configBuilder.serviceId(serviceId).build();

        then:
        thrown(IllegalArgumentException)

        where:
        serviceId | _
        ""        | _
        null      | _
    }

    def "Given all parameters set, the builder should return an object"() {

        expect:
        configBuilder.build() != null
    }
}
