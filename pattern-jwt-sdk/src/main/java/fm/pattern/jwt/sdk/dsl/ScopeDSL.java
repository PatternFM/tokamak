package fm.pattern.jwt.sdk.dsl;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import fm.pattern.commons.rest.JwtClientProperties;
import fm.pattern.commons.rest.Result;
import fm.pattern.jwt.sdk.ScopesClient;
import fm.pattern.jwt.sdk.model.ScopeRepresentation;

public class ScopeDSL extends AbstractDSL<ScopeDSL, ScopeRepresentation> {

    private ScopesClient client = new ScopesClient(JwtClientProperties.getEndpoint());

    private String name = randomAlphanumeric(10);
    private String description = "description";

    public static ScopeDSL scope() {
        return new ScopeDSL();
    }

    public ScopeDSL withName(String name) {
        this.name = name;
        return this;
    }

    public ScopeDSL withDescription(String description) {
        this.description = description;
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
        representation.setDescription(description);
        return representation;
    }

}
