package uk.gov.notifications.client.authorisation.crypto;

import java.io.UnsupportedEncodingException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Utils to help manage cryptographic operations.
 */
public class CryptoUtils {

    public static final String ENCODING = "UTF-8";

    public static SecretKey keyFromString(String value)
            throws UnsupportedEncodingException {
        return new SecretKeySpec(value.getBytes(ENCODING), "RAW");
    }
}
