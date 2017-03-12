package fm.pattern.jwt.sdk.dsl;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import fm.pattern.jwt.sdk.model.GrantTypeRepresentation;

public class GrantTypeDSL extends AbstractDSL<GrantTypeDSL, GrantTypeRepresentation> {

    private String name = randomAlphanumeric(10);
    private String description = "description";

    public static GrantTypeDSL grantType() {
        return new GrantTypeDSL();
    }

    public GrantTypeDSL withName(String name) {
        this.name = name;
        return this;
    }

    public GrantTypeDSL withDescription(String description) {
        this.description = description;
        return this;
    }

    public GrantTypeRepresentation build() {
        GrantTypeRepresentation representation = create();
        return representation;
    }

    private GrantTypeRepresentation create() {
        GrantTypeRepresentation representation = new GrantTypeRepresentation();
        representation.setName(name);
        representation.setDescription(description);
        return representation;
    }

}
