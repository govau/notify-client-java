package uk.gov.notifications.testutil

import org.apache.http.HttpStatus
import uk.gov.notifications.client.api.ClientConfiguration
import uk.gov.notifications.client.http.GovNotifyHttpClientResponse

public class TestStub {

    static def SERVICE_ID = UUID.randomUUID().toString();

    static def BASE_URL = "https://base-url.com";

    static def SECRET = UUID.randomUUID().toString();

    public static final ClientConfiguration CONFIGURATION = ClientConfiguration.builder()
            .baseUrl(BASE_URL).secret(SECRET).serviceId(SERVICE_ID).build();


    static
    def GovNotifyHttpClientStub govNotifyHttpClientStub(String returnedBody, int statusCode = HttpStatus.SC_OK) {
        GovNotifyHttpClientStub.builder().with(GovNotifyHttpClientResponse.builder()
                .body(returnedBody)
                .statusCode(statusCode)
                .build())
                .build()
    }
}
