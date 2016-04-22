package uk.gov.notifications.client.authorisation;

import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwx.HeaderParameterNames;
import org.jose4j.lang.JoseException;

import uk.gov.notifications.client.authorisation.crypto.HS256Signer;
import uk.gov.notifications.client.authorisation.crypto.Signer;

import java.io.UnsupportedEncodingException;

import static uk.gov.notifications.client.authorisation.crypto.CryptoUtils.keyFromString;


/**
 * Creates a JWT token. JwtTokenCreator uses HMAC-SHA256 signature, by default.
 */
public final class JwtTokenCreator implements RequestTokenCreator {

    private static final String CLAIM_KEY_REQUEST_SIGNATURE = "req";

    private static final String CLAIM_KEY_PAYLOAD_SIGNATURE = "pay";

    private Signer signer;

    public JwtTokenCreator() {
        this(new HS256Signer());
    }

    public JwtTokenCreator(Signer signer) {
        this.signer = signer;
    }

    @Override
    public String create(String resourcePath, String body, String issuer, String secret) {

        try {

            JsonWebSignature jws = new JsonWebSignature();
            jws.setPayload(body);
            jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
            jws.setHeader(HeaderParameterNames.TYPE, "JWT");
            JwtClaims claims = new JwtClaims();
            claims.setIssuer(issuer);
            claims.setIssuedAtToNow();
            claims.setClaim(CLAIM_KEY_REQUEST_SIGNATURE, signer.sign(resourcePath, secret));
            if (body != null) {
                claims.setClaim(CLAIM_KEY_PAYLOAD_SIGNATURE, signer.sign(body, secret));
            }
            jws.setPayload(claims.toJson());
            jws.setKey(keyFromString(secret));

            return jws.getCompactSerialization();

        } catch (JoseException | UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
