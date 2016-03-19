package uk.gov.notifications.testutil

import org.apache.commons.io.IOUtils
import org.apache.http.*
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.conn.ClientConnectionManager
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.message.BasicHttpResponse
import org.apache.http.message.BasicStatusLine
import org.apache.http.params.HttpParams
import org.apache.http.protocol.HttpContext

public class ApacheCloseableHttpClientStub extends CloseableHttpClient {

    def response, exception, request;

    public ApacheCloseableHttpClientStub(String body, int statusCode) {
        this.response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), statusCode, ""));
        this.response.setEntity(new StringEntity(body));
    }

    public ApacheCloseableHttpClientStub(IOException exception) {
        this.exception = exception;
    }

    public String requestBody() {
        return IOUtils.toString((request as HttpEntityEnclosingRequest).entity.content);
    }

    @Override
    protected CloseableHttpResponse doExecute(HttpHost target, HttpRequest request, HttpContext context) {
        this.request = request;

        if (exception != null) {
            throw new IOException();
        }

        new MockCloseableHttpResponse(response);
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public HttpParams getParams() {
        return null;
    }

    @Override
    public ClientConnectionManager getConnectionManager() {
        return null;
    }

    private class MockCloseableHttpResponse implements CloseableHttpResponse {

        private HttpResponse r;

        public MockCloseableHttpResponse(HttpResponse response) {
            this.r = response;
        }

        @Override
        public void close() throws IOException {

        }

        @Override
        public StatusLine getStatusLine() {
            return this.r.getStatusLine();
        }

        @Override
        public void setStatusLine(StatusLine statusline) {
            this.r.setStatusLine(statusline);
        }

        @Override
        public void setStatusLine(ProtocolVersion ver, int code) {
            this.r.setStatusLine(ver, code);
        }

        @Override
        public void setStatusLine(ProtocolVersion ver, int code, String reason) {
            this.r.setStatusLine(ver, code, reason);
        }

        @Override
        public void setStatusCode(int code) throws IllegalStateException {
            this.r.setStatusCode(code);
        }

        @Override
        public void setReasonPhrase(String reason) throws IllegalStateException {
            this.r.setReasonPhrase(reason);
        }

        @Override
        public HttpEntity getEntity() {
            return this.r.getEntity();
        }

        @Override
        public void setEntity(HttpEntity entity) {
            this.r.setEntity(entity);
        }

        @Override
        public Locale getLocale() {
            return this.r.getLocale();
        }

        @Override
        public void setLocale(Locale loc) {
            r.setLocale(loc);
        }

        @Override
        public ProtocolVersion getProtocolVersion() {
            return r.getProtocolVersion();
        }

        @Override
        public boolean containsHeader(String name) {
            return this.r.containsHeader(name);
        }

        @Override
        public Header[] getHeaders(String name) {
            return r.getHeaders(name);
        }

        @Override
        public Header getFirstHeader(String name) {
            return r.getFirstHeader(name);
        }

        @Override
        public Header getLastHeader(String name) {
            return r.getLastHeader(name);
        }

        @Override
        public Header[] getAllHeaders() {
            return r.getAllHeaders();
        }

        @Override
        public void addHeader(Header header) {
            r.addHeader(header);
        }

        @Override
        public void addHeader(String name, String value) {
            r.addHeader(name, value);
        }

        @Override
        public void setHeader(Header header) {
            r.setHeader(header);
        }

        @Override
        public void setHeader(String name, String value) {
            r.setHeader(name, value);
        }

        @Override
        public void setHeaders(Header[] headers) {
            r.setHeaders(headers);
        }

        @Override
        public void removeHeader(Header header) {
            r.removeHeader(header);
        }

        @Override
        public void removeHeaders(String name) {
            r.removeHeaders(name);
        }

        @Override
        public HeaderIterator headerIterator() {
            return r.headerIterator();
        }

        @Override
        public HeaderIterator headerIterator(String name) {
            return r.headerIterator(name);
        }

        @Override
        public HttpParams getParams() {
            return r.getParams();
        }

        @Override
        public void setParams(HttpParams params) {
            r.setParams(params);
        }
    }
}
