package uk.gov.notifications.client.authorisation.crypto

import spock.lang.Specification

public class HS256SignerSpec extends Specification {

    def signer = new HS256Signer();

    def "Given data and key, when I sign the data, I expect signature to be valid"() {

        expect:
        signer.sign("my data to sign", "key to sign") == "peYN3KZBVhKLmK1fXg+/KWboWzc6FVmLfmULmol64wU="
    }

    def "When I sign the same data twice, I expect consistent results"() {

        def produceSignature = { signer.sign("my data to sign", "key to sign") }

        expect:
        produceSignature() == produceSignature()
    }
}
