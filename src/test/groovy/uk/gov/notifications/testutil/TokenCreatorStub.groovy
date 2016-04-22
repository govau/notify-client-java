package uk.gov.notifications.testutil;


import uk.gov.notifications.client.authorisation.RequestTokenCreator;

public class TokenCreatorStub implements RequestTokenCreator {

    String createdToken, requestPath, issuer, body, secret;

    @Override
    public String create(String resourcePath, String body, String issuer, String secret) {
        this.requestPath = resourcePath;
        this.issuer = issuer;
        this.body = body;
        this.secret = secret;

        createdToken;
    }
}
