package uk.gov.notifications.client.handler

import spock.lang.Shared
import spock.lang.Specification
import uk.gov.notifications.client.Version
import uk.gov.notifications.client.api.GovNotifyClientException
import uk.gov.notifications.client.api.GovNotifyClientFailedResponseException
import uk.gov.notifications.client.http.ApacheGovNotifyHttpClient
import uk.gov.notifications.client.http.HttpMethod
import uk.gov.notifications.testutil.ApacheCloseableHttpClientStub
import uk.gov.notifications.testutil.TokenCreatorStub

import static HttpMethod.GET
import static HttpMethod.POST
import static org.apache.http.HttpStatus.*
import static uk.gov.notifications.testutil.TestStub.CONFIGURATION

public class BaseHandlerSpec extends Specification {

    @Shared
    def tokenCreator = new TokenCreatorStub(createdToken: "TokenValue")

    def """When the request is handled,
           I should expect correct parameters passed to token creator"""() {

        given:
        def path = "/my/path";
        def body = "body"

        def httpClient = new ApacheCloseableHttpClientStub(body, SC_OK)
        def handler = new BaseHandler(CONFIGURATION, new ApacheGovNotifyHttpClient(httpClient))
                .setTokenCreator(tokenCreator)

        when:
        handler.handle GET, path, body
        then:

        tokenCreator.with {
            requestPath == "${GET} $path".toString()
            secret == CONFIGURATION.secret
            issuer == CONFIGURATION.serviceId
            body == body
        }
    }

    def """When the request is handled, I should expect a request with valid specific header"""(
            String headerName, String expectedValue, String desc) {

        given:
        def path = "/my/path";
        def body = "body"
        def httpClient = new ApacheCloseableHttpClientStub("body", SC_OK)
        def handler = new BaseHandler(CONFIGURATION, new ApacheGovNotifyHttpClient(httpClient))
                .setTokenCreator(tokenCreator)

        when:
        handler.handle GET, path, body

        then:
        expectedValue == httpClient.request.getFirstHeader(headerName).value

        where:
        headerName      | expectedValue                               | desc
        "Authorization" | "Bearer ${tokenCreator.createdToken}"       | "Authorization token"
        "Content-Type"  | "application/json"                          | "Content type"
        "User-Agent"    | "NOTIFY-API-JAVA-CLIENT/${Version.VERSION}" | "Client version"
    }

    def """When the request is handled, I should expect a request with proper body"""() {

        given:
        def body = "body"
        def httpClient = new ApacheCloseableHttpClientStub("body", SC_OK)
        def handler = new BaseHandler(CONFIGURATION, new ApacheGovNotifyHttpClient(httpClient))

        when:
        handler.handle POST, "/some/path", body

        then:
        body == httpClient.requestBody()
    }

    def """When the request is handled with specific HTTP method,
        I should expect a request sent using that method"""(
            HttpMethod method) {

        given:
        def httpClient = new ApacheCloseableHttpClientStub("body", SC_OK)
        def handler = new BaseHandler(CONFIGURATION, new ApacheGovNotifyHttpClient(httpClient))

        when:
        handler.handle method, "/some/path", "some body"

        then:
        method.toString() == httpClient.request.requestLine.method

        where:
        method | _
        GET    | _
        POST   | _
    }

    def """When the request is handled for specific path,
        I should expect correct host and path set on the HTTP request"""() {

        given:
        def path = "/the/path"
        def httpClient = new ApacheCloseableHttpClientStub("body", SC_OK)
        def handler = new BaseHandler(CONFIGURATION, new ApacheGovNotifyHttpClient(httpClient))

        when:
        handler.handle GET, path, ""

        then:
        "${CONFIGURATION.baseUrl}${path}".toString() == httpClient.request.requestLine.uri
    }


    def """Given http client throws an exception,
            when the request is handled,
            I should expect GovNotifyClientException"""() {

        given:
        ApacheCloseableHttpClientStub httpClient = new ApacheCloseableHttpClientStub(new IOException());
        BaseHandler handler = new BaseHandler(CONFIGURATION, new ApacheGovNotifyHttpClient(httpClient));

        when:
        handler.handle POST, "/some/path", "some/body"

        then:
        thrown(GovNotifyClientException)
    }

    def """Given I submit request to a handler,
        when the server responds with non2xx status,
        I should expect GovNotifyClientFailedResponseException
        with that status code and message made of response body"""(int statusCode) {

        given:
        def responseBody = "Error occurred"
        def httpClient = new ApacheCloseableHttpClientStub(responseBody, statusCode)
        def handler = new BaseHandler(CONFIGURATION, new ApacheGovNotifyHttpClient(httpClient))

        when:
        handler.handle GET, "/somePath", responseBody

        then:
        def exception = thrown(GovNotifyClientFailedResponseException)
        exception.message == responseBody
        exception.statusCode == statusCode

        where:
        statusCode               | _
        SC_BAD_REQUEST           | _
        SC_UNAUTHORIZED          | _
        SC_FORBIDDEN             | _
        SC_NOT_FOUND             | _
        SC_NOT_ACCEPTABLE        | _
        SC_SERVICE_UNAVAILABLE   | _
        SC_INTERNAL_SERVER_ERROR | _
        SC_BAD_GATEWAY           | _
    }
}
