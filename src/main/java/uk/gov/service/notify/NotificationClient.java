package uk.gov.service.notify;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

public class NotificationClient implements NotificationClientApi {

    private static final Logger LOGGER = Logger.getLogger(NotificationClient.class.toString());
    public static final String LIVE_BASE_URL = "https://api.notifications.service.gov.uk";

    private final String apiKey;
    private final String serviceId;
    private final String baseUrl;
    private final Proxy proxy;
    private final String version;

    /**
     * This client constructor given the api key.
     * @param apiKey Generate an API key by signing in to GOV.UK Notify, https://www.notifications.service.gov.uk, and going to the **API integration** page
     */
    public NotificationClient(final String apiKey) {
        this(
                apiKey,
                LIVE_BASE_URL,
                null
        );
    }

    /**
     * Use this client constructor if you require a proxy for https requests.
     * @param apiKey Generate an API key by signing in to GOV.UK Notify, https://www.notifications.service.gov.uk, and going to the **API integration** page
     * @param proxy Proxy used on the http requests
     */
    public NotificationClient(final String apiKey, final Proxy proxy) {
        this(
                apiKey,
                LIVE_BASE_URL,
                proxy
        );
    }

    /**
     * This client constructor is used for testing on other environments, used by the GOV.UK Notify team.
     * @param apiKey Generate an API key by signing in to GOV.UK Notify, https://www.notifications.service.gov.uk, and going to the **API integration** page
     * @param baseUrl
     */
    public NotificationClient(final String apiKey, final String baseUrl) {
        this(
                apiKey,
                baseUrl,
                null
        );
    }


