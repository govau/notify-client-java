package uk.gov.notifications.client.authorisation

import org.jose4j.jwt.consumer.JwtConsumer
import org.jose4j.jwt.consumer.JwtConsumerBuilder
import org.jose4j.jwt.consumer.JwtContext
import spock.lang.Specification
import uk.gov.notifications.client.authorisation.crypto.Signer

import javax.crypto.spec.SecretKeySpec

import static groovy.json.JsonOutput.toJson
import static org.jose4j.jws.AlgorithmIdentifiers.HMAC_SHA256
import static uk.gov.notifications.client.http.HttpMethod.POST
import static uk.gov.notifications.testutil.TestStub.SECRET

public class JwtTokenCreatorSpec extends Specification {

    final def reqSignature = "this is a req signature"
    final def paySignature = "this is a pay signature"
    final def url = "/this/is/url"
    final def body = toJson("this": "is some JSON")
    final def signer = Mock(Signer.class)
    final def creator = new JwtTokenCreator(signer)
    final def ISSUER = "SomeIssuer"

    def "Given request without body, when I create a token, then I do not want 'pay' attribute to be set in token payload"() {

        signer.sign(_, _) >> reqSignature

        when:
        def ctx = decode(creator.create(POST.toString() + url, null, ISSUER, SECRET));

        then:
        ctx.jwtClaims.claimsMap["pay"] == null
    }

    def "Given request with body, when I create a token, then I want 'pay' attribute set correctly in the token payload"() {

        signer.sign(_, _) >>> [reqSignature, paySignature]

        when:
        def ctx = decode(creator.create(POST.toString() + url, body, ISSUER, SECRET));

        then:
        ctx.jwtClaims.claimsMap["pay"] == paySignature
    }

    def "When I create a token, then I want 'alg'=HS256 and 'typ'=JWT headers to be set"() {

        when:
        def ctx = decode(creator.create(POST.toString() + url, null, ISSUER, SECRET));

        then:
        ctx.joseObjects[0].with {
            algorithmHeaderValue == HMAC_SHA256
            getHeader("typ") == "JWT"
        }
    }

    def "When I create a token, then I want 'iss' field (Issuer) to be set correctly in the token payload"() {

        signer.sign(_, _) >> reqSignature

        when:
        def ctx = decode(creator.create(POST.toString() + url, null, ISSUER, SECRET));

        then:
        ctx.jwtClaims.issuer == ISSUER
    }

    def "When I create a token, then I want 'req' field set correctly in the token payload"() {

        signer.sign(_, _) >> reqSignature

        when:
        def ctx = decode(creator.create(POST.toString() + url, null, ISSUER, SECRET));

        then:
        ctx.jwtClaims.claimsMap['req'] == reqSignature
    }

    private static JwtContext decode(String token) throws Exception {

        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setVerificationKey(new SecretKeySpec(SECRET.getBytes("UTF-8"), "RAW"))
                .setRequireIssuedAt()
                .build();

        return jwtConsumer.process(token);
    }
}
