package uk.gov.service.notify;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NotificationClientTest {
    private final String apiKey = UUID.randomUUID().toString();
    private final String serviceId = UUID.randomUUID().toString();
    private final String baseUrl = "https://api.notifications.service.gov.uk";

    @Test
    public void testCreateNotificationClient_withSingleApiKeyAndBaseUrl(){
        String singleApiKey = "Api key name -" + serviceId + "-" + apiKey;

        NotificationClient client = new NotificationClient(singleApiKey, baseUrl);
        assertNotificationClient(client);

    }

    @Test
    public void testCreateNotificationClient_withApiKeyServiceId(){
        NotificationClient client = new NotificationClient(apiKey, serviceId, baseUrl);
        assertNotificationClient(client);
    }

    @Test
    public void testCreateNotificationClient_withSingleApiKeyAndProxy() {
        String singleApiKey = "Api key name -" + serviceId + "-" + apiKey;
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.1", 8080));
        NotificationClient client = new NotificationClient(singleApiKey, baseUrl, proxy);
        assertNotificationWithProxy(proxy, client);
    }

    @Test
    public void testCreateNotificationClient_withSingleApiKeyServiceIdAndProxy() {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.1", 8080));
        NotificationClient client = new NotificationClient(apiKey, serviceId, baseUrl, proxy);
        assertNotificationWithProxy(proxy, client);
    }

    @Test
    public void testCreateNotificationClient_withSingleApiKey() {
        String singleApiKey = "Api key name -" + serviceId + "-" + apiKey;
        NotificationClient client = new NotificationClient(singleApiKey);
        assertNotificationClient(client);
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
