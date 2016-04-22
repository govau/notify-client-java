package uk.gov.notifications.client.api;

import org.apache.commons.validator.routines.UrlValidator;

import java.io.Serializable;

import static org.apache.commons.validator.routines.UrlValidator.ALLOW_LOCAL_URLS;

/**
 * Configuration for API client.
 */
public class ClientConfiguration implements Serializable {

    private static final int MIN_SECRET_LENGTH = 32;

    private String serviceId;

    private String secret;

    private String baseUrl;

    private ClientConfiguration() {
    }

    /**
     * Returns service id.
     *
     * @return service id
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * Returns secret.
     *
     * @return secret
     */
    public String getSecret() {
        return secret;
    }

    /**
     * Returns base url eg https://example.gov.uk.
     *
     * @return base url
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Returns builder object to create configuration.
     *
     * @return builder object
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder object to ensure that client configuration will be built correctly.
     */
    public static class Builder {
        ClientConfiguration config = new ClientConfiguration();

        /**
         * Sets service id on the builder.
         * @param serviceId service id
         * @return builder object
         */
        public Builder serviceId(String serviceId) {
            config.serviceId = serviceId;
            return this;
        }

        /**
         * Sets secret on the builder.
         * @param secret secret
         * @return builder object
         */
        public Builder secret(String secret) {
            config.secret = secret;
            return this;
        }

        /**
         * Sets baseUrl on the builder.
         * @param baseUrl base url
         * @return builder object
         */
        public Builder baseUrl(String baseUrl) {
            config.baseUrl = baseUrl;
            return this;
        }

        /**
         * Validates and creates client configuration.
         */
        public ClientConfiguration build() {
            validate();
            return config;
        }

        private void validate() {

            if (config.secret == null || config.secret.length() < MIN_SECRET_LENGTH) {
                throw new IllegalArgumentException(
                        String.format(
                                "secret should have at least %d characters", MIN_SECRET_LENGTH)
                );
            }
            UrlValidator urlValidator = new UrlValidator(
                    new String [] {"https", "http"},  ALLOW_LOCAL_URLS
            );

            if (!urlValidator.isValid(config.baseUrl)) {
                throw new IllegalArgumentException("baseUrl should be a valid URL");
            }

            if (config.serviceId == null || config.serviceId.isEmpty()) {
                throw new IllegalArgumentException("serviceId cannot be empty");
            }
        }
    }
}
