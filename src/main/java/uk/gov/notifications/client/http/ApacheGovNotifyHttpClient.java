package uk.gov.notifications.client.http;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.util.Map;

import static uk.gov.notifications.client.http.HttpMethod.GET;

/**
 * Http client implementation utilising Apache HTTP Client library.
 */
public class ApacheGovNotifyHttpClient implements GovNotifyHttpClient {

    private CloseableHttpClient httpClient;

    /**
     * Creates http client that uses Apache HttpClient as transport manager.
     */
    public ApacheGovNotifyHttpClient() {
        this(HttpClientBuilder.create()
                .setConnectionManager(new BasicHttpClientConnectionManager())
                .build());
    }

    public ApacheGovNotifyHttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public GovNotifyHttpClientResponse send(GovNotifyHttpClientRequest request) throws Exception {

        HttpUriRequest httpUriRequest;
        if (request.getMethod() == GET) {
            httpUriRequest = new HttpGet(request.getUrl());
        } else {
            HttpPost post = new HttpPost(request.getUrl());
            post.setEntity(new StringEntity(request.getBody()));
            httpUriRequest = post;
        }

        for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
            httpUriRequest.setHeader(entry.getKey(), entry.getValue());
        }

        CloseableHttpResponse response = httpClient.execute(httpUriRequest);
        String content = EntityUtils.toString(response.getEntity());
        int status = response.getStatusLine().getStatusCode();

        return GovNotifyHttpClientResponse.builder().body(content).statusCode(status).build();
    }
}
