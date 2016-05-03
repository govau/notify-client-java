package uk.gov.notifications.client.authorisation

import org.jose4j.jwt.NumericDate
import org.jose4j.jwt.consumer.JwtConsumer
import org.jose4j.jwt.consumer.JwtConsumerBuilder
import org.jose4j.jwt.consumer.JwtContext
import spock.lang.Specification

import javax.crypto.spec.SecretKeySpec

import static groovy.json.JsonOutput.toJson
import static org.jose4j.jws.AlgorithmIdentifiers.HMAC_SHA256
import static uk.gov.notifications.testutil.TestStub.SECRET

public class JwtTokenCreatorSpec extends Specification {

    final def url = "/this/is/url"
    final def body = toJson("this": "is some JSON")
    final def creator = new JwtTokenCreator()
    final def ISSUER = "SomeIssuer"

    def "When I create a token, then I want 'alg'=HS256 and 'typ'=JWT headers to be set"() {

        when:
        def ctx = decode(creator.create(ISSUER, SECRET));

        then:
        ctx.joseObjects[0].with {
            algorithmHeaderValue == HMAC_SHA256
            getHeader("typ") == "JWT"
        }
    }

    def "When I create a token, then I want 'iss' field (Issuer) to be set correctly in the token payload"() {

        when:
        def ctx = decode(creator.create(ISSUER, SECRET));

        then:
        ctx.jwtClaims.issuer == ISSUER
        NumericDate.fromMilliseconds(System.currentTimeMillis()).isOnOrAfter(ctx.jwtClaims.issuedAt);
    }

    private static JwtContext decode(String token) throws Exception {

        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setVerificationKey(new SecretKeySpec(SECRET.getBytes("UTF-8"), "RAW"))
                .setRequireIssuedAt()
                .build();

        return jwtConsumer.process(token);
    }
}
