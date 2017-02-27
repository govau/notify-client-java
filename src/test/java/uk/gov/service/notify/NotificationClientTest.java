package uk.gov.service.notify;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NotificationClientTest {
    private final String serviceId = UUID.randomUUID().toString();

    private final String apiKey = UUID.randomUUID().toString();
    private final String combinedApiKey = "Api_key_name-" +serviceId + "-" + apiKey;

    private final String baseUrl = "https://api.notifications.service.gov.uk";

    @Test
    public void testCreateNotificationClient_withSingleApiKeyAndBaseUrl(){
        NotificationClient client = new NotificationClient(combinedApiKey, baseUrl);
        assertNotificationClient(client);

    }

    @Test
    public void testCreateNotificationClient_withSingleApiKeyAndProxy() {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.1", 8080));
        NotificationClient client = new NotificationClient(combinedApiKey, baseUrl, proxy);
        assertNotificationWithProxy(proxy, client);
    }

    @Test
    public void testCreateNotificationClient_withSingleApiKeyServiceIdAndProxy() {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.1", 8080));
        NotificationClient client = new NotificationClient(combinedApiKey, baseUrl, proxy);
        assertNotificationWithProxy(proxy, client);
    }

    @Test
    public void testCreateNotificationClientSetsUserAgent() {
        NotificationClient client = new NotificationClient(combinedApiKey, baseUrl);
        assertEquals(client.getUserAgent(), "NOTIFY-API-JAVA-CLIENT/3.1.1-RELEASE");
    }

    private void assertNotificationWithProxy(Proxy proxy, NotificationClient client) {
        assertEquals(client.getApiKey(), apiKey);
        assertEquals(client.getServiceId(), serviceId);
        assertEquals(client.getBaseUrl(), baseUrl);
        assertEquals(client.getProxy(), proxy);
    }

    private void assertNotificationClient(final NotificationClient client){
        assertEquals(client.getApiKey(), apiKey);
        assertEquals(client.getServiceId(), serviceId);
        assertEquals(client.getBaseUrl(), baseUrl);
        assertNull(client.getProxy());
    }
}
