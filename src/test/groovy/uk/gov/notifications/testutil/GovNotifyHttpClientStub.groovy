package uk.gov.notifications.testutil

import uk.gov.notifications.client.http.GovNotifyHttpClient
import uk.gov.notifications.client.http.GovNotifyHttpClientRequest
import uk.gov.notifications.client.http.GovNotifyHttpClientResponse

public class GovNotifyHttpClientStub implements GovNotifyHttpClient {

    final def requests = []

    private def List<GovNotifyHttpClientResponse> responses = []

    private GovNotifyHttpClientStub() {
    }

    static def builder() {
        new Builder();
    }

    static class Builder {

        def stub = new GovNotifyHttpClientStub();

        def with(GovNotifyHttpClientResponse response) {
            stub.responses << response
            this
        }

        def GovNotifyHttpClientStub build() {
            stub;
        }
    }

    @Override
    public GovNotifyHttpClientResponse send(GovNotifyHttpClientRequest request) {
        requests << request
        responses.remove(0);
    }
}
