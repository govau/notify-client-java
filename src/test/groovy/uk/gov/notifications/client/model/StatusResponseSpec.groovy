package uk.gov.notifications.client.model

import org.junit.Assert
import org.junit.Test
import spock.lang.Specification

public class StatusResponseSpec extends Specification {

    @Test
    def "Given I set all parameters for the request, when I build it, then I should be able to access them"() {

        given:
        Assert.assertNotNull(StatusResponse.builder()
                .id("someId")
                .status("someStatus")
                .build());
    }
}
