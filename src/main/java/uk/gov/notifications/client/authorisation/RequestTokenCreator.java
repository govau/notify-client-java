package uk.gov.notifications.client.authorisation;

/**
 * Contract for all token providers used to generate an Authorization token.
 */
public interface RequestTokenCreator {

    /**
     * Creates a authorisation token that is appended to request as header
     *
     * @param issuer       name of the issuer of the token
     * @param secret       string to used to sign the request/payload
     * @return request authorization token
     */
    String create(String issuer, String secret);
}
