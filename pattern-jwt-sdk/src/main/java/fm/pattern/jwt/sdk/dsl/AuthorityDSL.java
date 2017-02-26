package fm.pattern.jwt.sdk.dsl;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import fm.pattern.commons.rest.Result;
import fm.pattern.jwt.sdk.AuthoritiesClient;
import fm.pattern.jwt.sdk.model.AuthorityRepresentation;

public class AuthorityDSL extends AbstractDSL<AuthorityDSL, AuthorityRepresentation> {

    // TODO: Replace hard-coded values with configuration.
    private AuthoritiesClient client = new AuthoritiesClient("http://localhost:9600");

    private String name = randomAlphanumeric(10);

    public static AuthorityDSL authority() {
        return new AuthorityDSL();
    }

    public AuthorityDSL withName(String name) {
        this.name = name;
        return this;
    }

    public AuthorityRepresentation build() {
        AuthorityRepresentation representation = create();
        if (!shouldPersist()) {
            return representation;
        }

        Result<AuthorityRepresentation> response = client.create(representation, super.getToken().getAccessToken());
        if (response.rejected()) {
            throw new IllegalStateException("Unable to create authority, response from server: " + response.getErrors().toString());
        }

        return response.getInstance();
    }

    private AuthorityRepresentation create() {
        AuthorityRepresentation representation = new AuthorityRepresentation();
        representation.setName(name);
        return representation;
    }

}