    /**
     *
     * @param apiKey Generate an API key by signing in to GOV.UK Notify, https://www.notifications.service.gov.uk, and going to the **API integration** page
     * @param baseUrl base URL, defaults to https://api.notifications.service.gov.uk
     * @param proxy
     */
    public NotificationClient(final String apiKey, final String baseUrl, final Proxy proxy) {
        this.apiKey = extractApiKey(apiKey);
        this.serviceId = extractServiceId(apiKey);
        this.baseUrl = baseUrl;
        this.proxy = proxy;
        try {
            setDefaultSSLContext();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
        this.version = getVersion();
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public Proxy getProxy() {
        return proxy;
    }

    /**
     * The sendEmail method will create an HTTPS POST request. A JWT token will be created and added as an Authorization header to the request.
     *
     * @param templateId      Find templateId by clicking API info for the template you want to send
     * @param emailAddress    The email address
     * @param personalisation HashMap representing the placeholders for the template if any. For example, key=name value=Bob
     *                        Can be an empty map or null.
     * @param reference       A reference specified by the service for the notification. Get all notifications can be filtered by this reference.
     *                        This reference can be unique or used used to refer to a batch of notifications.
     *                        Can be an empty string or null.
     * @return <code>SendEmailResponse</code>
     * @throws NotificationClientException
     */
    public SendEmailResponse sendEmail(String templateId, String emailAddress, HashMap<String, String> personalisation, String reference) throws NotificationClientException {
        HttpsURLConnection conn = null;
        try {
            JSONObject body = createBodyForEmailRequest(templateId, emailAddress, personalisation, reference);

            Authentication tg = new Authentication();
            String token = tg.create(serviceId, apiKey);
            URL url = new URL(baseUrl + "/v2/notifications/email");
            conn = postConnection(token, url);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(body.toString());
            wr.flush();

            int httpResult = conn.getResponseCode();
            if (httpResult == HttpsURLConnection.HTTP_CREATED) {
                StringBuilder sb = readStream(new InputStreamReader(conn.getInputStream(), "utf-8"));
                return new SendEmailResponse(sb.toString());
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
     * The sendSms method will create an HTTPS POST request. A JWT token will be created and added as an Authorization header to the request.
     *
     * @param templateId      Find templateId by clicking API info for the template you want to send
     * @param phoneNumber              The mobile phone number
     * @param personalisation HashMap representing the placeholders for the template if any. For example, key=name value=Bob
     *                        Can be an empty map or null.
     * @param reference       A reference specified by the service for the notification. Get all notifications can be filtered by this reference.
     *                        This reference can be unique or used used to refer to a batch of notifications.
     *                        Can be an empty string or null.
     * @return <code>SendSmsResponse</code>
     * @throws NotificationClientException
     */
    public SendSmsResponse sendSms(String templateId, String phoneNumber, HashMap<String, String> personalisation, String reference) throws NotificationClientException {
        HttpsURLConnection conn = null;

        try {
            JSONObject body = createBodyForSmsRequest(templateId, phoneNumber, personalisation, reference);

            Authentication tg = new Authentication();
            String token = tg.create(serviceId, apiKey);
            URL url = new URL(baseUrl + "/v2/notifications/sms");
            conn = postConnection(token, url);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(body.toString());
            wr.flush();

            int httpResult = conn.getResponseCode();
            if (httpResult == HttpsURLConnection.HTTP_CREATED) {
                StringBuilder sb = readStream(new InputStreamReader(conn.getInputStream(), "utf-8"));
                return new SendSmsResponse(sb.toString());
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
            URL url = new URL(baseUrl + "/v2/notifications/" + notificationId);
            conn = getConnection(url);
            conn.setRequestMethod("GET");
            Authentication authentication = new Authentication();
            String token = authentication.create(serviceId, apiKey);
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("User-agent", "NOTIFY-API-JAVA-CLIENT/" + version);

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
     * @param status            If status is not null notifications will only return notifications for the given status.
     *                          Possible statuses are created|sending|delivered|permanent-failure|temporary-failure|technical-failure
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
            URL url = new URL(baseUrl + "/v2/notifications");
            conn = getConnection(url);
            conn.setRequestMethod("GET");
            Authentication authentication = new Authentication();
            String token = authentication.create(serviceId, apiKey);
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("User-agent", "NOTIFY-API-JAVA-CLIENT/" + version);

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
        conn.setRequestProperty("User-agent", "NOTIFY-API-JAVA-CLIENT/" + version);

        conn.connect();
        return conn;
    }

    private JSONObject createBodyForSmsRequest(final String templateId, final String phoneNumber, final HashMap<String, String> personalisation, final String reference) {
        JSONObject body = new JSONObject();
        body.put("phone_number", phoneNumber);
        body.put("template_id", templateId);
        if (personalisation != null && !personalisation.isEmpty()) {
            body.put("personalisation", new JSONObject(personalisation));
        }
        if(reference != null && !reference.isEmpty()){
            body.put("reference", reference);
        }
        return body;
    }
    private JSONObject createBodyForEmailRequest(final String templateId, final String emailAddress, final HashMap<String, String> personalisation, final String reference) {
        JSONObject body = new JSONObject();
        body.put("email_address", emailAddress);
        body.put("template_id", templateId);
        if (personalisation != null && !personalisation.isEmpty()) {
            body.put("personalisation", new JSONObject(personalisation));
        }
        if(reference != null && !reference.isEmpty()){
            body.put("reference", reference);
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
     * <p/>
     * This is necessary when client has to use keystore
     * (eg provide certification for client authentication).
     * <p/>
     * Use case: enterprise proxy requiring HTTPS client authentication
     *
     * @throws NoSuchAlgorithmException
     */
    private static void setDefaultSSLContext() throws NoSuchAlgorithmException {
        HttpsURLConnection.setDefaultSSLSocketFactory(SSLContext.getDefault().getSocketFactory());
    }

    private static String extractServiceId(String apiKey) {
        return apiKey.substring(Math.max(0, apiKey.length() - 73), Math.max(0, apiKey.length() - 37));
    }

    private static String extractApiKey(String apiKey) {
        return apiKey.substring(Math.max(0, apiKey.length() - 36));
    }


    private String getVersion(){
        InputStream input = null;
        Properties prop = new Properties();
        try {
            input = getClass().getClassLoader().getResourceAsStream("application.properties");

            prop.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return prop.getProperty("version");
    }
}
