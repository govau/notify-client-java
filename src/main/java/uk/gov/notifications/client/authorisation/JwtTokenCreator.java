package uk.gov.notifications.client.authorisation;

import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwx.HeaderParameterNames;
import org.jose4j.lang.JoseException;

import java.io.UnsupportedEncodingException;

import static uk.gov.notifications.client.authorisation.crypto.CryptoUtils.keyFromString;


/**
 * Creates a JWT token. JwtTokenCreator uses HMAC-SHA256 signature, by default.
 */
public final class JwtTokenCreator implements RequestTokenCreator {

    public JwtTokenCreator() {
    }

    @Override
    public String create(String issuer, String secret) {

        try {

            JsonWebSignature jws = new JsonWebSignature();
            jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
            jws.setHeader(HeaderParameterNames.TYPE, "JWT");
            JwtClaims claims = new JwtClaims();
            claims.setIssuer(issuer);
            claims.setIssuedAtToNow();
            jws.setPayload(claims.toJson());
            jws.setKey(keyFromString(secret));

            return jws.getCompactSerialization();

        } catch (JoseException | UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
