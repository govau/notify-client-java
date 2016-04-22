package uk.gov.notifications.client.model

import groovy.json.JsonOutput
import spock.lang.Specification

public class PersonalisationSpec extends Specification {

    def """Given field name is null,
        when I build Personalisation,
        then I should expect an exception"""() {

        when:
        Personalisation.builder().field(null, "bbb").build();
        then:
        thrown(NullPointerException)
    }

    def """Given Personalisation has no fields,
        when I build it,
        then I should expect an exception"""() {

        when:
        Personalisation.builder().build();
        then:
        thrown(IllegalArgumentException)
    }

    def """Given JSON string,
        when I want to create Personalisation object out of it,
        I should get valid instance"""() {

        given:
        def json = JsonOutput.toJson(["name": "John"])
        when:
        def personalisation = Personalisation.fromJsonString(json)
        then:
        personalisation.asMap()['name'] == "John"
    }

    def """Given invalid JSON string,
        when I want to create Personalisation object out of it,
        I should get a RuntimeException"""() {

        given:
        def json = "man{gled json}"
        when:
        Personalisation.fromJsonString(json)
        then:
        thrown(RuntimeException)
    }
}
