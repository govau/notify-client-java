package uk.gov.notifications.client.authorisation.crypto;

/**
 * Sign data with a secret key.
 */
public interface Signer {

    /**
     * Creates a signature for a payload with the use of secret key.
     * @param payload data to be signed
     * @param secretKey secret key
     * @return payload signature
     */
    String sign(String payload, String secretKey);
}
