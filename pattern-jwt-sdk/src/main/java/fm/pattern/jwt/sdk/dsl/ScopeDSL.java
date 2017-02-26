package fm.pattern.jwt.sdk.dsl;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import fm.pattern.commons.rest.Result;
import fm.pattern.jwt.sdk.ScopesClient;
import fm.pattern.jwt.sdk.model.ScopeRepresentation;

public class ScopeDSL extends AbstractDSL<ScopeDSL, ScopeRepresentation> {

    // TODO: Replace hard-coded values with configuration.
    private ScopesClient client = new ScopesClient("http://localhost:9600");

    private String name = randomAlphanumeric(10);

    public static ScopeDSL scope() {
        return new ScopeDSL();
    }

    public ScopeDSL withName(String name) {
        this.name = name;
        return this;
    }

    public ScopeRepresentation build() {
        ScopeRepresentation representation = create();
        if (!shouldPersist()) {
            return representation;
        }

        Result<ScopeRepresentation> response = client.create(representation, super.getToken().getAccessToken());
        if (response.rejected()) {
            throw new IllegalStateException("Unable to create scope, response from server: " + response.getErrors().toString());
        }

        return response.getInstance();
    }

    private ScopeRepresentation create() {
        ScopeRepresentation representation = new ScopeRepresentation();
        representation.setName(name);
        return representation;
    }

}
