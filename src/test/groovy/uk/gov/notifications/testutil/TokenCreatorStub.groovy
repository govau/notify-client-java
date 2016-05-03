package uk.gov.notifications.testutil;


import uk.gov.notifications.client.authorisation.RequestTokenCreator;

public class TokenCreatorStub implements RequestTokenCreator {

    String createdToken, issuer, secret;

    @Override
    public String create(String issuer, String secret) {
        this.issuer = issuer;
        this.secret = secret;

        createdToken;
    }
}
