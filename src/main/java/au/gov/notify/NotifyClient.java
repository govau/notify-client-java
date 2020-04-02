package au.gov.notify;

import au.gov.notify.dtos.Notification;
import au.gov.notify.dtos.NotificationList;
import au.gov.notify.dtos.SendEmailResponse;
import au.gov.notify.dtos.SendSmsResponse;
import au.gov.notify.dtos.Template;
import au.gov.notify.dtos.TemplateList;
import au.gov.notify.dtos.TemplatePreview;
import au.gov.notify.utils.Authentication;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotifyClient implements NotifyClientApi {

    private static final Logger LOGGER = Logger.getLogger(NotifyClient.class.toString());
    private static final String LIVE_BASE_URL = "https://rest-api.notify.gov.au";

    private final String apiKey;
    private final String serviceId;
    private final String baseUrl;
    private final Proxy proxy;
    private final String version;

    /**
     * This client constructor given the api key.
     *
     * @param apiKey Generate an API key by signing in to https://notify.gov.au, and going to the **API integration** page
     */
    public NotifyClient(final String apiKey) {
        this(
                apiKey,
                LIVE_BASE_URL,
                null
        );
    }

    /**
     * Use this client constructor if you require a proxy for https requests.
     *
     * @param apiKey Generate an API key by signing in to https://notify.gov.au, and going to the **API integration** page
     * @param proxy  Proxy used on the http requests
     */
    public NotifyClient(final String apiKey, final Proxy proxy) {
        this(
                apiKey,
                LIVE_BASE_URL,
                proxy
        );
    }

    /**
     * This client constructor is used for testing on other environments, used by the Notify.gov.au team.
     *
     * @param apiKey  Generate an API key by signing in to https://notify.gov.au, and going to the **API integration** page
     * @param baseUrl base URL, defaults to https://rest-api.notify.gov.au
     */
    public NotifyClient(final String apiKey, final String baseUrl) {
        this(
                apiKey,
                baseUrl,
                null
        );
    }


    /**
     * @param apiKey  Generate an API key by signing in to https://notify.gov.au, and going to the **API integration** page
     * @param baseUrl base URL, defaults to https://rest-api.notify.gov.au
     * @param proxy   Proxy used on the http requests
     */
    public NotifyClient(final String apiKey, final String baseUrl, final Proxy proxy) {
        this(
                apiKey,
                baseUrl,
                proxy,
                null
        );
        try {
            setDefaultSSLContext();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    public NotifyClient(final String apiKey,
                        final String baseUrl,
                        final Proxy proxy,
                        final SSLContext sslContext) {

        this.apiKey = extractApiKey(apiKey);
        this.serviceId = extractServiceId(apiKey);
        this.baseUrl = baseUrl;
        this.proxy = proxy;
        if (sslContext != null) {
            setCustomSSLContext(sslContext);
        }
        this.version = getVersion();
    }

    public String getUserAgent() {
        return "NOTIFY-API-JAVA-CLIENT/" + version;
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

    @Override
    public SendEmailResponse sendEmail(String templateId,
                                       String emailAddress,
                                       Map<String, String> personalisation,
                                       String reference) throws NotifyClientException {
        return sendEmail(templateId, emailAddress, personalisation, reference, "");
    }

    @Override
    public SendEmailResponse sendEmail(String templateId,
                                       String emailAddress,
                                       Map<String, String> personalisation,
                                       String reference,
                                       String emailReplyToId) throws NotifyClientException {

        JSONObject body = createBodyForPostRequest(templateId,
                null,
                emailAddress,
                personalisation,
                reference,
                null);

        if (emailReplyToId != null && !emailReplyToId.isEmpty()) {
            body.put("email_reply_to_id", emailReplyToId);
        }

        HttpURLConnection conn = createConnectionAndSetHeaders(baseUrl + "/v2/notifications/email", "POST");
        String response = performPostRequest(conn, body, HttpsURLConnection.HTTP_CREATED);
        return new SendEmailResponse(response);
    }

    @Override
    public SendSmsResponse sendSms(String templateId,
                                   String phoneNumber,
                                   Map<String, String> personalisation,
                                   String reference) throws NotifyClientException {

        return sendSms(templateId, phoneNumber, personalisation, reference, "");
    }

    @Override
    public SendSmsResponse sendSms(String templateId,
                                   String phoneNumber,
                                   Map<String, String> personalisation,
                                   String reference,
                                   String smsSenderId) throws NotifyClientException {

        JSONObject body = createBodyForPostRequest(templateId,
                phoneNumber,
                null,
                personalisation,
                reference,
                null);

        if (smsSenderId != null && !smsSenderId.isEmpty()) {
            body.put("sms_sender_id", smsSenderId);
        }
        HttpURLConnection conn = createConnectionAndSetHeaders(baseUrl + "/v2/notifications/sms", "POST");
        String response = performPostRequest(conn, body, HttpsURLConnection.HTTP_CREATED);
        return new SendSmsResponse(response);
    }

    @Override
    public Notification getNotificationById(String notificationId) throws NotifyClientException {
        String url = baseUrl + "/v2/notifications/" + notificationId;
        HttpURLConnection conn = createConnectionAndSetHeaders(url, "GET");
        String response = performGetRequest(conn);
        return new Notification(response);

    }

    @Override
    public NotificationList getNotifications(String status, String notification_type, String reference, String olderThanId) throws NotifyClientException {
        try {
            URIBuilder builder = new URIBuilder(baseUrl + "/v2/notifications");
            if (status != null && !status.isEmpty()) {
                builder.addParameter("status", status);
            }
            if (notification_type != null && !notification_type.isEmpty()) {
                builder.addParameter("template_type", notification_type);
            }
            if (reference != null && !reference.isEmpty()) {
                builder.addParameter("reference", reference);
            }
            if (olderThanId != null && !olderThanId.isEmpty()) {
                builder.addParameter("older_than", olderThanId);
            }

            HttpURLConnection conn = createConnectionAndSetHeaders(builder.toString(), "GET");
            String response = performGetRequest(conn);
            return new NotificationList(response);
        } catch (URISyntaxException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            throw new NotifyClientException(e);
        }
    }

    @Override
    public Template getTemplateById(String templateId) throws NotifyClientException {
        String url = baseUrl + "/v2/template/" + templateId;
        HttpURLConnection conn = createConnectionAndSetHeaders(url, "GET");
        String response = performGetRequest(conn);
        return new Template(response);
    }

    @Override
    public Template getTemplateVersion(String templateId, int version) throws NotifyClientException {
        String url = baseUrl + "/v2/template/" + templateId + "/version/" + version;
        HttpURLConnection conn = createConnectionAndSetHeaders(url, "GET");
        String response = performGetRequest(conn);
        return new Template(response);
    }

    @Override
    public TemplateList getAllTemplates(String templateType) throws NotifyClientException {
        try {
            URIBuilder builder = new URIBuilder(baseUrl + "/v2/templates");
            if (templateType != null && !templateType.isEmpty()) {
                builder.addParameter("type", templateType);
            }
            HttpURLConnection conn = createConnectionAndSetHeaders(builder.toString(), "GET");
            String response = performGetRequest(conn);
            return new TemplateList(response);
        } catch (URISyntaxException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            throw new NotifyClientException(e);
        }
    }

    @Override
    public TemplatePreview generateTemplatePreview(String templateId, Map<String, String> personalisation) throws NotifyClientException {
        JSONObject body = new JSONObject();
        if (personalisation != null && !personalisation.isEmpty()) {
            body.put("personalisation", new JSONObject(personalisation));
        }
        HttpURLConnection conn = createConnectionAndSetHeaders(baseUrl + "/v2/template/" + templateId + "/preview", "POST");
        String response = performPostRequest(conn, body, HttpsURLConnection.HTTP_OK);
        return new TemplatePreview(response);
    }

    private String performPostRequest(HttpURLConnection conn, JSONObject body, int expectedStatusCode) throws NotifyClientException {
        try {
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8);
            wr.write(body.toString());
            wr.flush();

            int httpResult = conn.getResponseCode();
            if (httpResult == expectedStatusCode) {
                StringBuilder sb = readStream(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                return sb.toString();
            } else {
                StringBuilder sb = readStream(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
                throw new NotifyClientException(httpResult, sb.toString());
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            throw new NotifyClientException(e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private String performGetRequest(HttpURLConnection conn) throws NotifyClientException {
        try {
            int httpResult = conn.getResponseCode();
            StringBuilder stringBuilder;
            if (httpResult == 200) {
                stringBuilder = readStream(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                conn.disconnect();
                return stringBuilder.toString();
            } else {
                stringBuilder = readStream(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
                throw new NotifyClientException(httpResult, stringBuilder.toString());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            throw new NotifyClientException(e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private HttpURLConnection createConnectionAndSetHeaders(String urlString, String method) throws NotifyClientException {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = getConnection(url);
            conn.setRequestMethod(method);
            Authentication authentication = new Authentication();
            String token = authentication.create(serviceId, apiKey);
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("User-agent", getUserAgent());
            if (method.equals("POST")) {
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");

            }
            return conn;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            throw new NotifyClientException(e);
        }
    }

    private HttpURLConnection getConnection(URL url) throws IOException {
        HttpURLConnection conn;

        if (null != proxy) {
            conn = (HttpURLConnection) url.openConnection(proxy);
        } else {
            conn = (HttpURLConnection) url.openConnection();
        }
        return conn;
    }

    private JSONObject createBodyForPostRequest(final String templateId,
                                                final String phoneNumber,
                                                final String emailAddress,
                                                final Map<String, String> personalisation,
                                                final String reference,
                                                final String encodedFileData) {
        JSONObject body = new JSONObject();

        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            body.put("phone_number", phoneNumber);
        }

        if (emailAddress != null && !emailAddress.isEmpty()) {
            body.put("email_address", emailAddress);
        }

        if (templateId != null && !templateId.isEmpty()) {
            body.put("template_id", templateId);
        }

        if (personalisation != null && !personalisation.isEmpty()) {
            body.put("personalisation", new JSONObject(personalisation));
        }

        if (reference != null && !reference.isEmpty()) {
            body.put("reference", reference);
        }

        if (encodedFileData != null && !encodedFileData.isEmpty()) {
            body.put("content", encodedFileData);
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

    private static void setCustomSSLContext(final SSLContext sslContext) {
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
    }


    private static String extractServiceId(String apiKey) {
        return apiKey.substring(Math.max(0, apiKey.length() - 73), Math.max(0, apiKey.length() - 37));
    }

    private static String extractApiKey(String apiKey) {
        return apiKey.substring(Math.max(0, apiKey.length() - 36));
    }


    private String getVersion() {
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
        return prop.getProperty("project.version");
    }
}
