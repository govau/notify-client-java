package uk.gov.service.notify;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

public class NotificationClient implements NotificationClientApi {

    private static final Logger LOGGER = Logger.getLogger(NotificationClient.class.toString());

    private final String apiKey;
    private final String serviceId;
    private final String baseUrl;
    private final Proxy proxy;

    public NotificationClient(String apiKey, String serviceId, String baseUrl) {
        this(apiKey, serviceId, baseUrl, null);
        try {
            setDefaultSSLContext();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    public NotificationClient(String apiKey, String serviceId, String baseUrl, Proxy proxy) {
        this.apiKey = apiKey.substring(Math.max(0, apiKey.length() - 36));
        this.serviceId = serviceId;
        this.baseUrl = baseUrl;
        this.proxy = proxy;
    }

    /**
     * The sendEmail method will create an HTTPS POST request. A JWT token will be created and added as an Authorization header to the request.
     *
     * @param templateId Find templateId by clicking API info for the template you want to send
     * @param to The email address
     * @param personalisation HashMap representing the placeholders for the template if any. For example, key=name value=Bob
     * @return <code>NotificationResponse</code>
     * @throws NotificationClientException
     */
    public NotificationResponse sendEmail(String templateId, String to, HashMap<String, String> personalisation) throws NotificationClientException {
        return postRequest("email", templateId, to, personalisation);
    }

    /**
     * The sendEmail method will create an HTTPS POST request. A JWT token will be created and added as an Authorization header to the request.
     *
     * @param templateId Find templateId by clicking API info for the template you want to send
     * @param to The email address
     * @return <code>NotificationResponse</code>
     * @throws NotificationClientException
     */
    public NotificationResponse sendEmail(String templateId, String to) throws NotificationClientException {
        return postRequest("email", templateId, to, null);
    }

    /**
     * The sendSms method will create an HTTPS POST request. A JWT token will be created and added as an Authorization header to the request.
     *
     * @param templateId Find templateId by clicking API info for the template you want to send
     * @param to The mobile phone number
     * @param personalisation HashMap representing the placeholders for the template if any. For example, key=name value=Bob
     * @return <code>NotificationResponse</code>
     * @throws NotificationClientException
     */
    public NotificationResponse sendSms(String templateId, String to, HashMap<String, String> personalisation) throws NotificationClientException {
        return postRequest("sms", templateId, to, personalisation);
    }

    /**
     * The sendSms method will create an HTTPS POST request. A JWT token will be created and added as an Authorization header to the request.
     *
     * @param templateId Find templateId by clicking API info for the template you want to send
     * @param to The mobile phone number
     * @return <code>NotificationResponse</code>
     * @throws NotificationClientException
     */
    public NotificationResponse sendSms(String templateId, String to) throws NotificationClientException {
        return postRequest("sms", templateId, to, null);
    }

    private NotificationResponse postRequest(String messageType, String templateId, String to, HashMap<String, String> personalisation) throws NotificationClientException {
        HttpsURLConnection conn = null;
        try {
            JSONObject body = createBodyForRequest(templateId, to, personalisation);

            Authentication tg = new Authentication();
            String token = tg.create(serviceId, apiKey);
            URL url = new URL(baseUrl + "/notifications/" + messageType);
            conn = postConnection(token, url);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(body.toString());
            wr.flush();

            int httpResult = conn.getResponseCode();
            if (httpResult == HttpsURLConnection.HTTP_CREATED) {
                StringBuilder sb = readStream(new InputStreamReader(conn.getInputStream(), "utf-8"));
                return new NotificationResponse(sb.toString());
            } else {
                StringBuilder sb = readStream(new InputStreamReader(conn.getErrorStream(), "utf-8"));
                throw new NotificationClientException(httpResult, sb.toString());
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    /**
     * The getNotificationById method will return a <code>Notification</code> for a given notification id.
     * The id is can be retrieved from the <code>NotificationResponse</code> of a <code>sendEmail</code> or <code>sendSms</code> request.
     *
     * @param notificationId The id of the notification.
     * @return <code>Notification</code>
     * @throws NotificationClientException
     */
    public Notification getNotificationById(String notificationId) throws NotificationClientException {
        StringBuilder stringBuilder;
        HttpsURLConnection conn = null;
        try {
            URL url = new URL(baseUrl + "/notifications/" + notificationId);
            conn = getConnection(url);
            conn.setRequestMethod("GET");
            Authentication authentication = new Authentication();
            String token = authentication.create(serviceId, apiKey);
            conn.setRequestProperty("Authorization", "Bearer " + token);

            conn.connect();
            int httpResult = conn.getResponseCode();
            if (httpResult == 200) {
                stringBuilder = readStream(new InputStreamReader(conn.getInputStream()));
                conn.disconnect();
                return new Notification(stringBuilder.toString());
            } else {
                stringBuilder = readStream(new InputStreamReader(conn.getErrorStream(), "utf-8"));
                throw new NotificationClientException(httpResult, stringBuilder.toString());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    /**
     * The getNotifications method will create a GET HTTPS request to retrieve all the notifications.
     *
     * @param status If status is not null notifications will only return notifications for the given status.
     *               Possible statuses are created|sending|delivered|permanent-failure|temporary-failure|technical-failure
     * @param notification_type If notification_type is not null only notification of the given status will be returned.
     *                          Possible notificationTypes are sms|email
     * @return <code>NotificationList</code>
     * @throws NotificationClientException
     */
    public NotificationList getNotifications(String status, String notification_type) throws NotificationClientException {
        JSONObject data = new JSONObject();
        if (status != null && !status.isEmpty()) {
            data.put("status", status);
        }
        if (notification_type != null && !notification_type.isEmpty()) {
            data.put("template_type", notification_type);
        }
        StringBuilder stringBuilder;
        HttpsURLConnection conn = null;
        try {
            URL url = new URL(baseUrl + "/notifications");
            conn = getConnection(url);
            conn.setRequestMethod("GET");
            Authentication authentication = new Authentication();
            String token = authentication.create(serviceId, apiKey);
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.connect();
            int httpResult = conn.getResponseCode();
            if (httpResult == 200) {
                stringBuilder = readStream(new InputStreamReader(conn.getInputStream()));
                conn.disconnect();
                return new NotificationList(stringBuilder.toString());
            } else {
                stringBuilder = readStream(new InputStreamReader(conn.getErrorStream(), "utf-8"));
                throw new NotificationClientException(httpResult, stringBuilder.toString());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    private HttpsURLConnection getConnection(URL url) throws IOException {
        HttpsURLConnection conn;

        if (null != proxy) {
            conn = (HttpsURLConnection) url.openConnection(proxy);
        } else {
            conn = (HttpsURLConnection) url.openConnection();
        }
        return conn;
    }

    private HttpsURLConnection postConnection(String token, URL url) throws IOException {
        HttpsURLConnection conn = getConnection(url);
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");

        conn.connect();
        return conn;
    }

    private JSONObject createBodyForRequest(String templateId, String to, HashMap<String, String> personalisation) {
        JSONObject body = new JSONObject();
        body.put("to", to);
        body.put("template", templateId);
        if (personalisation != null && !personalisation.isEmpty()) {
            body.put("personalisation", new JSONObject(personalisation));
        }
        return body;
    }

    private StringBuilder readStream(InputStreamReader streamReader) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(streamReader);
        String line = null;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();
        return sb;
    }

    /**
     * Set default SSL context for HTTPS connections.
     *
     * This is necessary when client has to use keystore
     * (eg provide certification for client authentication).
     *
     * Use case: enterprise proxy requiring HTTPS client authentication
     *
     * @throws NoSuchAlgorithmException
     */
    private static void setDefaultSSLContext() throws NoSuchAlgorithmException {
        HttpsURLConnection.setDefaultSSLSocketFactory(SSLContext.getDefault().getSocketFactory());
    }
}
