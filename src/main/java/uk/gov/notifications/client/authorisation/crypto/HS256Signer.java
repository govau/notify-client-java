package uk.gov.notifications.client.authorisation.crypto;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;

import static org.apache.commons.codec.binary.Base64.encodeBase64String;
import static uk.gov.notifications.client.authorisation.crypto.CryptoUtils.keyFromString;

/**
 * Signs data with HMAC-SHA256 algorithm.
 */
public final class HS256Signer implements Signer {

    /**
     * Signs payload with a secret key.
     *
     * @param payload   payload to be signed
     * @param secretKey key to sign data
     * @return base64 encoded encrypted payload
     */
    public String sign(String payload, String secretKey) {

        try {
            Mac instance = Mac.getInstance("HmacSHA256");
            instance.init(keyFromString(secretKey));
            byte[] signatureBytes = instance.doFinal(payload.getBytes(CryptoUtils.ENCODING));

            return encodeBase64String(signatureBytes);
        } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }
}
