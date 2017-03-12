package fm.pattern.tokamak.sdk.dsl;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import fm.pattern.tokamak.sdk.AudiencesClient;
import fm.pattern.tokamak.sdk.JwtClientProperties;
import fm.pattern.tokamak.sdk.commons.Result;
import fm.pattern.tokamak.sdk.model.AudienceRepresentation;

public class AudienceDSL extends AbstractDSL<AudienceDSL, AudienceRepresentation> {

    private AudiencesClient client = new AudiencesClient(JwtClientProperties.getEndpoint());

    private String name = randomAlphanumeric(10);
    private String description = "description";

    public static AudienceDSL audience() {
        return new AudienceDSL();
    }

    public AudienceDSL withName(String name) {
        this.name = name;
        return this;
    }

    public AudienceDSL withDescription(String description) {
        this.description = description;
        return this;
    }

    public AudienceRepresentation build() {
        AudienceRepresentation representation = create();
        if (!shouldPersist()) {
            return representation;
        }

        Result<AudienceRepresentation> response = client.create(representation, super.getToken().getAccessToken());
        if (response.rejected()) {
            throw new IllegalStateException("Unable to create audience, response from server: " + response.getErrors().toString());
        }

        return response.getInstance();
    }

    private AudienceRepresentation create() {
        AudienceRepresentation representation = new AudienceRepresentation();
        representation.setName(name);
        representation.setDescription(description);
        return representation;
    }

}
